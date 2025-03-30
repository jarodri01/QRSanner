package com.example.demo.service;

import com.example.demo.dto.DataInputRequest;
import com.example.demo.model.DataRecord;
import com.example.demo.repositories.DataRecordRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelUploaderService {
    private final DataRecordRepository repository;

    public ExcelUploaderService(DataRecordRepository repository) {
        this.repository = repository;
    }

    public void uploadTxtFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            List<DataRecord> records = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(","); // Assuming the .txt file uses CSV format

                // Create a DataRecord entity after validating fields
                if (fields.length >= 2) {
                    DataRecord record = new DataRecord();
                    record.setName(fields[0].trim());
                    record.setQrCodeData(fields[1].trim());
                    // Add additional fields as needed
                    records.add(record);
                }
            }

            // Save all records to the H2 database
            repository.saveAll(records);

        } catch (IOException e) {
            e.printStackTrace(); // Log this error appropriately
            throw new RuntimeException("Failed to process the .txt file: " + e.getMessage());
        }
    }

    public DataRecord addManualData(DataInputRequest request) {
        if (request == null || request.getName() == null || request.getQrCodeData() == null) {
            throw new IllegalArgumentException("Invalid request: Name and QR Code Data are required.");
        }
        DataRecord dataRecord = new DataRecord();

        dataRecord.setQrCodeData(request.getQrCodeData());
        dataRecord.setName(request.getName());
        dataRecord.setNumberOfTickets(request.getNumberOfTickets());
        dataRecord.setPaid(request.isPaid());

        return repository.save(dataRecord);
    }

    public List<DataRecord> fetchAllData() {
        return repository.findAll();
    }
}


