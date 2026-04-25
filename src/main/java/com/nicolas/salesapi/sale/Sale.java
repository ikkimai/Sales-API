package com.nicolas.salesapi.sale;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate saleDate;

    @Column(name = "sale_value", nullable = false, precision = 19, scale = 2)
    private BigDecimal value;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private String sellerName;

    protected Sale() {
    }

    public Sale(LocalDate saleDate, BigDecimal value, Long sellerId, String sellerName) {
        this.saleDate = saleDate;
        this.value = value;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }
}
