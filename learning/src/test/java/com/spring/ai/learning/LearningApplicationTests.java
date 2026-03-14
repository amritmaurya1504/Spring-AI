package com.spring.ai.learning;

import com.spring.ai.learning.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LearningApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private ChatService chatService;
	@Test
	void testTemplateRender(){
		System.out.println("Template Rendered");
		var chat = this.chatService.chatTemplate();
		System.out.println("Response from chat template: " + chat);
	}

}
