package com.nordnet.order_manager.models.dto;

import com.nordnet.order_manager.models.enums.Side;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Accessors(chain = true)
public class OrderDto {

    private long id = 0;

    private Instant createdAt = Instant.now();

    @NotNull(message = "Ticker cannot be null.")
    @Size(min = 1, message = "Ticker must be at least 2 characters long.")
    private String ticker;

    @NotNull(message = "Side cannot be null.")
    private Side side;

    @NotNull(message = "Volume cannot be null.")
    private BigDecimal volume;

    @NotEmpty(message = "Currency cannot be empty.")
    private String currency;

    @NotNull(message = "Price cannot be null")
    private BigDecimal price;
}
