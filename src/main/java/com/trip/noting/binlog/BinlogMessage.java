package com.trip.noting.binlog;

import lombok.Data;

import java.util.List;

@Data
public class BinlogMessage {
    private String tableName;
    private String eventType;
    private List<ColumnMessage> afterColumnList;
    private List<ColumnMessage> beforeColumnList;
}
