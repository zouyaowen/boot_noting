package com.trip.noting.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MixedSalesEnum {
    UnSet(0), MixedSales(1), NotMixedSales(2);
    private final Integer value;

    public static MixedSalesEnum getMixedSales(int v) {
        if (v == 1) {
            return MixedSales;
        } else if (v == 2) {
            return NotMixedSales;
        }
        return UnSet;
    }
}