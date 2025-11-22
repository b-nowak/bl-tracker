package com.example.bltracker.controller;

import com.example.bltracker.model.Contractor;
import com.example.bltracker.model.TimeEntry;
import com.example.bltracker.repository.ContractorRepository;
import com.example.bltracker.repository.TimeEntryRepository;
import com.example.bltracker.service.ContractorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/time-entries")
@CrossOrigin
public class TimeEntryController {
    private final TimeEntryRepository timeEntryRepository;
    private final ContractorRepository contractorRepository;
    private final ContractorService contractorService;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, ContractorRepository contractorRepository, ContractorService contractorService) {
        this.timeEntryRepository = timeEntryRepository;
        this.contractorRepository = contractorRepository;
        this.contractorService = contractorService;
    }

    @GetMapping("/contractor/{contractorId}")
    public List<TimeEntry> list(@PathVariable Long contractorId) {
        Contractor contractor = contractorRepository.findById(contractorId).orElseThrow();
        return contractorService.findTimeEntries(contractor);
    }

    @PostMapping
    public TimeEntry create(@RequestBody TimeEntry entry) {
        return timeEntryRepository.save(entry);
    }

    @PutMapping("/{id}")
    public TimeEntry update(@PathVariable Long id, @RequestBody TimeEntry entry) {
        entry.setId(id);
        return timeEntryRepository.save(entry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeEntryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk-csv")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) throws IOException {
        List<TimeEntry> created = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length < 4) continue;
                String email = parts[0].trim();
                Contractor contractor = contractorRepository.findByEmail(email);
                if (contractor == null) continue;
                LocalDate date = LocalDate.parse(parts[1].trim());
                double hours = Double.parseDouble(parts[2].trim());
                String description = parts[3].trim();
                TimeEntry entry = new TimeEntry();
                entry.setContractor(contractor);
                entry.setDate(date);
                entry.setHours(hours);
                entry.setDescription(description);
                created.add(entry);
            }
        }
        timeEntryRepository.saveAll(created);
        return ResponseEntity.ok("Imported " + created.size() + " entries");
    }
}
