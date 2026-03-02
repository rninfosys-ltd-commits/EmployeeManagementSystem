import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee } from '../models/employee.model';
import { APP_CONFIG } from '../config/config';

// ================= BASE URL =================
const EMPLOYEE_API_URL = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.EMPLOYEES}`;

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private http: HttpClient) { }

  // ================= GET ALL =================
  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(EMPLOYEE_API_URL);
  }

  // ================= GET BY ID =================
  getEmployeeById(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${EMPLOYEE_API_URL}/${id}`);
  }

  // ================= CREATE =================
  createEmployee(employee: Employee): Observable<Employee> {
    return this.http.post<Employee>(EMPLOYEE_API_URL, employee);
  }

  // ================= UPDATE =================
  updateEmployee(id: number, employee: Employee): Observable<Employee> {
    return this.http.put<Employee>(`${EMPLOYEE_API_URL}/${id}`, employee);
  }

  // ================= DELETE =================
  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<void>(`${EMPLOYEE_API_URL}/${id}`);
  }
}
