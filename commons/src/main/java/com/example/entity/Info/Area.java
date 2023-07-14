package com.example.entity.Info;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

public class Area {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String houseName;
    private Integer houseType;
    private Integer areaType;
    private Integer shelveNum;
    @TableField(exist = false)
    private Float currCapacity;

    public Area() {
    }
    public Area(String name, Integer houseType, Integer type) {
        this.houseName = name;
        this.houseType = houseType;
        this.areaType = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public Integer getHouseType() {
        return houseType;
    }

    public void setHouseType(Integer houseType) {
        this.houseType = houseType;
    }

    public Integer getAreaType() {
        return areaType;
    }

    public void setAreaType(Integer areaType) {
        this.areaType = areaType;
    }

    public Integer getShelveNum() {
        return shelveNum;
    }

    public void setShelveNum(Integer shelveNum) {
        this.shelveNum = shelveNum;
    }

    public Float getCurrCapacity() {
        return currCapacity;
    }

    public void setCurrCapacity(Float currCapacity) {
        this.currCapacity = currCapacity;
    }
}

