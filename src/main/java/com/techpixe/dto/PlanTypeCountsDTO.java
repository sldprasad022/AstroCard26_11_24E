package com.techpixe.dto;


public class PlanTypeCountsDTO {

	private Long totalActiveUsers;
    private Long totalCardUsersCount;
    private Long totalWebUsersCount;
    private Long totalCardAndWebUsersCount;
    

	// Getters and Setters
    public Long getTotalCardUsersCount() {
        return totalCardUsersCount;
    }

    public void setTotalCardUsersCount(Long totalCardUsersCount) {
        this.totalCardUsersCount = totalCardUsersCount;
    }

    public Long getTotalWebUsersCount() {
        return totalWebUsersCount;
    }

    public void setTotalWebUsersCount(Long totalWebUsersCount) {
        this.totalWebUsersCount = totalWebUsersCount;
    }

    public Long getTotalCardAndWebUsersCount() {
        return totalCardAndWebUsersCount;
    }

    public void setTotalCardAndWebUsersCount(Long totalCardAndWebUsersCount) {
        this.totalCardAndWebUsersCount = totalCardAndWebUsersCount;
    }

	public Long getTotalActiveUsers() {
		return totalActiveUsers;
	}

	public void setTotalActiveUsers(Long totalActiveUsers) {
		this.totalActiveUsers = totalActiveUsers;
	}

	

	
    
}
