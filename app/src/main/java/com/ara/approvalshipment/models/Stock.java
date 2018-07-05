package com.ara.approvalshipment.models;

import com.google.gson.annotations.SerializedName;

public class Stock {
    @SerializedName("grade_id")
    private int gradeId;
    @SerializedName("grade_name")
    private String gradeName;
    @SerializedName("grade_code")
    private String gradeCode;
    @SerializedName("open_qty")
    private int stockQuantity;

    private double clottedQty;
    private double damagedQty;

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public double getClottedQty() {
        return clottedQty;
    }

    public void setClottedQty(double clottedQty) {
        this.clottedQty = clottedQty;
    }

    public double getDamagedQty() {
        return damagedQty;
    }

    public void setDamagedQty(double damagedQty) {
        this.damagedQty = damagedQty;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
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
}
