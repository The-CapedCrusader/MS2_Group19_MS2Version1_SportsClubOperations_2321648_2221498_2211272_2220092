package cse213.cse213_sporting_club_operations.SilmaSubha;

import java.io.Serializable;

public class Ticket implements Serializable {
    private String name;
    private String match;
    private String venue;
    private String date;
    private String ticketType;
    private String price;

    public Ticket(String name, String match, String venue, String date, String ticketType, String price) {
        this.name = name;
        this.match = match;
        this.venue = venue;
        this.date = date;
        this.ticketType = ticketType;
        this.price = price;
    }

    // Getters
    public String getName() { return name; }
    public String getMatch() { return match; }
    public String getVenue() { return venue; }
    public String getDate() { return date; }
    public String getTicketType() { return ticketType; }
    public String getPrice() { return price; }
}
