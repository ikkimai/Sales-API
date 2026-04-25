package com.nicolas.salesapi.sale;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SaleRequest(
        LocalDate saleDate,
        BigDecimal value,
        Long sellerId,
        String sellerName
) {
}
