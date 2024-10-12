package com.nordnet.order_manager.utils;

import com.nordnet.order_manager.models.dto.OrderDto;
import com.nordnet.order_manager.models.entities.OrderEntity;
import com.nordnet.order_manager.models.enums.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderMapperTests {

    private OrderDto orderDto;
    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        // Create a sample OrderDto
        orderDto = new OrderDto()
                .setId(1L)
                .setSide(Side.BUY)
                .setPrice(BigDecimal.valueOf(100))
                .setCurrency("BWP")
                .setVolume(BigDecimal.valueOf(10000))
                .setTicker("SAVE")
                .setCreatedAt(Instant.now());

        // Create a sample OrderEntity
        orderEntity = new OrderEntity()
                .setId(1L)
                .setSide(Side.BUY)
                .setPrice(BigDecimal.valueOf(100))
                .setCurrency("BWP")
                .setVolume(BigDecimal.valueOf(10))
                .setTicker("SAVE")
                .setCreatedAt(Instant.now());
    }

    @Test
    void Should_MapEntityToDto_Correctly() {
        // Act
        OrderDto mappedDto = OrderMapper.entityToDto(orderEntity);

        // Assert
        assertEquals(orderEntity.getId(), mappedDto.getId());
        assertEquals(orderEntity.getSide(), mappedDto.getSide());
        assertEquals(orderEntity.getPrice(), mappedDto.getPrice());
        assertEquals(orderEntity.getCurrency(), mappedDto.getCurrency());
        assertEquals(orderEntity.getVolume(), mappedDto.getVolume());
        assertEquals(orderEntity.getTicker(), mappedDto.getTicker());
        assertEquals(orderEntity.getCreatedAt(), mappedDto.getCreatedAt());
    }

    @Test
    void Should_MapDtoToEntity_Correctly() {
        // Act
        OrderEntity mappedEntity = OrderMapper.dtoToEntity(orderDto);

        // Assert
        assertEquals(orderDto.getSide(), mappedEntity.getSide());
        assertEquals(orderDto.getPrice(), mappedEntity.getPrice());
        assertEquals(orderDto.getCurrency(), mappedEntity.getCurrency());
        assertEquals(orderDto.getVolume(), mappedEntity.getVolume());
        assertEquals(orderDto.getTicker(), mappedEntity.getTicker());
    }
}
