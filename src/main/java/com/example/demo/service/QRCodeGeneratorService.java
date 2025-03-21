package com.example.demo.service;


import com.example.demo.model.DataRecord;
import com.example.demo.repositories.DataRecordRepository;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class QRCodeGeneratorService {
    private final DataRecordRepository repository;

    public QRCodeGeneratorService(DataRecordRepository repository) {
        this.repository = repository;
    }

    public void generateQRCodePDF(String pdfPath) throws Exception {
        List<DataRecord> records = repository.findAll();
        PDDocument document = new PDDocument();

        for (DataRecord record : records) {
            BufferedImage qrImage = generateQRCodeImage(record.getQrCodeData());

            PDPage page = new PDPage();
            document.addPage(page);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(toBitMatrix(record.getQrCodeData()), "PNG", baos);

            PDImageXObject image = PDImageXObject.createFromByteArray(document, baos.toByteArray(), "QR Code");
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.drawImage(image, 100, 500, 200, 200);
            contentStream.close();
        }

        document.save(pdfPath);
        document.close();
    }

    private BufferedImage generateQRCodeImage(String data) throws Exception {
        BitMatrix bitMatrix = toBitMatrix(data);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private BitMatrix toBitMatrix(String data) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        return qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
    }
}
