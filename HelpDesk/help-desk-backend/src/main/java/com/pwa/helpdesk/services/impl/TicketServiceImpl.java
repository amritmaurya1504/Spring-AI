package com.pwa.helpdesk.services.impl;

import com.pwa.helpdesk.entity.Ticket;
import com.pwa.helpdesk.respositories.TicketRepository;
import com.pwa.helpdesk.services.TicketService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional
    public Ticket createTicket(Ticket ticket) {
        ticket.setId(null);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket updateTicket(Long ticketId, Ticket ticket) {
        Ticket ticketNotFound = ticketRepository.findById(ticketId).orElseThrow(() ->
                new RuntimeException("Ticket not found"));
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    @Override
    public Ticket getTicketByEmail(String email) {
        return ticketRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Ticket not found"));
    }
}
