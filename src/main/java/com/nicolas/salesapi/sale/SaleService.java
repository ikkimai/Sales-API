package com.nicolas.salesapi.sale;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nicolas.salesapi.support.InvalidRequestException;

@Service
public class SaleService {

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public SaleResponse create(SaleRequest request) {
        validateSale(request);

        Sale sale = new Sale(
                request.saleDate(),
                request.value(),
                request.sellerId(),
                request.sellerName().trim()
        );

        return SaleResponse.from(saleRepository.save(sale));
    }

    public List<SellerSalesSummary> summarizeBySeller(LocalDate startDate, LocalDate endDate) {
        validatePeriod(startDate, endDate);

        long daysInPeriod = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        Map<Long, List<Sale>> salesBySeller = saleRepository.findBySaleDateBetween(startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(Sale::getSellerId));

        return salesBySeller.values()
                .stream()
                .map(sales -> toSummary(sales, daysInPeriod))
                .sorted(Comparator.comparing(SellerSalesSummary::sellerName))
                .toList();
    }

    private SellerSalesSummary toSummary(List<Sale> sales, long daysInPeriod) {
        Sale firstSale = sales.get(0);
        BigDecimal average = BigDecimal.valueOf(sales.size())
                .divide(BigDecimal.valueOf(daysInPeriod), 2, RoundingMode.HALF_UP);

        return new SellerSalesSummary(firstSale.getSellerName(), sales.size(), average);
    }

    private void validateSale(SaleRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Sale request is required.");
        }
        if (request.saleDate() == null) {
            throw new InvalidRequestException("Sale date is required.");
        }
        if (request.value() == null || request.value().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Sale value must be greater than zero.");
        }
        if (request.sellerId() == null || request.sellerId() <= 0) {
            throw new InvalidRequestException("Seller id must be greater than zero.");
        }
        if (request.sellerName() == null || request.sellerName().isBlank()) {
            throw new InvalidRequestException("Seller name is required.");
        }
    }

    private void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidRequestException("startDate and endDate are required.");
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidRequestException("endDate must be equal to or after startDate.");
        }
    }
}
