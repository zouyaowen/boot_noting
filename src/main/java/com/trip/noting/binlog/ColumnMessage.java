package com.trip.noting.binlog;

import lombok.Data;

@Data
public class ColumnMessage {
    private String name;
    private String value;
    private boolean isUpdated;
    private boolean isKey;
}
