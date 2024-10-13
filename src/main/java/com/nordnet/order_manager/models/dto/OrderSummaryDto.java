package com.nordnet.order_manager.models.dto;

import com.nordnet.order_manager.models.enums.Side;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class OrderSummaryDto {
    private int totalOrders;
    private String ticker;
    private Map<Side, SideSummaryDto> sideSummaries;
}

