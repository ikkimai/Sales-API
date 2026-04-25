package com.nicolas.salesapi.sale;

import java.math.BigDecimal;

public record SellerSalesSummary(
        String sellerName,
        long totalSales,
        BigDecimal dailySalesAverage
) {
}
