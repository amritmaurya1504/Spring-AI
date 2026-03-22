package com.memory;

import com.memory.service.ChatService;
import com.memory.tools.WeatherTool;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MemoryApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private ChatService chatService;

	@Autowired
	private WeatherTool weatherTool;

	@Test
	void getWeatherTest(){
		var response = weatherTool.getWeather("Delhi India");
		System.out.println(response);
	}

}
