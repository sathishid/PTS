package com.ara.approvalshipment.models;

import com.google.gson.annotations.SerializedName;

public class ShipmentDetail {
    @SerializedName("vehicle_count")
    int vehicleCount;
    @SerializedName("total_qty")
    double dispatchedCount;

    public int getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(int vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public double getDispatchedCount() {
        return dispatchedCount;
    }

    public void setDispatchedCount(double dispatchedCount) {
        this.dispatchedCount = dispatchedCount;
    }
}
