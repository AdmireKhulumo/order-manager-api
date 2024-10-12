package com.nordnet.order_manager.services;

import com.nordnet.order_manager.models.dto.OrderSummaryDto;
import com.nordnet.order_manager.models.dto.SideSummaryDto;
import com.nordnet.order_manager.models.entities.OrderEntity;
import com.nordnet.order_manager.models.enums.Side;
import com.nordnet.order_manager.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderManagerService {
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Optional<OrderEntity> getOrder(long orderId) {
        return orderRepository.getOrderById(orderId);
    }

    @Transactional
    public OrderEntity createOrder(OrderEntity orderEntity) {
        log.info("Creating new order...");
        return orderRepository.save(orderEntity);
    }

    @Transactional
    public OrderSummaryDto getOrderSummary(String ticker, Instant startDate, Instant endDate) {
        List<OrderEntity> orders = orderRepository.findOrdersByTickerAndDateRange(ticker, startDate, endDate);

        if (orders.isEmpty()) {
            log.info("No orders found for ticker {} in provided range.", ticker);
            return null;
        }

        Map<Side, List<OrderEntity>> ordersBySide = orders.stream()
                .collect(Collectors.groupingBy(OrderEntity::getSide));

        Map<Side, SideSummaryDto> sideSummaries = ordersBySide.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateSideSummary(entry.getValue())
                ));

        OrderSummaryDto summaryDto = new OrderSummaryDto();
        summaryDto.setTotalOrders(orders.size());
        summaryDto.setSideSummaries(sideSummaries);

        return summaryDto;
    }

    private SideSummaryDto calculateSideSummary(List<OrderEntity> orders) {
        BigDecimal averagePrice = orders.stream()
                .map(OrderEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(orders.size()), RoundingMode.HALF_UP);

        BigDecimal maxPrice = orders.stream()
                .map(OrderEntity::getPrice)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        BigDecimal minPrice = orders.stream()
                .map(OrderEntity::getPrice)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        return new SideSummaryDto()
                .setAveragePrice(averagePrice)
                .setMaxPrice(maxPrice)
                .setMinPrice(minPrice);
    }
}
