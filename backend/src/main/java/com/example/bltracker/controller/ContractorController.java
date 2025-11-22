package com.example.bltracker.controller;

import com.example.bltracker.dto.ContractorSummary;
import com.example.bltracker.model.Contractor;
import com.example.bltracker.service.ContractorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contractors")
@CrossOrigin
public class ContractorController {
    private final ContractorService contractorService;

    public ContractorController(ContractorService contractorService) {
        this.contractorService = contractorService;
    }

    @GetMapping
    public List<ContractorSummary> list(@RequestParam(required = false) String q) {
        return contractorService.listSummaries(q);
    }

    @GetMapping("/{id}")
    public ContractorSummary get(@PathVariable Long id) {
        return contractorService.getSummary(id);
    }

    @PostMapping
    public Contractor create(@Valid @RequestBody Contractor contractor) {
        return contractorService.save(contractor);
    }

    @PutMapping("/{id}")
    public Contractor update(@PathVariable Long id, @Valid @RequestBody Contractor contractor) {
        contractor.setId(id);
        return contractorService.save(contractor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contractorService.deleteContractor(id);
        return ResponseEntity.noContent().build();
    }
}
