package com.nicolas.salesapi.sale;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/sales")
    public ResponseEntity<SaleResponse> create(@RequestBody SaleRequest request) {
        SaleResponse response = saleService.create(request);
        return ResponseEntity.created(URI.create("/sales/" + response.id())).body(response);
    }

    @GetMapping("/sellers")
    public List<SellerSalesSummary> summarizeBySeller(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return saleService.summarizeBySeller(startDate, endDate);
    }
}
