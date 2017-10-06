package com.ticket.ticketStructure;

public class Ticket {
	

	
	private int seatNumber;
	private String customerEmailId;
	public static enum availableStatus {
		AVAILABLE, ONHOLD, RESERVED
		
	}
	private String status;
	
	public Ticket(int seatNumber, String customerEmailId) {
		this.seatNumber = seatNumber;
		this.customerEmailId = customerEmailId;
		this.status = availableStatus.AVAILABLE.name().toString();
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
	public String getCustomerEmailId() {
		return customerEmailId;
	}
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}
	
	
}
