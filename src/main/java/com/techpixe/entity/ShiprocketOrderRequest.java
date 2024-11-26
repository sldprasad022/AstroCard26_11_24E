package com.techpixe.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class ShiprocketOrderRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sId;

    private String order_id;
    //private String order_date;
    
    private String orderDate;
    
    private String pickup_location;
    private String channel_id;
    private String comment;
    private String billing_customer_name;
    private String billing_last_name;
    private String billing_address;
    private String billing_address_2;
    private String billing_city;
    //private String billing_pincode;
    
    private Integer billing_pincode;
    
    private String billing_state;
    private String billing_country;
    private String billing_email;
    private String billing_phone;
    private boolean shipping_is_billing;
    private String shipping_customer_name;
    private String shipping_last_name;
    private String shipping_address;
    private String shipping_address_2;
    private String shipping_city;
    private Integer shipping_pincode;
    private String shipping_country;
    private String shipping_state;
    private String shipping_email;
    private String shipping_phone;
    
    private Double frightCharges;
    

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sId")  // foreign key in OrderItem table
    private List<OrderItem> order_items;

   
    private String payment_method;
    
    private Integer sub_total;
    private double length;
    private double breadth;
    private double height;
    private double weight;

    // Getters and Setters
    
//--------------------------------------------    

    
 
 //---------------------------------------   

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userProfileId")  // This will create a foreign key in the ShiprocketOrderRequest table
	@JsonIgnore
	private UserProfile userProfile;
    
    private String shipmentId;
    
    
    private String status;
	private int statusCode;
	private int onboardingCompletedNow;
	private String awbCode;  //Tracking Url
	private Integer courierCompanyId; //Courier Name
	private String courierName;
	
	
	private String label;

	private String invoice;
	
	private Integer shiprocketOrderId;
	
	private Integer planAmount;
	
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

	public String getpickup_location() {
		return pickup_location;
	}

	public void setpickup_location(String pickup_location) {
		this.pickup_location = pickup_location;
	}

	

	public String getchannel_id() {
		return channel_id;
	}

	public void setchannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getBilling_customer_name() {
		return billing_customer_name;
	}

	public void setBilling_customer_name(String billing_customer_name) {
		this.billing_customer_name = billing_customer_name;
	}

	public String getbilling_last_name() {
		return billing_last_name;
	}

	public void setbilling_last_name(String billing_last_name) {
		this.billing_last_name = billing_last_name;
	}

	public String getbilling_address() {
		return billing_address;
	}

	public void setbilling_address(String billing_address) {
		this.billing_address = billing_address;
	}

	public String getbilling_address_2() {
		return billing_address_2;
	}

	public void setbilling_address_2(String billing_address_2) {
		this.billing_address_2 = billing_address_2;
	}

	public String getbilling_city() {
		return billing_city;
	}

	public void setbilling_city(String billing_city) {
		this.billing_city = billing_city;
	}


	
	

	public String getbilling_state() {
		return billing_state;
	}

	public Integer getBilling_pincode() {
		return billing_pincode;
	}

	public void setBilling_pincode(Integer billing_pincode) {
		this.billing_pincode = billing_pincode;
	}

	public void setbilling_state(String billing_state) {
		this.billing_state = billing_state;
	}

	public String getbilling_country() {
		return billing_country;
	}

	public void setbilling_country(String billing_country) {
		this.billing_country = billing_country;
	}

	public String getbilling_email() {
		return billing_email;
	}

	public void setbilling_email(String billing_email) {
		this.billing_email = billing_email;
	}

	public String getbilling_phone() {
		return billing_phone;
	}

	public void setbilling_phone(String billing_phone) {
		this.billing_phone = billing_phone;
	}

	public boolean isshipping_is_billing() {
		return shipping_is_billing;
	}

	public void setshipping_is_billing(boolean shipping_is_billing) {
		this.shipping_is_billing = shipping_is_billing;
	}

	public String getshipping_customer_name() {
		return shipping_customer_name;
	}

	public void setshipping_customer_name(String shipping_customer_name) {
		this.shipping_customer_name = shipping_customer_name;
	}

	public String getshipping_last_name() {
		return shipping_last_name;
	}

	public void setshipping_last_name(String shipping_last_name) {
		this.shipping_last_name = shipping_last_name;
	}

	public String getshipping_address() {
		return shipping_address;
	}

	public void setshipping_address(String shipping_address) {
		this.shipping_address = shipping_address;
	}

	public String getshipping_address_2() {
		return shipping_address_2;
	}

	public void setshipping_address_2(String shipping_address_2) {
		this.shipping_address_2 = shipping_address_2;
	}

	public String getshipping_city() {
		return shipping_city;
	}

	public void setshipping_city(String shipping_city) {
		this.shipping_city = shipping_city;
	}

	

	public Integer getShipping_pincode() {
		return shipping_pincode;
	}

	public void setShipping_pincode(Integer shipping_pincode) {
		this.shipping_pincode = shipping_pincode;
	}

	public String getshipping_country() {
		return shipping_country;
	}

	public void setshipping_country(String shipping_country) {
		this.shipping_country = shipping_country;
	}

	public String getshipping_state() {
		return shipping_state;
	}

	public void setshipping_state(String shipping_state) {
		this.shipping_state = shipping_state;
	}

	public String getshipping_email() {
		return shipping_email;
	}

	public void setshipping_email(String shipping_email) {
		this.shipping_email = shipping_email;
	}

	public String getshipping_phone() {
		return shipping_phone;
	}

	public void setshipping_phone(String shipping_phone) {
		this.shipping_phone = shipping_phone;
	}

	public List<OrderItem> getorder_items() {
		return order_items;
	}

	public void setorder_items(List<OrderItem> order_items) {
		this.order_items = order_items;
	}

	public String getpayment_method() {
		return payment_method;
	}

	public void setpayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	

	public Integer getSub_total() {
		return sub_total;
	}

	public void setSub_total(Integer sub_total) {
		this.sub_total = sub_total;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getBreadth() {
		return breadth;
	}

	public void setBreadth(double breadth) {
		this.breadth = breadth;
	}

	
	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	

	public UserProfile getUserProfile() {
	    return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
	    this.userProfile = userProfile;
	}
    
	@Override
	public String toString() {
		return "ShiprocketOrderRequest [sId=" + sId + ", order_id=" + order_id + ", orderDate=" + orderDate
				+ ", pickup_location=" + pickup_location + ", channel_id=" + channel_id + ", comment=" + comment
				+ ", billing_customer_name=" + billing_customer_name + ", billing_last_name=" + billing_last_name
				+ ", billing_address=" + billing_address + ", billing_address_2=" + billing_address_2
				+ ", billing_city=" + billing_city + ", billing_pincode=" + billing_pincode + ", billing_state="
				+ billing_state + ", billing_country=" + billing_country + ", billing_email=" + billing_email
				+ ", billing_phone=" + billing_phone + ", shipping_is_billing=" + shipping_is_billing
				+ ", shipping_customer_name=" + shipping_customer_name + ", shipping_last_name=" + shipping_last_name
				+ ", shipping_address=" + shipping_address + ", shipping_address_2=" + shipping_address_2
				+ ", shipping_city=" + shipping_city + ", shipping_pincode=" + shipping_pincode + ", shipping_country="
				+ shipping_country + ", shipping_state=" + shipping_state + ", shipping_email=" + shipping_email
				+ ", shipping_phone=" + shipping_phone + ", order_items=" + order_items + ", payment_method="
				+ payment_method + ", sub_total=" + sub_total + ", length=" + length + ", breadth=" + breadth
				+ ", height=" + height + ", weight=" + weight + ", userProfile=" + userProfile + ", shipmentId="
				+ shipmentId + ", status=" + status + ", statusCode=" + statusCode + ", onboardingCompletedNow="
				+ onboardingCompletedNow + ", awbCode=" + awbCode + ", courierCompanyId=" + courierCompanyId
				+ ", courierName=" + courierName + "]";
	}

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

	public int getOnboardingCompletedNow() {
		return onboardingCompletedNow;
	}

	public void setOnboardingCompletedNow(int onboardingCompletedNow) {
		this.onboardingCompletedNow = onboardingCompletedNow;
	}



	public String getAwbCode() {
		return awbCode;
	}

	public void setAwbCode(String awbCode) {
		this.awbCode = awbCode;
	}

	public Integer getCourierCompanyId() {
		return courierCompanyId;
	}

	public void setCourierCompanyId(Integer courierCompanyId) {
		this.courierCompanyId = courierCompanyId;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public Double getFrightCharges() {
		return frightCharges;
	}

	public void setFrightCharges(Double frightCharges) {
		this.frightCharges = frightCharges;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	
	
	

	public Integer getShiprocketOrderId() {
		return shiprocketOrderId;
	}

	public void setShiprocketOrderId(Integer shiprocketOrderId) {
		this.shiprocketOrderId = shiprocketOrderId;
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

