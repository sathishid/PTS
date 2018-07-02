package com.ara.approvalshipment.models;

import com.google.gson.annotations.SerializedName;

public class OrderItem {
    @SerializedName("oa_goods_id")
    private int gradeId;
    @SerializedName("oa_goods_code")
    private String gradeCode;
    @SerializedName("oa_goods_name")
    private String gradeName;
    private double soldQty;

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public double getSoldQty() {
        return soldQty;
    }

    public void setSoldQty(double soldQty) {
        this.soldQty = soldQty;
    }
}
