package com.ara.approvalshipment.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class SalesOrder {
    private int id;
    private String date;
    private List<OrderItem> orderItems;
    private User user;
    private double totalQuantity;

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(double totalPrice) {
        this.totalQuantity = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {

        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        getOrderItems().add(orderItem);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public static SalesOrder fromJson(String message) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(message, SalesOrder.class);
    }
}
