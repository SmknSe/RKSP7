package com.example.pr7;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
class Order {
    //Семейкин Сергей ИКБО-16-21 (SmknSe)
    @Id
    private Long id;
    private String customerName;
    private String customerEmail;
    private String deliveryAddress;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime orderDate;
    private LocalDateTime lastUpdated;
}
