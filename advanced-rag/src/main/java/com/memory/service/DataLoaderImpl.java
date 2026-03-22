package com.memory.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLoaderImpl implements DataLoader {

    @Value("classpath:sample-data.json")
    private Resource jsonResource;

    @Value("classpath:cricket_rules.pdf")
    private Resource pdfResource;

    @Override
    public List<Document> loadDocumentFromJson() {
        System.out.println("started loading json");
        var jsonReader = new JsonReader(jsonResource, "events");
        return jsonReader.get();
    }

    @Override
    public List<Document> loadDocumentFromPDF() {
        System.out.println("started loading pdf");
        var pdfReader = new PagePdfDocumentReader(pdfResource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(
                                ExtractedTextFormatter.builder()
                                        .withNumberOfTopTextLinesToDelete(0)
                                        .build()
                        )
                        .withPagesPerDocument(1)
                        .build());

        return pdfReader.read();
    }
}
