import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ApiService } from './api.service';
import { ContractorSummary } from './types';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
  <div class="card">
    <div style="display:flex;justify-content:space-between;align-items:center;">
      <h2>Contractors</h2>
      <div>
        <input placeholder="Search name or vendor" [(ngModel)]="search" (ngModelChange)="load()" style="width:240px;">
      </div>
    </div>
    <div class="form-grid">
      <div>
        <label>First name</label>
        <input [(ngModel)]="newContractor.firstName">
      </div>
      <div>
        <label>Last name</label>
        <input [(ngModel)]="newContractor.lastName">
      </div>
      <div>
        <label>Email</label>
        <input [(ngModel)]="newContractor.email">
      </div>
      <div>
        <label>Vendor</label>
        <input [(ngModel)]="newContractor.vendorCompany">
      </div>
      <div>
        <label>Hourly rate</label>
        <input type="number" [(ngModel)]="newContractor.hourlyRate">
      </div>
      <div>
        <label>Rate type</label>
        <select [(ngModel)]="newContractor.rateType">
          <option value="net">Net</option>
          <option value="gross">Gross</option>
        </select>
      </div>
      <div>
        <label>Total contract hours</label>
        <input type="number" [(ngModel)]="newContractor.contractHoursTotal">
      </div>
      <div>
        <label>Start date</label>
        <input type="date" [(ngModel)]="newContractor.contractStartDate">
      </div>
      <div>
        <label>End date</label>
        <input type="date" [(ngModel)]="newContractor.contractEndDate">
      </div>
    </div>
    <div style="text-align:right;">
      <button class="button" (click)="saveContractor()">Create contractor</button>
    </div>
  </div>
  <div class="card">
    <table class="table">
      <thead>
        <tr>
          <th>Contractor</th>
          <th>Vendor</th>
          <th>Hourly Rate</th>
          <th>Contract Hours</th>
          <th>Used</th>
          <th>Remaining</th>
          <th>Remaining Budget</th>
          <th>Burn-out</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let c of contractors" (click)="open(c)" style="cursor:pointer;">
          <td>{{c.firstName}} {{c.lastName}}</td>
          <td>{{c.vendorCompany}}</td>
          <td>{{c.hourlyRate | currency}}</td>
          <td>{{c.contractHoursTotal}}</td>
          <td>{{c.usedHours}}</td>
          <td><span class="badge">{{c.remainingHours}}</span></td>
          <td>{{c.remainingBudget | currency}}</td>
          <td>{{c.burnOutDate || 'N/A'}}</td>
        </tr>
      </tbody>
    </table>
  </div>
  `
})
export class ContractorListComponent implements OnInit {
  contractors: ContractorSummary[] = [];
  search = '';
  newContractor: any = { rateType: 'net' };

  constructor(private api: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.load();
  }

  load() {
    this.api.listContractors(this.search).subscribe(data => this.contractors = data);
  }

  saveContractor() {
    this.api.createContractor(this.newContractor).subscribe(() => {
      this.newContractor = { rateType: 'net' };
      this.load();
    });
  }

  open(c: ContractorSummary) {
    this.router.navigate(['/contractors', c.id]);
  }
}
