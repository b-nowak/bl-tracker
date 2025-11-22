package com.example.bltracker.controller;

import com.example.bltracker.model.Contractor;
import com.example.bltracker.model.Invoice;
import com.example.bltracker.repository.ContractorRepository;
import com.example.bltracker.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin
public class InvoiceController {
    private final InvoiceRepository invoiceRepository;
    private final ContractorRepository contractorRepository;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    public InvoiceController(InvoiceRepository invoiceRepository, ContractorRepository contractorRepository) {
        this.invoiceRepository = invoiceRepository;
        this.contractorRepository = contractorRepository;
    }

    @GetMapping("/contractor/{contractorId}")
    public List<Invoice> list(@PathVariable Long contractorId) {
        Contractor contractor = contractorRepository.findById(contractorId).orElseThrow();
        return invoiceRepository.findByContractor(contractor);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Invoice create(@RequestParam Long contractorId,
                          @RequestParam String invoiceNumber,
                          @RequestParam String invoiceDate,
                          @RequestParam double amount,
                          @RequestParam(required = false) MultipartFile pdf) throws IOException {
        Contractor contractor = contractorRepository.findById(contractorId).orElseThrow();
        Invoice invoice = new Invoice();
        invoice.setContractor(contractor);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setInvoiceDate(LocalDate.parse(invoiceDate));
        invoice.setAmount(amount);

        if (pdf != null && !pdf.isEmpty()) {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            Path filePath = dir.resolve(pdf.getOriginalFilename());
            pdf.transferTo(filePath);
            invoice.setPdfPath(filePath.toString());
        }
        return invoiceRepository.save(invoice);
    }

    @PutMapping("/{id}")
    public Invoice update(@PathVariable Long id, @RequestBody Invoice invoice) {
        invoice.setId(id);
        return invoiceRepository.save(invoice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws IOException {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        if (invoice.getPdfPath() != null) {
            Files.deleteIfExists(Path.of(invoice.getPdfPath()));
        }
        invoiceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<FileSystemResource> download(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        if (invoice.getPdfPath() == null) {
            return ResponseEntity.notFound().build();
        }
        FileSystemResource resource = new FileSystemResource(new File(invoice.getPdfPath()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + resource.getFilename())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
