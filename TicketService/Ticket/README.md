# Ticket Application
This application requests user to provide input for
1. Number of tickets to reserve
2. email address

Upon checking the availability, the system asks for "Confirm to reserve the seats"


## Ticket Application is a Client-Server multi-threading application
The application is deployed as jar under the target directory. Server runs on 8080 port and is ready to accept the requests from client. Multiple clients can be try to book seats at same time. Hence, the seats are synchronized to avoid multiple bookings on a single seat.

## Run Locally
### Prerequisites
- Java 1.6 or above
- Maven
- (Optional) Spring Tool Suite/ eclipse

### Running the application from the command line
1. Navigate to the project directory ../../Ticket
2. Build the jar file.
    `mvn clean`
    `mvn install`
  
3. To instantiate server, run the TicketServer class
    `java -cp target/Ticket-0.0.1-SNAPSHOT.jar com.ticket.ticketServer.TicketServer`
    
4. To open clients, open another terminal and navigate to the folder ../../Ticket
5. Run the TicketClient class
	`java -cp target/Ticket-0.0.1-SNAPSHOT.jar com.ticket.ticketServer.TicketClient` 

### Running the application from STS/eclipse
1. Import the project. On the File menu, click Import, then select Existing Maven Projects, then select the root directory. When the list populates, check that your pom is selected, the press finish.
2. Navigate to /Ticket/src/main/java/com/ticket/ticketServer
3. Right click on the TicketServer.java and run as "Java Application"
4. Navigate to /Ticket/src/main/java/com/ticket/ticketServer
5. Right click on TicketClient.java and run as "Java Application". Multiple clients can be instantiated at any time.

### Limitations of this application
1. Number of seats by default were set to 25
2. Seat numbers are taken from 0-24
3. If the user provides confirmation to reserve the seats within 10 seconds, reservation is successful. If not, session will terminate
4. Unique id was returned as "emailId <seat1> <seat2> .. "

### Working of the Application:
Server is running in and is open to listen to the requests from clients. Once if server checks the availability of the seats, server requests to confirm the reservation. If the user did not respond within 10 seconds, the socket will be closed. For instance, Client A, Client B and Client C tries to book the ticket. Client should provide number of seats requesting and the email id to book the tickets. If A requests for 3 tickets and B for 4 tickets, A will be assigned seats: 0, 1, 2 and B will be assigned seats: 3,4,5,6. The total available tickets are now 18. If client D tries to reserve 25 tickets, the system will respond will available count. Meanwhile, if Client C did not confirm the reservation within 10 seconds, the client socket will be closed out with server timed out message.

  