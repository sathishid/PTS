package com.ara.approvalshipment.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Shipment {
    User user;
    @SerializedName("despatch_id")
    private int dispatchId;
    @SerializedName("despatch_product_id")
    private int productId;
    @SerializedName("despatch_dc_no")
    private String dcNo;
    @SerializedName("despatch_dc_date")
    private String date;
    @SerializedName("despatch_truck_no")
    private String truckNo;
    @SerializedName("despatch_qty")
    private double quantity;
    @SerializedName("despatch_rate")
    private double rate;
    @SerializedName("despatch_total_amount")
    private double total;
    @SerializedName("grade_name")
    private String gradeName;
    @SerializedName("station_name")
    private String stationName;
    @SerializedName("product_name")
    private String productName;
    @SerializedName("customer_name")
    private String customerName;
    private double damagedQty;
    private String damagedReason;
    private double clottedQty;
    private String clottedReason;
    private double goodQty;
    private String arrivedDate;
    private double companyDiversionQty;
    private double ownDiversionQty;

    public double getCompanyDiversionQty() {
        return companyDiversionQty;
    }

    public void setCompanyDiversionQty(double companyDiversionQty) {
        this.companyDiversionQty = companyDiversionQty;
    }

    public double getOwnDiversionQty() {
        return ownDiversionQty;
    }

    public void setOwnDiversionQty(double ownDiversionQty) {
        this.ownDiversionQty = ownDiversionQty;
    }

    public String getArrivedDate() {
        return arrivedDate;
    }

    public void setArrivedDate(String arrivedDate) {
        this.arrivedDate = arrivedDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(int dispatchId) {
        this.dispatchId = dispatchId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDcNo() {
        return dcNo;
    }

    public void setDcNo(String dcNo) {
        this.dcNo = dcNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTruckNo() {
        return truckNo;
    }

    public void setTruckNo(String truckNo) {
        this.truckNo = truckNo;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getDamagedQty() {
        return damagedQty;
    }

    public void setDamagedQty(double damagedQty) {
        this.damagedQty = damagedQty;
    }

    public String getDamagedReason() {
        return damagedReason;
    }

    public void setDamagedReason(String damagedReason) {
        this.damagedReason = damagedReason;
    }

    public double getClottedQty() {
        return clottedQty;
    }

    public void setClottedQty(double clottedQty) {
        this.clottedQty = clottedQty;
    }

    public String getClottedReason() {
        return clottedReason;
    }

    public void setClottedReason(String clottedReason) {
        this.clottedReason = clottedReason;
    }

    public double getGoodQty() {
        return goodQty;
    }

    public void setGoodQty(double goodQty) {
        this.goodQty = goodQty;
    }

    public static Shipment fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Shipment.class);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return truckNo;
    }
}
