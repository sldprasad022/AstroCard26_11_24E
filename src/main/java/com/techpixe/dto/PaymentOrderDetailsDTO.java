package com.techpixe.dto;

public class PaymentOrderDetailsDTO 
{
	private String invoice;
    private String label;
    private String orderDate;
    private Integer shiprocketOrderId;
    private String shipping_customer_name;
    private String order_id;
    
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	
	
	public Integer getShiprocketOrderId() {
		return shiprocketOrderId;
	}
	public void setShiprocketOrderId(Integer shiprocketOrderId) {
		this.shiprocketOrderId = shiprocketOrderId;
	}
	
	
	
	
	
	public String getShipping_customer_name() {
		return shipping_customer_name;
	}
	public void setShipping_customer_name(String shipping_customer_name) {
		this.shipping_customer_name = shipping_customer_name;
	}
	
	
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public PaymentOrderDetailsDTO() {
		super();
	}
	public PaymentOrderDetailsDTO(String invoice, String label, String orderDate, Integer shiprocketOrderId,
			String shipping_customer_name, String order_id) {
		super();
		this.invoice = invoice;
		this.label = label;
		this.orderDate = orderDate;
		this.shiprocketOrderId = shiprocketOrderId;
		this.shipping_customer_name = shipping_customer_name;
		this.order_id = order_id;
	}
	
	
	
	
    
    
}
