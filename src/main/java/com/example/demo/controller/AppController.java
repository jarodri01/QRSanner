package com.example.demo.controller;

import com.example.demo.dto.DataInputRequest;
import com.example.demo.model.DataRecord;
import com.example.demo.repositories.DataRecordRepository;
import com.example.demo.service.ExcelUploaderService;
import com.example.demo.service.QRCodeGeneratorService;
import com.example.demo.service.ScannerService;
import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    private final ExcelUploaderService excelUploaderService;

    private final ScannerService scannerService;

    private final QRCodeGeneratorService qrCodeGeneratorService;

    public AppController(ExcelUploaderService excelUploaderService, ScannerService scannerService, QRCodeGeneratorService qrCodeGeneratorService) {
        this.excelUploaderService = excelUploaderService;
        this.scannerService = scannerService;
        this.qrCodeGeneratorService = qrCodeGeneratorService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/add/upload")
    public String uploadPage() {
        return "/add/upload";
    }


    @PostMapping("/upload")
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

    @PostMapping("/add/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        String path = "/add/upload";
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
            e.printStackTrace();
            model.addAttribute("message", "Failed to upload the file.");
        }

        return path;
    }

    @GetMapping("/api/scan")
    public String scanPage() {
        return "api/scan";
    }


//    @PostMapping("/scan")
//    public String handleQRCodeScan(@RequestParam("file") MultipartFile file, Model model) {
//        if (file.isEmpty()) {
//            model.addAttribute("message", "Please select a  file to scan.");
//            return "scan";
//        }
//
//        // Validate file type - Should check for PDF files
//        String contentType = file.getContentType();
//        if (contentType == null || !contentType.equals("application/pdf")) {
//            model.addAttribute("message", "Invalid file type. Please upload a valid PDF file.");
//            return "scan";
//        }
//
//        // Validate file size (example: limit file size to 5MB)
//        final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
//        if (file.getSize() > MAX_FILE_SIZE) {
//            model.addAttribute("message", "File is too large. Maximum allowed size is 5MB.");
//            return "scan";
//        }
//
//        File tempFile = null;
//        try {
//            // Save the MultipartFile into a temporary file
//            tempFile = File.createTempFile("qr-scan-", ".pdf");
//            file.transferTo(tempFile);
//
//            // Extract the QR Code from the PDF file
//            String result = extractAndScanQRCodeFromPDF(tempFile);
//
//            // Handle result after scanning QR Code
//            if (result == null || result.isEmpty() || result.startsWith("Error")) {
//                String errorMessage = (result == null || result.isEmpty())
//                        ? "Failed to scan the QR Code from the PDF."
//                        : result; // Use scannerService's error message if it starts with "Error"
//                model.addAttribute("message", errorMessage);
//            } else {
//                // Success case
//                model.addAttribute("message", "QR Code scanned successfully!");
//                model.addAttribute("result", result);
//
//                // Check if the result matches a record in the database
//                if (scannerService.matchDataWithDatabase(result)) {
//                    model.addAttribute("matchMessage", "QR Code matches with a database record.");
//                } else {
//                    model.addAttribute("matchMessage", "No matching record found in the database.");
//                }
//            }
//        } catch (Exception e) {
//            logger.error("Error occurred while processing the QR Code scan from the PDF", e);
//            model.addAttribute("message", "Failed to process the uploaded PDF file.");
//        } finally {
//            // Ensure temporary file cleanup
//            if (tempFile != null && tempFile.exists()) {
//                if (!tempFile.delete()) {
//                    logger.warn("Failed to delete temporary file: " + tempFile.getAbsolutePath());
//                }
//            }
//        }
//
//        return "scan";
//    }
//
//    /**
//     * Extracts images (such as QR codes) from a PDF and scans the QR code.
//     *
//     * @param pdfFile The PDF file to extract QR code from.
//     * @return The decoded QR code content, or null if no QR code can be found.
//     * @throws IOException If any IO error occurs while reading the PDF.
//     */
//    private String extractAndScanQRCodeFromPDF(File pdfFile) throws IOException {
//        try (PDDocument document = PDDocument.load(pdfFile)) {
//            // Use PDFBox to extract images from the PDF
//            PDFRenderer pdfRenderer = new PDFRenderer(document);
//
//            for (int page = 0; page < document.getNumberOfPages(); page++) {
//                BufferedImage file = pdfRenderer.renderImageWithDPI(page, 300); // Render at 300 DPI
//                String result = scannerService.scanQRCode(file); // Assuming scannerService supports BufferedImage input
//                if (result != null && !result.isEmpty()) {
//                    return result; // Return the first valid QR code result
//                }
//            }
//        } catch (Exception e) {
//            logger.error("Error extracting or scanning QR Code from PDF", e);
//            throw new IOException("Failed to extract and scan QR Code from the PDF file.", e);
//        }
//
//        return null; // No QR code found
//    }


    @RestController
    @RequestMapping("/api")
    public class ScannerController {

        private final ScannerService scannerService;
        private final DataRecordRepository repository;

        public ScannerController(ScannerService scannerService, DataRecordRepository repository) {
            this.scannerService = scannerService;
            this.repository = repository;


        }

        @PostMapping("/scan")
        public ResponseEntity<?> scanQRCode() {
            try {
                // Open the camera and scan the QR code
                Webcam webcam = Webcam.getDefault();
                if (webcam == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            Map.of("success", false, "message", "No webcam detected.")
                    );
                }
                webcam.open();

                // Capture QR Code image
                BufferedImage image = webcam.getImage();

                // Process QR Code
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                Result result = new MultiFormatReader().decode(bitmap);

                String qrCodeData = result.getText();
                System.out.println("QR Code data: " + qrCodeData);

                // Check the repository for a matching record
                Optional<DataRecord> recordOptional = Optional.ofNullable(repository.findByQrCodeData(qrCodeData));
                if (recordOptional.isPresent()) {
                    DataRecord record = recordOptional.get();

                    // Prepare a success response
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("success", true);
                    responseData.put("name", record.getName());
                    responseData.put("email", record.getEmail());
                    responseData.put("numberOfTickets", record.getNumberOfTickets());
                    responseData.put("isPaid", record.isPaid());

                    return ResponseEntity.ok(responseData);
                } else {
                    // No matching record found
                    return ResponseEntity.ok(Map.of(
                            "success", false,
                            "message", "No matching record found for the QR code."
                    ));
                }

            } catch (NotFoundException e) {
                System.out.println("QR Code not detected.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        Map.of("success", false, "message", "No QR Code detected. Please try again.")
                );
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        Map.of("success", false, "message", "An error occurred while processing the QR Code.")
                );
            }
        }

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
