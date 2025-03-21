package com.example.demo.service;

import com.example.demo.model.DataRecord;
import com.example.demo.repositories.DataRecordRepository;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

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
            Iterator<Row> rows = sheet.iterator();

            while (rows.hasNext()) {
                Row row = rows.next();
                DataRecord record = new DataRecord();
                record.setName(row.getCell(0).getStringCellValue());
                record.setQrCodeData(row.getCell(1).getStringCellValue());
                repository.save(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
