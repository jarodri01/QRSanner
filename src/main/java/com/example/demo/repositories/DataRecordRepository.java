package com.example.demo.repositories;

import com.example.demo.model.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRecordRepository extends JpaRepository<DataRecord, Long> {
    DataRecord findByQrCodeData(String qrCodeData); // need query to find by QR code
}
