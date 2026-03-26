package com.pwa.helpdesk.services;

import com.pwa.helpdesk.entity.Ticket;

public interface TicketService {

    Ticket createTicket(Ticket ticket);
    Ticket updateTicket(Long id, Ticket ticket);
    Ticket getTicketById(Long id);
    Ticket getTicketByEmail(String email);

}
