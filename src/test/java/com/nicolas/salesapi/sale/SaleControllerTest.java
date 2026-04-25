package com.nicolas.salesapi.sale;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SaleRepository saleRepository;

    @BeforeEach
    void setUp() {
        saleRepository.deleteAll();
    }

    @Test
    void createsSale() throws Exception {
        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "saleDate": "2026-04-22",
                                  "value": 125.50,
                                  "sellerId": 10,
                                  "sellerName": "Ana Souza"
                                }
                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/sales/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.saleDate").value("2026-04-22"))
                .andExpect(jsonPath("$.value").value(125.50))
                .andExpect(jsonPath("$.sellerId").value(10))
                .andExpect(jsonPath("$.sellerName").value("Ana Souza"));
    }

    @Test
    void rejectsInvalidSale() throws Exception {
        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "saleDate": "2026-04-22",
                                  "value": 0,
                                  "sellerId": 10,
                                  "sellerName": "Ana Souza"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Sale value must be greater than zero."));
    }

    @Test
    void summarizesSellerSalesInsidePeriod() throws Exception {
        createSale("2026-04-20", "100.00", 1, "Ana Souza");
        createSale("2026-04-21", "200.00", 1, "Ana Souza");
        createSale("2026-04-22", "300.00", 2, "Bruno Lima");
        createSale("2026-04-25", "400.00", 1, "Ana Souza");

        mockMvc.perform(get("/sellers")
                        .param("startDate", "2026-04-20")
                        .param("endDate", "2026-04-22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].sellerName").value("Ana Souza"))
                .andExpect(jsonPath("$[0].totalSales").value(2))
                .andExpect(jsonPath("$[0].dailySalesAverage").value(0.67))
                .andExpect(jsonPath("$[1].sellerName").value("Bruno Lima"))
                .andExpect(jsonPath("$[1].totalSales").value(1))
                .andExpect(jsonPath("$[1].dailySalesAverage").value(0.33));
    }

    @Test
    void rejectsInvalidPeriod() throws Exception {
        mockMvc.perform(get("/sellers")
                        .param("startDate", "2026-04-23")
                        .param("endDate", "2026-04-22"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("endDate must be equal to or after startDate."));
    }

    private void createSale(String saleDate, String value, long sellerId, String sellerName) throws Exception {
        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "saleDate": "%s",
                                  "value": %s,
                                  "sellerId": %d,
                                  "sellerName": "%s"
                                }
                                """.formatted(saleDate, value, sellerId, sellerName)))
                .andExpect(status().isCreated());
    }
}
