import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

/* ================= COMPONENTS ================= */

// Auth
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';

// Layout
import { NavbarComponent } from './components/navbar/navbar.component';

// Employee
import { EmployeeListComponent } from './components/employee-list/employee-list.component';
import { EmployeeFormComponent } from './components/employee-form/employee-form.component';

// Customer
import { CustomerFormComponent } from './customer-form/customer-form.component';
import { CustomerListComponent } from './customer-list/customer-list.component';

// User
import { UserFormComponent } from './user-form/user-form.component';
import { UserListComponent } from './user-list/user-list.component';
import { CreateUserComponent } from './admin/create-user/create-user.component';
import { UserDetailsComponent } from './components/user-details/user-details.component';

// KM
import { KmFormComponent } from './components/km-form/km-form.component';
import { KmListComponent } from './components/km-list/km-list.component';

// Receipt
import { ReceiptFormComponent } from './components/receipt-form/receipt-form.component';
import { ReceiptListComponent } from './components/receipt-list/receipt-list.component';

// Profile
import { ProfileComponent } from './components/profile/profile.component';

// Product
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductFormComponent } from './components/product-form/product-form.component';
import { PostProductComponent } from './post-product/post-product.component';

// Purchase Order
import { PurchaseOrderComponent } from './purchase-order/purchase-order.component';

// Root
import { RootMasterComponent } from './components/root-master/root-master.component';

// ✅ PRICE LIST (THIS WAS MISSING BEFORE)
// import { PriceListComponent } from './components/price-list/price-list.component';
// import PriceListComponent from './components/price-list/price-list.component';

/* ================= ANGULAR MATERIAL ================= */

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { MatDialogModule } from '@angular/material/dialog';

/* ================= INTERCEPTOR ================= */

import { AuthInterceptor } from './interceptors/auth.interceptor';
import { CommonModule } from '@angular/common';
import { PriceListComponent } from './components/price-list/price-list.component';
import { SellOrderComponent } from './sell-order/sell-order.component';
import { StateMasterComponent } from './components/state-master/state-master.component';
import { DistrictMasterComponent } from './components/district-master/district-master.component';
import { TalukaMasterComponent } from './components/taluka-master/taluka-master.component';
import { CityMasterComponent } from './components/city-master/city-master.component';
import { ProjectComponent } from './project/project.component';
import { LeadComponent } from './lead/lead.component';
import { InquiryComponent } from './inquiry/inquiry.component';
import { InquiryScheduleComponent } from './inquiry-schedule/inquiry-schedule.component';
import { HomeDashboardComponent } from './home-dashboard/home-dashboard.component';
import { ProductionEntryComponent } from './production-entry/production-entry.component';
import { CastingHallReportComponent } from './casting-hall-report/casting-hall-report.component';
import { WireCuttingReportComponent } from './wire-cutting-report/wire-cutting-report.component';
import { AutoclaveComponent } from './autoclave/autoclave.component';
// import { CastingProductionComponent } from './casting-production/casting-production.component';
import { BlockSeparatingComponent } from './block-separating/block-separating.component';
// import { CubeTestComponentComponent } from './cube-test-component/cube-test-component.component';
import { CubeTestComponent } from './cube-test-component/cube-test-component.component';
import { RejectionComponentComponent } from './rejection-component/rejection-component.component';
import { ProductionDashboardComponent } from './production-dashboard/production-dashboard.component';
import { NotificationComponent } from './components/notification/notification.component';
import { TransactionExportComponent } from './components/transaction-export/transaction-export.component';
import { HorizontalReportComponent } from './horizontal-report/horizontal-report.component';


@NgModule({
  declarations: [
    AppComponent,

    // Auth
    LoginComponent,
    RegisterComponent,

    // Layout
    NavbarComponent,

    // Employee
    EmployeeListComponent,
    EmployeeFormComponent,

    // Customer
    CustomerFormComponent,
    CustomerListComponent,

    // User
    UserListComponent,
    UserFormComponent,
    CreateUserComponent,
    UserDetailsComponent,

    // KM
    KmFormComponent,
    KmListComponent,

    // Receipt
    ReceiptFormComponent,
    ReceiptListComponent,

    // Profile
    ProfileComponent,

    // Product
    ProductListComponent,
    ProductFormComponent,
    PostProductComponent,

    // Purchase Order
    PurchaseOrderComponent,
    SellOrderComponent,

    // Root
    RootMasterComponent,

    // ✅ Price List
    PriceListComponent,
    StateMasterComponent,
    DistrictMasterComponent,
    TalukaMasterComponent,
    CityMasterComponent,
    ProjectComponent,
    LeadComponent,
    InquiryComponent,
    InquiryScheduleComponent,
    HomeDashboardComponent,
    ProductionEntryComponent,
    CastingHallReportComponent,
    WireCuttingReportComponent,
    AutoclaveComponent,
    // CastingProductionComponent,
    BlockSeparatingComponent,
    CubeTestComponent,
    RejectionComponentComponent,
    ProductionDashboardComponent,
    NotificationComponent,
    TransactionExportComponent,
    HorizontalReportComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,

    // Angular Material
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatDividerModule,
    MatDialogModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
