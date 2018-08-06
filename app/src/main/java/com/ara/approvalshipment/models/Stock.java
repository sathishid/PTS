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
    private double openingQty;

    @SerializedName("purchase_qty")
    private double purchaseQty;

    @SerializedName("clotted_qty")
    private double clottedQty;

    @SerializedName("damaged_qty")
    private double damagedQty;

    @SerializedName("sales_qty")
    private double soldQty;

    @SerializedName("closing_qty")
    private double closingQty;

    public double getClosingQty() {
        return closingQty;
    }

    public void setClosingQty(double closingQty) {
        this.closingQty = closingQty;
    }

    public double getPurchaseQty() {
        return purchaseQty;
    }

    public void setPurchaseQty(double purchaseQty) {
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

    public double getOpeningQty() {
        return openingQty;
    }

    public void setOpeningQty(double openingQty) {
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
