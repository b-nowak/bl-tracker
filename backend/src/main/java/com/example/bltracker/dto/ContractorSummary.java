package com.example.bltracker.dto;

import java.time.LocalDate;

public class ContractorSummary {
    private Long id;
    private String firstName;
    private String lastName;
    private String vendorCompany;
    private String email;
    private double hourlyRate;
    private String rateType;
    private double contractHoursTotal;
    private java.time.LocalDate contractStartDate;
    private java.time.LocalDate contractEndDate;
    private String notes;
    private double usedHours;
    private double remainingHours;
    private double remainingBudget;
    private LocalDate burnOutDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getVendorCompany() {
        return vendorCompany;
    }

    public void setVendorCompany(String vendorCompany) {
        this.vendorCompany = vendorCompany;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public double getContractHoursTotal() {
        return contractHoursTotal;
    }

    public void setContractHoursTotal(double contractHoursTotal) {
        this.contractHoursTotal = contractHoursTotal;
    }

    public java.time.LocalDate getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(java.time.LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public java.time.LocalDate getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(java.time.LocalDate contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getUsedHours() {
        return usedHours;
    }

    public void setUsedHours(double usedHours) {
        this.usedHours = usedHours;
    }

    public double getRemainingHours() {
        return remainingHours;
    }

    public void setRemainingHours(double remainingHours) {
        this.remainingHours = remainingHours;
    }

    public double getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(double remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public LocalDate getBurnOutDate() {
        return burnOutDate;
    }

    public void setBurnOutDate(LocalDate burnOutDate) {
        this.burnOutDate = burnOutDate;
    }
}
