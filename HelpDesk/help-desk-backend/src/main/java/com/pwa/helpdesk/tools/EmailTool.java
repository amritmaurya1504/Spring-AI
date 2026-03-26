package com.pwa.helpdesk.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class EmailTool {

    Logger log = LoggerFactory.getLogger(EmailTool.class);

    @Tool(description = "This tools helps to send email to support team")
    public void sendEmail(String email, String message){
        log.info("Email sent to support team");
        log.info("Email: {}", email);
        log.info("Message: {}", message);
    }

}
