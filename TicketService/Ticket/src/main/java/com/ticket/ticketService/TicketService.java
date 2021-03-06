package com.ticket.ticketService;

import java.util.List;

import com.ticket.ticketStructure.Ticket;

public interface TicketService {
	
	/**
	* The number of seats in the venue that are neither held nor reserved
	* @param tickets list of tickets
	* @return the number of tickets available in the venue
	* 
	*/
	int numSeatsAvailable(List<Ticket> tickets);
	/**
	* Find and hold the best available seats for a customer
	*
	* @param numSeats the number of seats to find and hold
	* @param customerEmail unique identifier for the customer
	* @param tickets List of tickets
	* @return List of seats that are on hold
	*/
	List<Ticket> findAndHoldSeats(int numSeats, String customerEmail, List<Ticket> tickets);
	/**
	* Commit seats held for a specific customer
	*
	* @param ticketsOnHold tickets on hold 
	* @param customerEmail the email address of the customer to which the
	seat hold is assigned
	* @param tickets list of tickets
	* @return a reservation confirmation code
	*/
	String reserveSeats(List<Ticket> ticketsOnHold, String customerEmail, List<Ticket> tickets);

}
