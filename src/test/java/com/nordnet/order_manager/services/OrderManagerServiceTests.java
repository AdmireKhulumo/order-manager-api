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
import java.util.List;
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
        // Arrange
        long orderId = 1;
        OrderEntity order = new OrderEntity();
        order.setId(orderId);

        when(orderRepository.getOrderById(orderId)).thenReturn(Optional.of(order));

        // Act
        Optional<OrderEntity> result = orderManagerService.getOrder(orderId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(orderId, result.get().getId());
        verify(orderRepository, times(1)).getOrderById(orderId);
    }

    @Test
    void Should_CreateOrder_When_GivenOrderEntity() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setPrice(BigDecimal.valueOf(100));

        when(orderRepository.save(order)).thenReturn(order);

        // Act
        OrderEntity result = orderManagerService.createOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals(order.getPrice(), result.getPrice());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void Should_GetCorrectBuyOrderSummary_When_GivenTickerAndDateRange() {
        // Arrange
        String ticker = "SAVE";
        Instant startDate = Instant.now().minusSeconds(86400);
        Instant endDate = Instant.now();

        OrderEntity order1 = new OrderEntity()
                .setPrice(BigDecimal.valueOf(100))
                .setSide(Side.BUY);

        OrderEntity order2 = new OrderEntity()
                .setPrice(BigDecimal.valueOf(150))
                .setSide(Side.BUY);

        OrderEntity order3 = new OrderEntity()
                .setPrice(BigDecimal.valueOf(200))
                .setSide(Side.SELL);

        OrderEntity order4 = new OrderEntity()
                .setPrice(BigDecimal.valueOf(250))
                .setSide(Side.SELL);

        when(orderRepository.findOrdersByTickerAndDateRange(ticker, startDate, endDate))
                .thenReturn(Arrays.asList(order1, order2, order3, order4));

        // Act
        OrderSummaryDto summary = orderManagerService.getOrderSummary(ticker, startDate, endDate);

        // Assert
        assertNotNull(summary);
        assertEquals(ticker, summary.getTicker());
        assertEquals(4, summary.getTotalOrders());
        assertEquals(2, summary.getSideSummaries().size());

        SideSummaryDto buySummary = summary.getSideSummaries().get(Side.BUY);
        assertNotNull(buySummary);
        assertEquals(BigDecimal.valueOf(125), buySummary.getAveragePrice());
        assertEquals(BigDecimal.valueOf(150), buySummary.getMaxPrice());
        assertEquals(BigDecimal.valueOf(100), buySummary.getMinPrice());

        SideSummaryDto sellSummary = summary.getSideSummaries().get(Side.SELL);
        assertNotNull(sellSummary);
        assertEquals(BigDecimal.valueOf(225), sellSummary.getAveragePrice());
        assertEquals(BigDecimal.valueOf(250), sellSummary.getMaxPrice());
        assertEquals(BigDecimal.valueOf(200), sellSummary.getMinPrice());

        verify(orderRepository, times(1)).findOrdersByTickerAndDateRange(ticker, startDate, endDate);
    }

    @Test
    void Should_ReturnNullSummary_When_NoOrdersForGivenTickerAndDateRange() {
        // Arrange
        String ticker = "SAVE";
        Instant startDate = Instant.now().minusSeconds(3600);
        Instant endDate = Instant.now();

        when(orderRepository.findOrdersByTickerAndDateRange(ticker, startDate, endDate)).thenReturn(List.of());

        // Act
        OrderSummaryDto summary = orderManagerService.getOrderSummary(ticker, startDate, endDate);

        // Assert
        assertNull(summary);
        verify(orderRepository, times(1)).findOrdersByTickerAndDateRange(ticker, startDate, endDate);
    }
}
