package com.memory.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateTimeTool {
    private Logger log = LoggerFactory.getLogger(DateTimeTool.class);
    @Tool(description = "Get the current date and time in users zone")
    public String getCurrentDateTime() {
        log.info("Tool Calling");
        log.info("Get the current date and time in users zone");
        return LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();
    }

    @Tool(description = "Set the alarm for given time.")
    public void setAlarm(@ToolParam(description = "Time in ISO-8601 format") String time){
        var dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        log.info("Set the alarm for given time : {}", dateTime);
    }
}
