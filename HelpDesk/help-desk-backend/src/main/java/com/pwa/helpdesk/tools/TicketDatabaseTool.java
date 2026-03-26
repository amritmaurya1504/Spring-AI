package com.pwa.helpdesk.tools;

import com.pwa.helpdesk.entity.Ticket;
import com.pwa.helpdesk.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketDatabaseTool {

    private final TicketService ticketService;

    @Tool(description = "This tools helps to create new ticket in database")
    public Ticket createTicketTool(@ToolParam(description = "Ticket fields required to create new ticket") Ticket ticket){
        try{
            return ticketService.createTicket(ticket);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Tool(description = "This tools helps to get ticket details by email from database")
    public Ticket getTicketByUserName(@ToolParam(description = "Email whose ticket is required") String email){
        return ticketService.getTicketByEmail(email);
    }

    @Tool(description = "This tools helps to update ticket details in database")
    public Ticket updateTicket(@ToolParam(description = "new ticket details with ticket id.") Ticket ticket){
        return ticketService.updateTicket(ticket.getId(), ticket);
    }

}
