package com.example.demo.service;

import com.example.demo.model.DataRecord;
import com.example.demo.repositories.DataRecordRepository;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class ScannerService {

    private final DataRecordRepository repository;

    public ScannerService(DataRecordRepository repository) {
        this.repository = repository;
    }

    public String scanQRCode(File file) {
        try {
            // Load the image file
            BufferedImage bufferedImage = ImageIO.read(file);

            // Convert the image to binary for QR Code decoding
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // Decode the QR Code
            Result result = new MultiFormatReader().decode(bitmap);

            return result.getText(); // Return the decoded text
        } catch (NotFoundException e) {
            return "Error: QR Code not found in the image.";
        } catch (Exception e) {
            return "Error: Unable to process the image.";
        }
    }

    public boolean matchDataWithDatabase(String qrCodeData) {
        // Query the database for a record with the given QR code data
        DataRecord record = repository.findByQrCodeData(qrCodeData);
        return record != null; // Return true if a matching record exists
    }
}
