package com.example.demo.service;


import com.google.zxing.BarcodeFormat;

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import org.springframework.stereotype.Service;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;


@Service
public class QRCodeService {

    private static final String BASE_URL = "https://ticketscanner-5849c904ea5f.herokuapp.com/guest/";

    public String generateQRCode(Long guestId) throws WriterException, IOException {

        String userDetailsUrl = BASE_URL + guestId;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                userDetailsUrl,
                BarcodeFormat.QR_CODE,
                250,
                250
        );

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(qrCodeBytes);

    }
}
