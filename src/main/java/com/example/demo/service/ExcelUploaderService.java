package com.example.demo.service;

import com.example.demo.dto.DataInputRequest;
import com.example.demo.model.DataRecord;
import com.example.demo.repositories.DataRecordRepository;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelUploaderService {
    private final DataRecordRepository repository;

    public ExcelUploaderService(DataRecordRepository repository) {
        this.repository = repository;
    }

    public void uploadExcel(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                DataRecord record = new DataRecord();
                record.setName(row.getCell(0).getStringCellValue());
                record.setQrCodeData(row.getCell(1).getStringCellValue());
                repository.save(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataRecord addManualData(DataInputRequest request) {
        // Convert the DTO to the entity class
        DataRecord dataRecord = new DataRecord();
        dataRecord.setId(request.getId());
        dataRecord.setQrCodeData(request.getQrCodeData());
        dataRecord.setName(request.getName());
        dataRecord.setNumberOfTickets(request.getNumberOfTickets());
        dataRecord.setPaid(request.isPaid());

        // Save the record into the database
        return repository.save(dataRecord);
    }

    // Optional: Retrieve all data from the database for verification (useful for testing)
    public List<DataRecord> fetchAllData() {
        return repository.findAll();
    }
}

