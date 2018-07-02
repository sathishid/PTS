package com.ara.approvalshipment.models;

public class Grade {
    private int gradeId;
    private String gradeName;
    private double quantity;


    public Grade(int gradeId, String gradeName, double quantity) {
        this.gradeId = gradeId;
        this.gradeName = gradeName;
        this.quantity = quantity;
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
