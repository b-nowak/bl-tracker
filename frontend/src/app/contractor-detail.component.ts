import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from './api.service';
import { ContractorSummary, Invoice, TimeEntry } from './types';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
  <div class="card" *ngIf="contractor">
    <a routerLink="/">← Back</a>
    <h2>{{contractor.firstName}} {{contractor.lastName}}</h2>
    <div class="form-grid">
      <div><strong>Vendor:</strong> {{contractor.vendorCompany}}</div>
      <div><strong>Hourly rate:</strong> {{contractor.hourlyRate | currency}} ({{contractor.rateType}})</div>
      <div><strong>Contract hours:</strong> {{contractor.contractHoursTotal}}</div>
      <div><strong>Used hours:</strong> {{contractor.usedHours}}</div>
      <div><strong>Remaining hours:</strong> {{contractor.remainingHours}}</div>
      <div><strong>Remaining budget:</strong> {{contractor.remainingBudget | currency}}</div>
      <div><strong>Burn-out date:</strong> {{contractor.burnOutDate || 'N/A'}}</div>
    </div>
  </div>

  <div class="card">
    <div style="display:flex;gap:1rem;">
      <button class="button" [class.secondary]="activeTab!=='time'" (click)="activeTab='time'">Time entries</button>
      <button class="button" [class.secondary]="activeTab!=='invoices'" (click)="activeTab='invoices'">Invoices</button>
      <button class="button" [class.secondary]="activeTab!=='notes'" (click)="activeTab='notes'">Notes</button>
    </div>

    <div *ngIf="activeTab==='time'">
      <h3>Add time entry</h3>
      <div class="form-grid">
        <div><label>Date</label><input type="date" [(ngModel)]="timeEntry.date"></div>
        <div><label>Hours</label><input type="number" [(ngModel)]="timeEntry.hours"></div>
        <div><label>Description</label><input [(ngModel)]="timeEntry.description"></div>
      </div>
      <button class="button" (click)="saveTimeEntry()">Save entry</button>
      <input type="file" (change)="uploadCsv($event)" accept=".csv" style="margin-left:1rem;" />
      <p *ngIf="csvMessage">{{csvMessage}}</p>

      <table class="table" style="margin-top:1rem;">
        <thead><tr><th>Date</th><th>Hours</th><th>Description</th><th></th></tr></thead>
        <tbody>
          <tr *ngFor="let t of timeEntries">
            <td>{{t.date}}</td><td>{{t.hours}}</td><td>{{t.description}}</td>
            <td><button class="button secondary" (click)="deleteTimeEntry(t.id!)">Delete</button></td>
          </tr>
        </tbody>
      </table>
    </div>

    <div *ngIf="activeTab==='invoices'">
      <h3>Add invoice</h3>
      <div class="form-grid">
        <div><label>Number</label><input [(ngModel)]="invoice.invoiceNumber"></div>
        <div><label>Date</label><input type="date" [(ngModel)]="invoice.invoiceDate"></div>
        <div><label>Amount</label><input type="number" [(ngModel)]="invoice.amount"></div>
        <div><label>PDF</label><input type="file" (change)="selectPdf($event)" accept="application/pdf"></div>
      </div>
      <button class="button" (click)="saveInvoice()">Save invoice</button>
      <table class="table" style="margin-top:1rem;">
        <thead><tr><th>Number</th><th>Date</th><th>Amount</th><th>PDF</th><th></th></tr></thead>
        <tbody>
          <tr *ngFor="let inv of invoices">
            <td>{{inv.invoiceNumber}}</td><td>{{inv.invoiceDate}}</td><td>{{inv.amount | currency}}</td>
            <td><a *ngIf="inv.id" [href]="'/api/invoices/' + inv.id + '/pdf'" target="_blank">Preview</a></td>
            <td><button class="button secondary" (click)="deleteInvoice(inv.id!)">Delete</button></td>
          </tr>
        </tbody>
      </table>
    </div>

    <div *ngIf="activeTab==='notes'">
      <textarea rows="5" [(ngModel)]="notes" placeholder="Notes" (blur)="saveNotes()"></textarea>
    </div>
  </div>
  `
})
export class ContractorDetailComponent implements OnInit {
  contractor?: ContractorSummary;
  timeEntries: TimeEntry[] = [];
  invoices: Invoice[] = [];
  timeEntry: Partial<TimeEntry> = {};
  invoice: Partial<Invoice> = {};
  selectedPdf?: File;
  csvMessage = '';
  notes = '';
  activeTab: 'time' | 'invoices' | 'notes' = 'time';

  constructor(private route: ActivatedRoute, private api: ApiService) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadContractor(id);
  }

  loadContractor(id: number) {
    this.api.getContractor(id).subscribe(c => {
      this.contractor = c;
      this.timeEntry.contractor = c as any;
      this.invoice.contractor = c as any;
      this.notes = (c as any).notes || '';
      this.loadTimeEntries();
      this.loadInvoices();
    });
  }

  loadTimeEntries() {
    if (!this.contractor) return;
    this.api.getTimeEntries(this.contractor.id).subscribe(d => this.timeEntries = d);
  }

  loadInvoices() {
    if (!this.contractor) return;
    this.api.getInvoices(this.contractor.id).subscribe(d => this.invoices = d);
  }

  saveTimeEntry() {
    if (!this.contractor) return;
    this.timeEntry.contractor = this.contractor as any;
    this.api.saveTimeEntry(this.timeEntry as TimeEntry).subscribe(() => {
      this.timeEntry = {};
      this.loadTimeEntries();
      this.loadContractor(this.contractor!.id);
    });
  }

  deleteTimeEntry(id: number) {
    this.api.deleteTimeEntry(id).subscribe(() => {
      this.loadTimeEntries();
      this.loadContractor(this.contractor!.id);
    });
  }

  uploadCsv(event: any) {
    const file = event.target.files?.[0];
    if (!file) return;
    this.api.uploadTimeEntriesCsv(file).subscribe(msg => {
      this.csvMessage = msg as any;
      this.loadTimeEntries();
      this.loadContractor(this.contractor!.id);
    });
  }

  selectPdf(event: any) {
    this.selectedPdf = event.target.files?.[0];
  }

  saveInvoice() {
    if (!this.contractor) return;
    this.invoice.contractor = this.contractor as any;
    this.api.saveInvoice(this.invoice, this.selectedPdf).subscribe(() => {
      this.invoice = {};
      this.selectedPdf = undefined;
      this.loadInvoices();
      this.loadContractor(this.contractor!.id);
    });
  }

  deleteInvoice(id: number) {
    this.api.deleteInvoice(id).subscribe(() => {
      this.loadInvoices();
      this.loadContractor(this.contractor!.id);
    });
  }

  saveNotes() {
    if (!this.contractor) return;
    // notes stored on contractor record
    const patch: any = { ...this.contractor, notes: this.notes };
    this.api.updateContractor(this.contractor.id, patch).subscribe(c => this.contractor = c);
  }
}
