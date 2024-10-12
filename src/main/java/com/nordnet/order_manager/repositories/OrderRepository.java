package com.nordnet.order_manager.repositories;

import com.nordnet.order_manager.models.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity>{

    Optional<OrderEntity> getOrderById(long id);

    @Query("SELECT o FROM OrderEntity o " +
            "WHERE o.ticker = :ticker " +
            "AND o.createdAt BETWEEN :startDate AND :endDate")
    List<OrderEntity> findOrdersByTickerAndDateRange(
            @Param("ticker") String ticker,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );

}
