package com.example.entity.Info;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

public class Distance {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer relatedType;
    private Long relatedId;
    private Float distance;

    @TableField(exist = false)
    private Supplier s;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(Integer relatedType) {
        this.relatedType = relatedType;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }


    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Supplier getS() {
        return s;
    }

    public void setS(Supplier s) {
        this.s = s;
    }
}

