package com.memory;

import com.memory.service.ChatService;
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
	private DataLoader dataLoader;

	@Autowired
	private DataTransformer dataTransformer;

	@Test
	void saveDataToVectorDatabase(List<Document> documentList){
		System.out.println("saving data to vector database");
		this.chatService.saveData(documentList);
		System.out.println("saved data to vector database");
	}

	@Test
	void testJsonDataLoader() {
		var documents = dataLoader.loadDocumentFromJson();
		documents.forEach(System.out::println);
		System.out.println(documents.size());
	}

	@Test
	void testPdfDataLoader() {
		var documents = dataLoader.loadDocumentFromPDF();
		System.out.println(documents.size());
		documents.forEach(item -> {
			System.out.println(item);
			System.out.println("________________________________");
		});

		var transform = this.dataTransformer.transform(documents);
		System.out.println(transform.size());

		// saving data to vectors tore database
		this.saveDataToVectorDatabase(transform);

		System.out.println("Done saving data to vector database maria db");
	}

}
