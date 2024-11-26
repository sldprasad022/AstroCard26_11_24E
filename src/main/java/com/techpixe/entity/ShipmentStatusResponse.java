package com.techpixe.entity;

import java.util.List;

public class ShipmentStatusResponse 
{
	private String shipmentId;
    private String status;
    private int statusCode;
    private String awbCode;
    private String courierName;
    private int onboardingCompletedNow;
    private String pickupLocation;
    private String billingCustomerName;
    private ShippingAddress shippingAddress;
    private List<OrderItem> orderItems;

    // Getters and setters for each field
    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getAwbCode() {
        return awbCode;
    }

    public void setAwbCode(String awbCode) {
        this.awbCode = awbCode;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public int getOnboardingCompletedNow() {
        return onboardingCompletedNow;
    }

    public void setOnboardingCompletedNow(int onboardingCompletedNow) {
        this.onboardingCompletedNow = onboardingCompletedNow;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getBillingCustomerName() {
        return billingCustomerName;
    }

    public void setBillingCustomerName(String billingCustomerName) {
        this.billingCustomerName = billingCustomerName;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
	    
	    
}
