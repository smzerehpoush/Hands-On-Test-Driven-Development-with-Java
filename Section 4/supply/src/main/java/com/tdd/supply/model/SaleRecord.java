package com.tdd.supply.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleRecord {
    private Integer productId;
    private Integer quantity;

    public SaleRecord(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
