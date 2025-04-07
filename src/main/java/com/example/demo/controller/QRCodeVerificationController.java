//package com.example.demo.controller;
//
//import com.example.demo.model.DataRecord;
//import com.example.demo.repositories.DataRecordRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.util.Optional;
//
//@Controller
//public class QRCodeVerificationController {
//
//    @Autowired
//    private DataRecordRepository repository;
//
//    @PostMapping("/verify-qr")
//    public String verifyQRCode(@RequestParam("qrContent") String qrContent, Model model) {
//        try {
//            // Parse QR Code content
//            ObjectMapper mapper = new ObjectMapper();
//            DataRecord qrRecord = mapper.readValue(qrContent, DataRecord.class);
//
//            // Fetch data from the database
//            Optional<DataRecord> optionalRecord = repository.findById(qrRecord.getId());
//            if (!optionalRecord.isPresent()) {
//                model.addAttribute("message", "QR Code does not match any record.");
//                model.addAttribute("status", "error");
//                return "verify";
//            }
//
//            DataRecord dbRecord = optionalRecord.get();
//
//            // Compare QR data with database data
//            if (qrRecord.getName().equals(dbRecord.getName()) &&
//                    qrRecord.getEmail().equals(dbRecord.getEmail())) {
//                model.addAttribute("message", "Match successful!");
//                model.addAttribute("status", "success");
//            } else {
//                model.addAttribute("message", "QR Code does not match the database record.");
//                model.addAttribute("status", "error");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("message", "Error occurred during verification.");
//            model.addAttribute("status", "error");
//        }
//
//        return "verify"; // Return a verification UI
//    }
//}
//
