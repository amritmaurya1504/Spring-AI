package com.memory;

import com.memory.helper.Helper;
import com.memory.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemoryApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private ChatService chatService;

	@Test
	void saveDataToVectorDatabase(){
		System.out.println("saving data to vector database");
		this.chatService.saveData(Helper.getData());
		System.out.println("saved data to vector database");
	}
}
