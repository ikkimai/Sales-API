package com.nicolas.salesapi.sale;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SaleResponse(
        Long id,
        LocalDate saleDate,
        BigDecimal value,
        Long sellerId,
        String sellerName
) {

    public static SaleResponse from(Sale sale) {
        return new SaleResponse(
                sale.getId(),
                sale.getSaleDate(),
                sale.getValue(),
                sale.getSellerId(),
                sale.getSellerName()
        );
    }
}
