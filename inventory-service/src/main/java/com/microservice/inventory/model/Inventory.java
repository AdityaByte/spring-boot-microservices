package com.microservice.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_inventory")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private Integer quantity;
}
