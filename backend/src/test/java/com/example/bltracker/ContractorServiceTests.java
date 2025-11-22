package com.example.bltracker;

import com.example.bltracker.dto.ContractorSummary;
import com.example.bltracker.model.Contractor;
import com.example.bltracker.model.Invoice;
import com.example.bltracker.model.TimeEntry;
import com.example.bltracker.repository.ContractorRepository;
import com.example.bltracker.repository.InvoiceRepository;
import com.example.bltracker.repository.TimeEntryRepository;
import com.example.bltracker.service.ContractorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ContractorService.class)
class ContractorServiceTests {

    @Autowired
    private ContractorRepository contractorRepository;
    @Autowired
    private TimeEntryRepository timeEntryRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ContractorService contractorService;

    private Contractor contractor;

    @BeforeEach
    void setup() {
        contractor = new Contractor();
        contractor.setFirstName("Jane");
        contractor.setLastName("Doe");
        contractor.setEmail("jane@example.com");
        contractor.setVendorCompany("Acme");
        contractor.setHourlyRate(100);
        contractor.setRateType("net");
        contractor.setContractHoursTotal(160);
        contractor.setContractStartDate(LocalDate.now().minusMonths(1));
        contractorRepository.save(contractor);
    }

    @Test
    void calculatesRemainingHours() {
        TimeEntry entry = new TimeEntry();
        entry.setContractor(contractor);
        entry.setDate(LocalDate.now());
        entry.setHours(40);
        timeEntryRepository.save(entry);

        ContractorSummary summary = contractorService.getSummary(contractor.getId());
        assertThat(summary.getRemainingHours()).isEqualTo(120);
    }

    @Test
    void calculatesRemainingBudgetFromInvoices() {
        Invoice invoice = new Invoice();
        invoice.setContractor(contractor);
        invoice.setAmount(1000);
        invoice.setInvoiceDate(LocalDate.now());
        invoiceRepository.save(invoice);

        ContractorSummary summary = contractorService.getSummary(contractor.getId());
        assertThat(summary.getRemainingBudget()).isEqualTo(15000);
    }

    @Test
    void calculatesBurnOutDate() {
        for (int i = 0; i < 4; i++) {
            TimeEntry entry = new TimeEntry();
            entry.setContractor(contractor);
            entry.setDate(LocalDate.now().minusWeeks(i));
            entry.setHours(20);
            timeEntryRepository.save(entry);
        }
        ContractorSummary summary = contractorService.getSummary(contractor.getId());
        assertThat(summary.getBurnOutDate()).isNotNull();
    }
}
