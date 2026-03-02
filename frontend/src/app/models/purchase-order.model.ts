import { Product } from './product.model';

export interface PurchaseOrderItem {
    product: Product;
    quantity: number;
    price: number;
}

export interface PurchaseOrder {
    id?: number;
    supplierName: string;
    status?: string;
    totalAmount?: number;
    items: PurchaseOrderItem[];
}
