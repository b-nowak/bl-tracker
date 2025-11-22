package com.example.bltracker.repository;

import com.example.bltracker.model.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractorRepository extends JpaRepository<Contractor, Long> {
    List<Contractor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrVendorCompanyContainingIgnoreCase(String firstName, String lastName, String vendorCompany);
    Contractor findByEmail(String email);
}
