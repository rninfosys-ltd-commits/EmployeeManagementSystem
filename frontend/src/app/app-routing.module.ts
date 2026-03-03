import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { EmployeeListComponent } from './components/employee-list/employee-list.component';
import { EmployeeFormComponent } from './components/employee-form/employee-form.component';
import { AuthGuard } from './guards/auth.guard';

import { CustomerFormComponent } from './customer-form/customer-form.component';
import { CustomerListComponent } from './customer-list/customer-list.component';
import { UserFormComponent } from './user-form/user-form.component';
import { UserListComponent } from './user-list/user-list.component';
import { CreateUserComponent } from './admin/create-user/create-user.component';
import { KmFormComponent } from './components/km-form/km-form.component';
import { KmListComponent } from './components/km-list/km-list.component';
import { ReceiptFormComponent } from './components/receipt-form/receipt-form.component';
import { ReceiptListComponent } from './components/receipt-list/receipt-list.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ProductFormComponent } from './components/product-form/product-form.component';
import { ProductListComponent } from './components/product-list/product-list.component';
// import { PurchaseOrderFormComponent } from './components/purchase-order-form/purchase-order-form.component';
// import { PurchaseOrderListComponent } from './components/purchase-order-list/purchase-order-list.component';
// import { PostProductComponent } from './post-product/post-product.component';
import { PurchaseOrderComponent } from './purchase-order/purchase-order.component';
import { PriceListComponent } from './components/price-list/price-list.component';
import { RootMasterComponent } from './components/root-master/root-master.component';
import { UserDetailsComponent } from './components/user-details/user-details.component';
import { SellOrderComponent } from './sell-order/sell-order.component';
import { CityMasterComponent } from './components/city-master/city-master.component';
import { DistrictMasterComponent } from './components/district-master/district-master.component';
import { StateMasterComponent } from './components/state-master/state-master.component';
import { TalukaMasterComponent } from './components/taluka-master/taluka-master.component';
import { ProjectComponent } from './project/project.component';
import { LeadComponent } from './lead/lead.component';
import { InquiryComponent } from './inquiry/inquiry.component';
import { InquiryScheduleComponent } from './inquiry-schedule/inquiry-schedule.component';
import { HomeDashboardComponent } from './home-dashboard/home-dashboard.component';
import { ProductionEntryComponent } from './production-entry/production-entry.component';
import { CastingHallReportComponent } from './casting-hall-report/casting-hall-report.component';
import { WireCuttingReportComponent } from './wire-cutting-report/wire-cutting-report.component';
import { AutoclaveComponent } from './autoclave/autoclave.component';
import { BlockSeparatingComponent } from './block-separating/block-separating.component';
import { CubeTestComponent } from './cube-test-component/cube-test-component.component';
import { RejectionComponentComponent } from './rejection-component/rejection-component.component';
import { ProductionDashboardComponent } from './production-dashboard/production-dashboard.component';
import { TransactionExportComponent } from './components/transaction-export/transaction-export.component';
import { HorizontalReportComponent } from './horizontal-report/horizontal-report.component';
// import { PartyPriceComponent } from './components/price-list/price-list.component';


const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  { path: 'employees', component: EmployeeListComponent, canActivate: [AuthGuard] },
  { path: 'employees/add', component: EmployeeFormComponent, canActivate: [AuthGuard] },
  { path: 'employees/edit/:id', component: EmployeeFormComponent, canActivate: [AuthGuard] },

  { path: 'customers', component: CustomerListComponent, canActivate: [AuthGuard] },
  { path: 'customers/add', component: CustomerFormComponent, canActivate: [AuthGuard] },
  { path: 'customers/edit/:id', component: CustomerFormComponent, canActivate: [AuthGuard] },

  { path: 'users', component: UserListComponent, canActivate: [AuthGuard] },
  { path: 'users/add', component: UserFormComponent, canActivate: [AuthGuard] },
  { path: 'users/edit/:id', component: UserFormComponent, canActivate: [AuthGuard] },

  { path: 'admin/create-user', component: CreateUserComponent, canActivate: [AuthGuard] },

  // ⭐ KM MODULE (MUST BE BEFORE **)
  { path: 'km-form', component: KmFormComponent, canActivate: [AuthGuard] },
  { path: 'km-list', component: KmListComponent, canActivate: [AuthGuard] },


  { path: 'receipts', component: ReceiptListComponent, canActivate: [AuthGuard] },
  { path: 'receipts/new', component: ReceiptFormComponent, canActivate: [AuthGuard] },
  { path: 'receipts/edit/:id', component: ReceiptFormComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  // WILDCARD MUST BE LAST

  { path: 'products', component: ProductListComponent, canActivate: [AuthGuard] },
  { path: 'products/new', component: ProductFormComponent, canActivate: [AuthGuard] },
  { path: 'purchase-orders', component: PurchaseOrderComponent, canActivate: [AuthGuard] },
  { path: 'sell-orders', component: SellOrderComponent, canActivate: [AuthGuard] },
  // { path: 'party-price', component: PartyPriceComponent, canActivate: [AuthGuard] },
  { path: 'price-list', component: PriceListComponent, canActivate: [AuthGuard] },
  { path: 'root-master', component: RootMasterComponent, canActivate: [AuthGuard] },
  { path: 'user-details', component: UserDetailsComponent, canActivate: [AuthGuard] },
  { path: 'projects', component: ProjectComponent, canActivate: [AuthGuard] },
  { path: 'leads', component: LeadComponent, canActivate: [AuthGuard] },
  { path: 'inquiries', component: InquiryComponent, canActivate: [AuthGuard] },

  { path: 'state-master', component: StateMasterComponent, canActivate: [AuthGuard] },
  { path: 'district-master', component: DistrictMasterComponent, canActivate: [AuthGuard] },
  { path: 'taluka-master', component: TalukaMasterComponent, canActivate: [AuthGuard] },
  { path: 'city-master', component: CityMasterComponent, canActivate: [AuthGuard] },

  { path: 'inquiry-schedule', component: InquiryScheduleComponent, canActivate: [AuthGuard] },
  { path: 'home', component: HomeDashboardComponent, canActivate: [AuthGuard] },   // home dashboard
  { path: 'production-entry', component: ProductionEntryComponent, canActivate: [AuthGuard] },
  { path: 'casting-hall-report', component: CastingHallReportComponent, canActivate: [AuthGuard] },
  { path: 'wire-cutting-report', component: WireCuttingReportComponent, canActivate: [AuthGuard] },
  {
    path: 'autoclave',
    component: AutoclaveComponent,
    canActivate: [AuthGuard]
  },
  { path: 'cube-test', component: CubeTestComponent, canActivate: [AuthGuard] },
  { path: 'block-separating', component: BlockSeparatingComponent, canActivate: [AuthGuard] },
  { path: 'rejection', component: RejectionComponentComponent, canActivate: [AuthGuard] },
  {
    path: 'production-dashboard',
    component: ProductionDashboardComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'transaction-export',
    component: TransactionExportComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'horizontal-report',
    component: HorizontalReportComponent,
    canActivate: [AuthGuard]
  },

  { path: '**', redirectTo: '/login' }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
