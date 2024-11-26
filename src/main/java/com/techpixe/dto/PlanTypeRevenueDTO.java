package com.techpixe.dto;

public class PlanTypeRevenueDTO {

    private Double totalPaymentsAmount; // Total sum of all payments
    private Double totalCardRevenue; // Total sum of payments for Card
    private Double totalWebRevenue; // Total sum of payments for Web
    private Double totalCardAndWebRevenue; // Total sum of payments for Card and Web

    // Getters and Setters
    

    public Double getTotalCardRevenue() {
        return totalCardRevenue;
    }

    public Double getTotalPaymentsAmount() {
		return totalPaymentsAmount;
	}

	public void setTotalPaymentsAmount(Double totalPaymentsAmount) {
		this.totalPaymentsAmount = totalPaymentsAmount;
	}

	public void setTotalCardRevenue(Double totalCardRevenue) {
        this.totalCardRevenue = totalCardRevenue;
    }

    public Double getTotalWebRevenue() {
        return totalWebRevenue;
    }

    public void setTotalWebRevenue(Double totalWebRevenue) {
        this.totalWebRevenue = totalWebRevenue;
    }

    public Double getTotalCardAndWebRevenue() {
        return totalCardAndWebRevenue;
    }

    public void setTotalCardAndWebRevenue(Double totalCardAndWebRevenue) {
        this.totalCardAndWebRevenue = totalCardAndWebRevenue;
    }
}

