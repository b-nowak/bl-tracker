import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Contractor, ContractorSummary, Invoice, TimeEntry } from './types';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  listContractors(term?: string): Observable<ContractorSummary[]> {
    return this.http.get<ContractorSummary[]>('/api/contractors', { params: term ? { q: term } : {} });
  }

  getContractor(id: number): Observable<ContractorSummary> {
    return this.http.get<ContractorSummary>(`/api/contractors/${id}`);
  }

  createContractor(contractor: Partial<Contractor>): Observable<Contractor> {
    return this.http.post<Contractor>('/api/contractors', contractor);
  }

  updateContractor(id: number, contractor: Partial<Contractor>): Observable<Contractor> {
    return this.http.put<Contractor>(`/api/contractors/${id}`, contractor);
  }

  getTimeEntries(contractorId: number) {
    return this.http.get<TimeEntry[]>(`/api/time-entries/contractor/${contractorId}`);
  }

  saveTimeEntry(entry: TimeEntry) {
    if (entry.id) {
      return this.http.put<TimeEntry>(`/api/time-entries/${entry.id}`, entry);
    }
    return this.http.post<TimeEntry>('/api/time-entries', entry);
  }

  deleteTimeEntry(id: number) {
    return this.http.delete(`/api/time-entries/${id}`);
  }

  uploadTimeEntriesCsv(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post('/api/time-entries/bulk-csv', formData, { responseType: 'text' });
  }

  getInvoices(contractorId: number) {
    return this.http.get<Invoice[]>(`/api/invoices/contractor/${contractorId}`);
  }

  saveInvoice(invoice: Partial<Invoice>, pdf?: File) {
    const formData = new FormData();
    formData.append('contractorId', `${invoice.contractor?.id}`);
    formData.append('invoiceNumber', invoice.invoiceNumber || '');
    formData.append('invoiceDate', invoice.invoiceDate || '');
    formData.append('amount', `${invoice.amount || 0}`);
    if (pdf) {
        formData.append('pdf', pdf);
    }
    return this.http.post<Invoice>('/api/invoices', formData);
  }

  deleteInvoice(id: number) {
    return this.http.delete(`/api/invoices/${id}`);
  }
}
