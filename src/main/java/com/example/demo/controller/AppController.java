package com.example.demo.controller;

import com.example.demo.dto.DataInputRequest;
import com.example.demo.model.DataRecord;
import com.example.demo.service.ExcelUploaderService;
import com.example.demo.service.QRCodeGeneratorService;
import com.example.demo.service.ScannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Controller
public class AppController {

    @Autowired
    private ExcelUploaderService excelUploaderService;

    @Autowired
    private ScannerService scannerService;

    @Autowired
    private QRCodeGeneratorService qrCodeGeneratorService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "/upload";
    }


    @PostMapping("/api/add/upload")
    public ResponseEntity<DataRecord> addData(@ModelAttribute DataInputRequest request) {
        System.out.println("Received DataInputRequest: " + request);

        DataRecord savedData = excelUploaderService.addManualData(request);

        if (savedData != null) {
            System.out.println("Saved Record: " + savedData);

            return new ResponseEntity<>(savedData, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/text/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        String path = "/api/text/upload";
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload.");
            return path;
        }

        try {
            File tempFile = File.createTempFile("uploaded-", ".txt");
            file.transferTo(tempFile);

            excelUploaderService.uploadTxtFile(tempFile);
            model.addAttribute("message", "File uploaded successfully!");

        } catch (IOException e) {
            e.printStackTrace(); //why is this yellow
            model.addAttribute("message", "Failed to upload the file.");
        }

        return path;
    }

    @GetMapping("/scan")
    public String scanPage() {
        return "scan";
    }

    @PostMapping("/scan")
    public String handleQRCodeScan(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select an image to scan.");
            return "scan";
        }

        try {
            File tempFile = File.createTempFile("qr-scan-", ".png");
            file.transferTo(tempFile);

            String result = scannerService.scanQRCode(tempFile);

            if (result.startsWith("Error")) {
                model.addAttribute("message", result);
            } else {
                model.addAttribute("message", "QR Code scanned successfully!");
                model.addAttribute("result", result);

                if (scannerService.matchDataWithDatabase(result)) {
                    model.addAttribute("matchMessage", "QR Code matches with database record.");
                } else {
                    model.addAttribute("matchMessage", "No matching record found in the database.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // why is this yellow
            model.addAttribute("message", "Failed to process the image.");
        }

        return "scan";
    }

    @GetMapping("/generate")
    public String generatePage() {
        return "generate";
    }


    @PostMapping("/generate")
    public String handleQRCodeGeneration(Model model) {
        try {
            qrCodeGeneratorService.generateQRCodePDF("output/qrcodes.pdf");
            model.addAttribute("message", "QR Code generation process completed. Check logs for details.");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to generate QR Code PDF: " + e.getMessage());
        }
        return "generate";
    }

}
