export interface ContractorSummary {
  id: number;
  firstName: string;
  lastName: string;
  vendorCompany: string;
  hourlyRate: number;
  rateType: string;
  contractHoursTotal: number;
  usedHours: number;
  remainingHours: number;
  remainingBudget: number;
  burnOutDate?: string;
}

export interface Contractor extends ContractorSummary {
  email: string;
  contractStartDate: string;
  contractEndDate?: string;
  notes?: string;
}

export interface TimeEntry {
  id?: number;
  contractor: Contractor;
  date: string;
  hours: number;
  description: string;
}

export interface Invoice {
  id?: number;
  contractor: Contractor;
  invoiceNumber: string;
  invoiceDate: string;
  amount: number;
  pdfPath?: string;
}
