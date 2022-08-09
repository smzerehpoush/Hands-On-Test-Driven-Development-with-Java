package com.tdd.supply.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplyRecord {
    private Integer productId;
    private String productName;
    private String productCategory;
    private Integer quantity;


    public SupplyRecord(Integer productId, String productName, String productCategory, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.quantity = quantity;
    }
}
