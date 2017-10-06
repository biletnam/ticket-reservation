package com.ticket.ticketServer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ticket.ticketService.TicketServiceImpl;
import com.ticket.ticketStructure.Ticket;

public class TicketServer {

	private static final int PORT = 8080;

	private static List<Ticket> tickets = new ArrayList<Ticket>();

	private static List<Ticket> ticketsOnHold = new ArrayList<Ticket>();
	private static String  reservationUniqueId = "";

	static {
		for (int i = 0; i < 25; i++) {
			tickets.add(new Ticket(i, ""));
		}
	}

	public static void main(String[] args) throws IOException {

		System.out.println("Starting Ticket Reservation Server");
		ServerSocket listener = new ServerSocket(PORT);
		System.out.println(listener.toString());
		System.out.println("");
		try {
			while (true) {
				Socket s = listener.accept();
				new Handler(s).start();

			}
		} finally {
			listener.close();
		}
	}

	private static class Handler extends Thread {
		private Socket socket;
		private Scanner in;
		private String doReserve;
		private PrintStream out;
		private int numberOfTicketsRequested;
		private String customerEmailId;
		

		public Handler(Socket socket) {
			this.socket = socket;
		}

		/*
		 * Synchronizes the tickets list. Checks if the requested tickets are
		 * available within the system. If yes, books the ticket if user
		 * confirms with in the given time and sends back the unique code to the
		 * user. Holds the tickets until user confirms or session expires and
		 * releases them. If no, responds with number of available seats in the
		 * system.
		 */
		public void run() {
			try {
				
				in = new Scanner(socket.getInputStream());
				out = new PrintStream(socket.getOutputStream());
				
				TicketServiceImpl ticketServiceImpl = new TicketServiceImpl();
				int availableTickets = 0;
				numberOfTicketsRequested = in.nextInt();
				customerEmailId = in.next();
				System.out.println("Reserving "+ numberOfTicketsRequested + " tickets for: " + customerEmailId);
				if (numberOfTicketsRequested > 0) {
					synchronized (tickets) {
						availableTickets = ticketServiceImpl.numSeatsAvailable(tickets);
						if(availableTickets > 0 && numberOfTicketsRequested <= availableTickets) {
							ticketsOnHold = ticketServiceImpl.findAndHoldSeats(numberOfTicketsRequested, customerEmailId, tickets);
							out.println("true");
							doReserve = in.next();
							if("TIMEDOUT".equals(doReserve)){
								releaseTicketsOnHold(ticketsOnHold);
						        return;
							}
						}
						else {
							out.println(
									"Unable to make reservation. You have requested to book " + numberOfTicketsRequested + ", but Maximum available seats are: " + totalAvailableSeats());
							return;
						}
					}
					
					if(doReserve.equalsIgnoreCase("YES") || doReserve.equalsIgnoreCase("Y")) {
						synchronized(tickets) {
							reservationUniqueId = ticketServiceImpl.reserveSeats(ticketsOnHold, customerEmailId, tickets);
							if (null != reservationUniqueId || !"".equals(reservationUniqueId)) {
								ticketsOnHold = new ArrayList<Ticket>();
							}
							System.out.println("Reserved seats successfully for " + customerEmailId + " with unique id: " + reservationUniqueId);
							out.println("Thank you, for reserving with our system, " + customerEmailId + "! Please provide the unique id \"" + reservationUniqueId + "\" at the kiosk to print the tickets.");
						} 
					} else {
						out.println("Thank You, " + customerEmailId);
						return;
					}

				} else {
					out.println("Please reserve tickets > 0");
					return;
				}

			} catch (IOException e) {
				System.out.println(e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		
		/**
		 * Counts the total number of available seats
		 * @return count of available seats
		 */
		public int totalAvailableSeats() {
			int availableSeatsAtThisTime = 0;
			for(Ticket t: tickets) {
				if(t.getStatus().equalsIgnoreCase("AVAILABLE")) {
					availableSeatsAtThisTime++;
				}
			}
			
			return availableSeatsAtThisTime;
		}
		
		/**
		 * Upon session expiry or user selection of no, releases tickets from hold
		 * @param ticketsOnHold
		 */
		public void releaseTicketsOnHold(List<Ticket> ticketsOnHold) {
			int bookingCount = ticketsOnHold.size();
			for (Ticket t : ticketsOnHold) {
				if (tickets.contains(t) && bookingCount > 0) {
					t.setStatus(Ticket.availableStatus.AVAILABLE.name().toString());
					tickets.set(t.getSeatNumber(), t);
					bookingCount--;
				}
			}
		}
		
	}
}
