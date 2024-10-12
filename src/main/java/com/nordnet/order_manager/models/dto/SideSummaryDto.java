package com.nordnet.order_manager.models.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class SideSummaryDto {
    private BigDecimal averagePrice;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
}
