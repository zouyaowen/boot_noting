package com.trip.noting.graph;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class UserModel {
    private String name;
    private Integer age;

    private LocalDateTime createTime;
    /**
     * 是否支持混排 1 支持混排 2 不支持混排
     */
    private Integer supportMixedSales;

    @JsonIgnore
    public MixedSalesEnum getSupportMixedSalesEnum() {
        return MixedSalesEnum.getMixedSales(this.supportMixedSales);
    }

    public void setSupportMixedSalesEnum(MixedSalesEnum supportMixedSales) {
        this.supportMixedSales = supportMixedSales.getValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSupportMixedSales() {
        return supportMixedSales;
    }

    public void setSupportMixedSales(Integer supportMixedSales) {
        this.supportMixedSales = supportMixedSales;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
