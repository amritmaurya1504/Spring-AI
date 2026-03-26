package com.pwa.helpdesk.respositories;

import com.pwa.helpdesk.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByEmail(String email);
}
