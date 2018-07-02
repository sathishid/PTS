package com.ara.approvalshipment.models;

import com.google.gson.annotations.SerializedName;

public class Grade {
    @SerializedName("oa_goods_id")
    private int gradeId;
    @SerializedName("oa_goods_code")
    private String gradeCode;
    @SerializedName("oa_goods_name")
    private String gradeName;
    @SerializedName("available_qty")
    private double quantity;
    private double soldQuantity;

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public double getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(double soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return gradeName;
    }
}
