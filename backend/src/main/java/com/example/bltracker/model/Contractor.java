package com.example.bltracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Contractor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String vendorCompany;
    private double hourlyRate;
    private String rateType;
    private double contractHoursTotal;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;

    @Column(length = 4000)
    private String notes;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVendorCompany() {
        return vendorCompany;
    }

    public void setVendorCompany(String vendorCompany) {
        this.vendorCompany = vendorCompany;
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

    public LocalDate getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public LocalDate getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(LocalDate contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
