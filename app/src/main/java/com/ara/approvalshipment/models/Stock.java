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
    private int openingQty;

    @SerializedName("purchase_qty")
    private int purchaseQty;

    @SerializedName("clotted_qty")
    private double clottedQty;

    @SerializedName("damaged_qty")
    private double damagedQty;

    @SerializedName("sales_qty")
    private double soldQty;

    public int getPurchaseQty() {
        return purchaseQty;
    }

    public void setPurchaseQty(int purchaseQty) {
        this.purchaseQty = purchaseQty;
    }

    public double getSoldQty() {
        return soldQty;
    }

    public void setSoldQty(double soldQty) {
        this.soldQty = soldQty;
    }

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

    public int getOpeningQty() {
        return openingQty;
    }

    public void setOpeningQty(int openingQty) {
        this.openingQty = openingQty;
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
