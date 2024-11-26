package com.techpixe.dto;

public class InvoiceDTO 
{

    
    
  //Delievery Address
    private String shipping_customer_name;
    private String shipping_last_name;
    private String shipping_address;
    private String shipping_city;
    private Integer shipping_pincode;
    private String shipping_country;
    private String shipping_state;
    private String shipping_email;
    private String shipping_phone;
    
    private Double frightCharges;
	
	private Integer planAmount;//subtotal
	
	private String order_id;
	
	private String orderDate;
	
	private String uuId;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getShipping_customer_name() {
		return shipping_customer_name;
	}

	public void setShipping_customer_name(String shipping_customer_name) {
		this.shipping_customer_name = shipping_customer_name;
	}

	public String getShipping_last_name() {
		return shipping_last_name;
	}

	public void setShipping_last_name(String shipping_last_name) {
		this.shipping_last_name = shipping_last_name;
	}

	public String getShipping_address() {
		return shipping_address;
	}

	public void setShipping_address(String shipping_address) {
		this.shipping_address = shipping_address;
	}

	
	public String getShipping_city() {
		return shipping_city;
	}

	public void setShipping_city(String shipping_city) {
		this.shipping_city = shipping_city;
	}

	public Integer getShipping_pincode() {
		return shipping_pincode;
	}

	public void setShipping_pincode(Integer shipping_pincode) {
		this.shipping_pincode = shipping_pincode;
	}

	public String getShipping_country() {
		return shipping_country;
	}

	public void setShipping_country(String shipping_country) {
		this.shipping_country = shipping_country;
	}

	public String getShipping_state() {
		return shipping_state;
	}

	public void setShipping_state(String shipping_state) {
		this.shipping_state = shipping_state;
	}

	public String getShipping_email() {
		return shipping_email;
	}

	public void setShipping_email(String shipping_email) {
		this.shipping_email = shipping_email;
	}

	public String getShipping_phone() {
		return shipping_phone;
	}

	public void setShipping_phone(String shipping_phone) {
		this.shipping_phone = shipping_phone;
	}

	

	public Double getFrightCharges() {
		return frightCharges;
	}

	public void setFrightCharges(Double frightCharges) {
		this.frightCharges = frightCharges;
	}

	public Integer getPlanAmount() {
		return planAmount;
	}

	public void setPlanAmount(Integer planAmount) {
		this.planAmount = planAmount;
	}

	public String getUuId() {
		return uuId;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}

	
	
	
}
