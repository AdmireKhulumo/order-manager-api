package com.nordnet.order_manager.models.entities;

import com.nordnet.order_manager.models.enums.Side;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Table(name = "orders")
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private Side side;

    private String ticker;

    private BigDecimal volume;

    private String currency;

    private BigDecimal price;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}

