package com.example.jobsapp.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

@Service
public class Tools {

    private final S3Client s3Client;
    private final String bucketName;

    public Tools(
            S3Client s3Client,
            @Value("${aws.s3.bucket}") String bucketName
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Tool(
            name = "pdf_to_markdown",
            description = "Extract full text from a PDF resume stored in S3. The input is the S3 key, for example resumes/file.pdf."
    )
    public String pdfToMarkdown(String s3Key) {
        System.out.println("TOOL pdf_to_markdown CALLED");
        System.out.println("TOOL input s3Key = " + s3Key);
        System.out.println("TOOL bucketName = " + bucketName);

        if (s3Key == null || s3Key.isBlank()) {
            System.out.println("TOOL ERROR: s3Key is null or blank");
            throw new IllegalArgumentException("s3Key is required");
        }

        if (!s3Key.startsWith("resumes/")) {
            System.out.println("TOOL ERROR: invalid s3Key prefix: " + s3Key);
            throw new IllegalArgumentException("Invalid S3 key. Only resumes/ keys are allowed.");
        }

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            System.out.println("TOOL downloading from S3...");
            ResponseBytes<GetObjectResponse> objectBytes =
                    s3Client.getObjectAsBytes(getObjectRequest);

            byte[] pdfBytes = objectBytes.asByteArray();

            System.out.println("TOOL S3 download OK. bytes = " + pdfBytes.length);
            System.out.println("TOOL contentType = " + objectBytes.response().contentType());

            try (PDDocument document = Loader.loadPDF(pdfBytes)) {
                System.out.println("TOOL PDF loaded. pages = " + document.getNumberOfPages());

                PDFTextStripper stripper = new PDFTextStripper();
                String extractedText = stripper.getText(document);

                if (extractedText == null) {
                    System.out.println("TOOL extractedText is null");
                    return "";
                }

                extractedText = extractedText.trim();

                System.out.println("TOOL extracted text length = " + extractedText.length());

                if (extractedText.length() > 500) {
                    System.out.println("TOOL extracted text preview = " + extractedText.substring(0, 500));
                } else {
                    System.out.println("TOOL extracted text preview = " + extractedText);
                }

                if (extractedText.length() > 8000) {
                    extractedText = extractedText.substring(0, 8000);
                    System.out.println("TOOL text truncated to 8000 chars");
                }

                return extractedText;
            }

        } catch (Exception e) {
            System.out.println("TOOL ERROR in pdf_to_markdown");
            System.out.println("TOOL ERROR class = " + e.getClass().getName());
            System.out.println("TOOL ERROR message = " + e.getMessage());
            e.printStackTrace();

            throw new RuntimeException("pdf_to_markdown failed for s3Key=" + s3Key + ": " + e.getMessage(), e);
        }
    }
}