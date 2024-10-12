package com.nordnet.order_manager.utils;

import com.nordnet.order_manager.models.dto.OrderDto;
import com.nordnet.order_manager.models.entities.OrderEntity;

public class OrderMapper {

    public static OrderDto entityToDto(OrderEntity orderEntity) {
        return new OrderDto()
                .setId(orderEntity.getId())
                .setSide(orderEntity.getSide())
                .setPrice(orderEntity.getPrice())
                .setCurrency(orderEntity.getCurrency())
                .setVolume(orderEntity.getVolume())
                .setTicker(orderEntity.getTicker())
                .setCreatedAt(orderEntity.getCreatedAt());
    }

    public static OrderEntity dtoToEntity(OrderDto orderDto) {
        return new OrderEntity()
                .setSide(orderDto.getSide())
                .setPrice(orderDto.getPrice())
                .setCurrency(orderDto.getCurrency())
                .setVolume(orderDto.getVolume())
                .setTicker(orderDto.getTicker());
    }
}
