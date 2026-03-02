import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CartItem, PlaceSellOrderDto, SellService } from '../services/sell-order.service';


@Component({
  selector: 'app-sell-order',
  templateUrl: './sell-order.component.html',
  styleUrls: ['./sell-order.component.css']
})

export class SellOrderComponent implements OnInit {


  customers: any[] = [];
  products: any[] = [];
  cartItems: CartItem[] = [];

  originalOrders: PlaceSellOrderDto[] = [];
  myOrders: PlaceSellOrderDto[] = [];
  selectedOrder: PlaceSellOrderDto | null = null;

  cartForm: FormGroup;
  selectedUserId: number | null = null;
  selectedCustomerId: number | null = null;
  selectedProduct: any = null;
  productPrice = 0;
  totalAmount = 0;
  dateValue = '';
  showOrderForm = false;

  // Filters
  filterFromDate = '';
  filterToDate = '';
  filterPartyName = '';
  filterMinAmount: number | null = null;
  filterMaxAmount: number | null = null;

  currentPage = 1;
  pageSize = 9;
  totalPages = 0;
  paginatedOrders: PlaceSellOrderDto[] = [];

  constructor(
    private fb: FormBuilder,
    private sellService: SellService
  ) {
    this.cartForm = this.fb.group({
      productId: [null, Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.loadCustomers();
    this.loadAllProducts();
    this.loadOrders();
    this.dateValue = new Date().toISOString().substring(0, 10);
  }

  loadCustomers() {
    this.sellService.getCustomers().subscribe(res => this.customers = res);
  }

  onCustomerChange(e: any) {
    this.selectedCustomerId = +e.target.value;
    this.cartItems = [];
    this.totalAmount = 0;
  }

  onProductChange(e: any) {
    const id = +e.target.value;
    this.selectedProduct = this.products.find(p => p.id === id);
    this.productPrice = this.selectedProduct?.unitPrice || 0;
  }

  addToCart() {
    const qty = this.cartForm.value.quantity;
    const item: CartItem = {
      productId: this.selectedProduct.id,
      productName: this.selectedProduct.name,
      price: this.productPrice,
      quantity: qty
    };
    this.cartItems.push(item);
    this.totalAmount += item.price * item.quantity;
  }

  editItem(it: CartItem) {
    it.isEditing = true;
    it.tempQty = it.quantity;
    it.tempPrice = it.price;
  }

  saveItem(it: CartItem) {
    this.totalAmount -= it.price * it.quantity;
    it.quantity = it.tempQty!;
    it.price = it.tempPrice!;
    this.totalAmount += it.price * it.quantity;
    it.isEditing = false;
  }

  removeItem(i: number) {
    const r = this.cartItems.splice(i, 1)[0];
    this.totalAmount -= r.price * r.quantity;
  }

  placeOrder() {
    const dto: PlaceSellOrderDto = {
      userId: this.selectedUserId!,
      customerId: this.selectedCustomerId!,
      cartItems: this.cartItems,
      orderDescription: 'Sell Order'
    };
    this.sellService.placeOrder(dto).subscribe(() => {
      this.showOrderForm = false;
      this.loadOrders();
    });
  }

  loadOrders() {
    this.sellService.getSellOrders().subscribe(res => {
      this.originalOrders = res.reverse();
      this.myOrders = [...this.originalOrders];
      this.totalPages = Math.ceil(this.myOrders.length / this.pageSize);
      this.setPage(1);
    });
  }

  applyFilters() {
    this.myOrders = this.originalOrders.filter(o => {
      const matchDate = (!this.filterFromDate || new Date(o.date!) >= new Date(this.filterFromDate)) &&
        (!this.filterToDate || new Date(o.date!) <= new Date(this.filterToDate));
      // Sell Orders often have userName or customerName. SellService DTO uses userId mostly but let's assume userName from backend join
      const matchParty = !this.filterPartyName || (o as any).userName?.toLowerCase().includes(this.filterPartyName.toLowerCase());
      const matchAmount = (this.filterMinAmount === null || (o.amount || 0) >= this.filterMinAmount) &&
        (this.filterMaxAmount === null || (o.amount || 0) <= this.filterMaxAmount);
      return matchDate && matchParty && matchAmount;
    });
    this.totalPages = Math.ceil(this.myOrders.length / this.pageSize);
    this.setPage(1);
  }

  clearFilters() {
    this.filterFromDate = '';
    this.filterToDate = '';
    this.filterPartyName = '';
    this.filterMinAmount = null;
    this.filterMaxAmount = null;
    this.myOrders = [...this.originalOrders];
    this.totalPages = Math.ceil(this.myOrders.length / this.pageSize);
    this.setPage(1);
  }

  exportData(format: string) {
    if (!format) return;
    const from = this.filterFromDate;
    const to = this.filterToDate;
    const party = this.filterPartyName;
    const min = this.filterMinAmount ?? '';
    const max = this.filterMaxAmount ?? '';

    this.sellService.exportTransactions('SALES', format, from, to, party, min, max).subscribe({
      next: (blob) => {
        const fileName = `Sale_Report_${new Date().getTime()}.${format === 'excel' ? 'xlsx' : 'pdf'}`;
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = fileName;
        link.click();
      },
      error: (err) => {
        console.error('Export failed', err);
        alert('Export failed: ' + (err.message || 'Unknown error'));
      }
    });
  }

  setPage(p: number) {
    this.currentPage = p;
    const s = (p - 1) * this.pageSize;
    this.paginatedOrders = this.myOrders.slice(s, s + this.pageSize);
  }

  nextPage() { this.setPage(this.currentPage + 1); }
  prevPage() { this.setPage(this.currentPage - 1); }

  getOrderDetailsById(id: number) {
    this.sellService.getSellOrderDetails(id).subscribe(res => this.selectedOrder = res);
  }

  loadAllProducts() {
    this.sellService.getAllProducts().subscribe(res => this.products = res);
  }

  calculateGrandTotal(items: CartItem[] | undefined): number {
    if (!items || items.length === 0) {
      return 0;
    }
    return items.reduce(
      (sum, item) => sum + (item.price * item.quantity),
      0
    );
  }

  // ================= DROPDOWN STATE =================
  customerOpen = false;
  productOpen = false;

  selectedCustomer: any = null;

  // ================= TOGGLES =================
  toggleCustomerDropdown() {
    this.customerOpen = !this.customerOpen;
    this.productOpen = false;
  }

  toggleProductDropdown() {
    this.productOpen = !this.productOpen;
    this.customerOpen = false;
  }

  // ================= SELECT CUSTOMER =================
  selectCustomer(c: any) {
    this.selectedCustomer = c;
    this.selectedCustomerId = c.id;
    this.cartItems = [];
    this.totalAmount = 0;
    this.customerOpen = false;
  }

  // ================= SELECT PRODUCT =================
  selectProduct(p: any) {
    this.selectedProduct = p;
    this.productPrice = p.unitPrice || 0;
    this.cartForm.patchValue({ productId: p.id });
    this.productOpen = false;
  }



}
