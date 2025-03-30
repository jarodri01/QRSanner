package com.example.demo.service;

import com.example.demo.model.DataRecord;
import com.example.demo.repositories.DataRecordRepository;
import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Optional;

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

    public void scanQRCodeWithCameraAndDisplayInfo() {
        try {
            // Open the camera using Java's Robot or an external library (e.g., webcam-capture API)
            Webcam webcam = Webcam.getDefault();
            if (webcam == null) {
                System.out.println("No webcam detected.");
                return;
            }
            webcam.open();

            System.out.println("Accessing the camera. Please show the QR Code.");

            // Capture the QR Code image
            BufferedImage image = webcam.getImage();

            // Process the QR Code and decode it
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);

            String qrCodeData = result.getText();
            System.out.println("QR Code data: " + qrCodeData);

            // Match the data with the database
            Optional<DataRecord> recordOptional = Optional.ofNullable(repository.findByQrCodeData(qrCodeData));
            if (recordOptional.isPresent()) {
                DataRecord record = recordOptional.get();

                // Display the user details on the Swing UI
                String info = "Name: " + record.getName() + "\n" +
                        "Email: " + record.getEmail() + "\n" +
                        "Number of Ticket: " + record.getNumberOfTickets() + "\n" +
                        "Payment Status: " + (record.isPaid() ? "Paid" : "Not Paid");

                JOptionPane.showMessageDialog(null, info);
            } else {
                JOptionPane.showMessageDialog(null, "No matching record found for the QR code.");
            }

        } catch (NotFoundException e) {
            System.out.println("No QR Code detected. Please try again.");
            JOptionPane.showMessageDialog(null, "No QR Code detected. Please try again.");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

}
