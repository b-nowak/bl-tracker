package com.example.bltracker.service;

import com.example.bltracker.dto.ContractorSummary;
import com.example.bltracker.model.Contractor;
import com.example.bltracker.model.Invoice;
import com.example.bltracker.model.TimeEntry;
import com.example.bltracker.repository.ContractorRepository;
import com.example.bltracker.repository.InvoiceRepository;
import com.example.bltracker.repository.TimeEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContractorService {
    private final ContractorRepository contractorRepository;
    private final TimeEntryRepository timeEntryRepository;
    private final InvoiceRepository invoiceRepository;

    public ContractorService(ContractorRepository contractorRepository, TimeEntryRepository timeEntryRepository, InvoiceRepository invoiceRepository) {
        this.contractorRepository = contractorRepository;
        this.timeEntryRepository = timeEntryRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public List<ContractorSummary> listSummaries(String term) {
        List<Contractor> contractors = (term == null || term.isBlank()) ?
                contractorRepository.findAll() :
                contractorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrVendorCompanyContainingIgnoreCase(term, term, term);
        return contractors.stream().map(this::mapToSummary).collect(Collectors.toList());
    }

    public ContractorSummary getSummary(Long id) {
        Contractor contractor = contractorRepository.findById(id).orElseThrow();
        return mapToSummary(contractor);
    }

    public Contractor save(Contractor contractor) {
        return contractorRepository.save(contractor);
    }

    public void deleteContractor(Long id) {
        contractorRepository.deleteById(id);
    }

    public List<TimeEntry> findTimeEntries(Contractor contractor) {
        return timeEntryRepository.findByContractor(contractor);
    }

    public List<Invoice> findInvoices(Contractor contractor) {
        return invoiceRepository.findByContractor(contractor);
    }

    public ContractorSummary mapToSummary(Contractor contractor) {
        List<TimeEntry> timeEntries = timeEntryRepository.findByContractor(contractor);
        List<Invoice> invoices = invoiceRepository.findByContractor(contractor);

        double usedHours = timeEntries.stream().mapToDouble(TimeEntry::getHours).sum();
        double remainingHours = contractor.getContractHoursTotal() - usedHours;
        double usedBudget = invoices.stream().mapToDouble(Invoice::getAmount).sum();
        double remainingBudget = contractor.getContractHoursTotal() * contractor.getHourlyRate() - usedBudget;

        ContractorSummary summary = new ContractorSummary();
        summary.setId(contractor.getId());
        summary.setFirstName(contractor.getFirstName());
        summary.setLastName(contractor.getLastName());
        summary.setVendorCompany(contractor.getVendorCompany());
        summary.setEmail(contractor.getEmail());
        summary.setHourlyRate(contractor.getHourlyRate());
        summary.setRateType(contractor.getRateType());
        summary.setContractHoursTotal(contractor.getContractHoursTotal());
        summary.setContractStartDate(contractor.getContractStartDate());
        summary.setContractEndDate(contractor.getContractEndDate());
        summary.setNotes(contractor.getNotes());
        summary.setUsedHours(usedHours);
        summary.setRemainingHours(remainingHours);
        summary.setRemainingBudget(remainingBudget);
        summary.setBurnOutDate(calculateBurnOutDate(contractor, timeEntries, remainingHours));
        return summary;
    }

    public LocalDate calculateBurnOutDate(Contractor contractor, List<TimeEntry> entries, double remainingHours) {
        if (entries.isEmpty() || remainingHours <= 0) {
            return null;
        }

        LocalDate fourWeeksAgo = LocalDate.now().minusWeeks(4);
        List<TimeEntry> lastMonth = entries.stream()
                .filter(e -> !e.getDate().isBefore(fourWeeksAgo))
                .toList();

        double avgWeeklyHours = calculateAverageWeeklyHours(lastMonth);
        if (avgWeeklyHours == 0) {
            avgWeeklyHours = calculateAverageWeeklyHours(entries);
        }

        if (avgWeeklyHours == 0) {
            return null;
        }

        double weeksLeft = remainingHours / avgWeeklyHours;
        return LocalDate.now().plusDays(Math.round(weeksLeft * 7));
    }

    private double calculateAverageWeeklyHours(List<TimeEntry> entries) {
        if (entries.isEmpty()) {
            return 0;
        }
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return entries.stream()
                .collect(Collectors.groupingBy(e -> e.getDate().get(weekFields.weekOfWeekBasedYear()), Collectors.summingDouble(TimeEntry::getHours)))
                .values().stream()
                .mapToDouble(Double::doubleValue)
                .average().orElse(0);
    }
}
