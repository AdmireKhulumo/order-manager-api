package com.nordnet.order_manager.services;

import com.nordnet.order_manager.models.dto.OrderSummaryDto;
import com.nordnet.order_manager.models.dto.SideSummaryDto;
import com.nordnet.order_manager.models.entities.OrderEntity;
import com.nordnet.order_manager.models.enums.Side;
import com.nordnet.order_manager.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderManagerServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderManagerService orderManagerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Should_GetOrder_When_GivenOrderId() {
        long orderId = 1L;
        OrderEntity order = new OrderEntity();
        order.setId(orderId);

        when(orderRepository.getOrderById(orderId)).thenReturn(Optional.of(order));

        Optional<OrderEntity> result = orderManagerService.getOrder(orderId);

        assertTrue(result.isPresent());
        assertEquals(orderId, result.get().getId());
        verify(orderRepository, times(1)).getOrderById(orderId);
    }

    @Test
    void Should_CreateOrder_When_GivenOrderEntity() {
        OrderEntity order = new OrderEntity();
        order.setPrice(BigDecimal.valueOf(100));

        when(orderRepository.save(order)).thenReturn(order);

        OrderEntity result = orderManagerService.createOrder(order);

        assertNotNull(result);
        assertEquals(order.getPrice(), result.getPrice());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void Should_GetOrderSummary_When_GivenTickerAndDateRange() {
        String ticker = "AAPL";
        Instant startDate = Instant.now().minusSeconds(86400);
        Instant endDate = Instant.now();

        OrderEntity order1 = new OrderEntity();
        order1.setPrice(BigDecimal.valueOf(100));
        order1.setSide(Side.BUY);

        OrderEntity order2 = new OrderEntity();
        order2.setPrice(BigDecimal.valueOf(150));
        order2.setSide(Side.BUY);

        when(orderRepository.findOrdersByTickerAndDateRange(ticker, startDate, endDate))
                .thenReturn(Arrays.asList(order1, order2));

        OrderSummaryDto summary = orderManagerService.getOrderSummary(ticker, startDate, endDate);

        assertNotNull(summary);
        assertEquals(2, summary.getTotalOrders());
        assertEquals(1, summary.getSideSummaries().size());

        SideSummaryDto buySummary = summary.getSideSummaries().get(Side.BUY);
        assertNotNull(buySummary);
        assertEquals(BigDecimal.valueOf(125), buySummary.getAveragePrice()); // (100 + 150) / 2
        assertEquals(BigDecimal.valueOf(150), buySummary.getMaxPrice());
        assertEquals(BigDecimal.valueOf(100), buySummary.getMinPrice());

        verify(orderRepository, times(1)).findOrdersByTickerAndDateRange(ticker, startDate, endDate);
    }

    @Test
    void Should_ReturnNull_When_NoOrdersForGivenTickerAndDateRange() {
        String ticker = "AAPL";
        Instant startDate = Instant.now().minusSeconds(86400);
        Instant endDate = Instant.now();

        when(orderRepository.findOrdersByTickerAndDateRange(ticker, startDate, endDate)).thenReturn(Arrays.asList());

        OrderSummaryDto summary = orderManagerService.getOrderSummary(ticker, startDate, endDate);

        assertNull(summary);
        verify(orderRepository, times(1)).findOrdersByTickerAndDateRange(ticker, startDate, endDate);
    }
}
