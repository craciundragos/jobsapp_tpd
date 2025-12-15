package com.example.jobsapp.service;


import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.Loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

@Service
public class TextExtractService {

    public String extractText(String fileName) {
        File file = new File(fileName);

        if (fileName.toLowerCase().endsWith(".pdf")) {
            return extractPdfWithOcr(file);
        } else {
            return extractImageWithOcr(file);
        }
    }

    private String extractPdfWithOcr(File file) {
        try {
            PDDocument document = Loader.loadPDF(file);
            PDFRenderer renderer = new PDFRenderer(document);


            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
            tesseract.setLanguage("eng");

            StringBuilder sb = new StringBuilder();

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = renderer.renderImageWithDPI(page, 300);
                String text = tesseract.doOCR(image);
                sb.append(text).append("\n");
            }

            document.close();
            return sb.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String extractImageWithOcr(File file) {
        try {
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
            tesseract.setLanguage("eng");

            return tesseract.doOCR(file).trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}