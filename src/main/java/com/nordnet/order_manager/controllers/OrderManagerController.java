package com.nordnet.order_manager.controllers;

import com.nordnet.order_manager.models.ApiResponse;
import com.nordnet.order_manager.models.dto.OrderDto;
import com.nordnet.order_manager.models.dto.OrderSummaryDto;
import com.nordnet.order_manager.services.OrderManagerService;
import com.nordnet.order_manager.utils.ApiResponseUtil;
import com.nordnet.order_manager.utils.OrderMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@Slf4j
@Validated
public class OrderManagerController {

    private final OrderManagerService orderManagerService;

    @GetMapping(value = "/{orderId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse<OrderDto>> getOrder(@PathVariable("orderId") long orderId) {
        log.info("Received request to GET order with id {}", orderId);

        var response = orderManagerService.getOrder(orderId);

        return response.isEmpty()
                ? ApiResponseUtil.error("Order not found.", HttpStatus.NOT_FOUND)
                : ApiResponseUtil.success("Order found.", OrderMapper.entityToDto(response.get()));
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@Valid @RequestBody OrderDto orderDto) {
        log.info("Received request to create new order.");
        var orderEntity = OrderMapper.dtoToEntity(orderDto);

        var response = orderManagerService.createOrder(orderEntity);

        return response.getId() == 0
                ? ApiResponseUtil.error("Failed to create order.", HttpStatus.INTERNAL_SERVER_ERROR)
                : ApiResponseUtil.success("Created new order.", OrderMapper.entityToDto(response), HttpStatus.CREATED);
    }

    @GetMapping(value ="/summary", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse<OrderSummaryDto>> getOrderSummary(
            @RequestParam String ticker,
            @RequestParam Instant startDate,
            @RequestParam Instant endDate) {
        log.info("Received request to GET order summary for ticker {}", ticker);

        var response = orderManagerService.getOrderSummary(ticker, startDate, endDate);

        return response == null
                ? ApiResponseUtil.error("No order data found.", HttpStatus.NOT_FOUND)
                : ApiResponseUtil.success("Order summary calculated.", response);
    }
}
