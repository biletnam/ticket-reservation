package com.ticket.ticketService;

import java.util.ArrayList;
import java.util.List;

import com.ticket.ticketStructure.Ticket;

public class TicketServiceImpl implements TicketService {

	public int numSeatsAvailable(List<Ticket> ticketsCount) {
		int numberOfSeatsAvailable = 0;
		for(Ticket ticket: ticketsCount) {
			if(ticket.getStatus().equalsIgnoreCase("AVAILABLE")) {
				numberOfSeatsAvailable = numberOfSeatsAvailable + 1;
			}
		}
		return numberOfSeatsAvailable;
	}

	public List<Ticket> findAndHoldSeats(int numSeats, String customerEmail, List<Ticket> ticketsCount) {
		List<Ticket> holdingSeats = new ArrayList<Ticket>();
		int bookingCount = numSeats;
		for(Ticket t: ticketsCount) {
			if(t.getStatus().equalsIgnoreCase("AVAILABLE") && bookingCount>0) {
				holdingSeats.add(t);
				bookingCount--;
				t.setStatus(Ticket.availableStatus.ONHOLD.name().toString());
			}
		}
		return holdingSeats;
	}

	public String reserveSeats(List<Ticket> ticketsOnHold, String customerEmail, List<Ticket> ticketsCount) {
		List<Ticket> reservingSeats = new ArrayList<Ticket>();
		int bookingCount = ticketsOnHold.size();
		for (Ticket t : ticketsOnHold) {
			if (ticketsCount.contains(t) && bookingCount > 0) {
				t.setStatus(Ticket.availableStatus.RESERVED.name().toString());
				ticketsCount.set(t.getSeatNumber(), t);
				reservingSeats.add(t);
				bookingCount--;
			}
		}
		StringBuffer reservedTicketsList = new StringBuffer("");
		if (ticketsOnHold.size() > 0) {
			reservedTicketsList.append(customerEmail + " ");
			for (Ticket t : reservingSeats) {
				reservedTicketsList.append(t.getSeatNumber());
				reservedTicketsList.append(" ");
			}
		}
		return reservedTicketsList.toString().trim();
	}

}
