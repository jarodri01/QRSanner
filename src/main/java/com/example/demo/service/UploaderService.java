package com.example.demo.service;


import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Service
public class UploaderService {

    private final UserRepository userRepository;
    @Autowired
    public UploaderService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void addUser(String name, String email, String teacherName, String guestName1, String guestName2, String guestName3, String guestName4) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setTeacherName(teacherName);
        user.setGuestName1(guestName1);
        user.setGuestName2(guestName2);
        user.setGuestName3(guestName3);
        user.setGuestName4(guestName4);
        userRepository.save(user);
    }


    public void uploadUsersFromTextFile(MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length == 7) {
                User user = new User();
                user.setName(data[0].trim());
                user.setEmail(data[1].trim());
                user.setTeacherName(data[2].trim());
                user.setGuestName1(data[3].trim());
                user.setGuestName2(data[4].trim());
                user.setGuestName3(data[5].trim());
                user.setGuestName4(data[6].trim());
                //user.setTickets(Integer.parseInt(data[2].trim()));
                //user.setPaid(Boolean.parseBoolean(data[3].trim()));
                userRepository.save(user);
            }
        }
    }

    public void uploadUsersFromExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook;
            if (file.getOriginalFilename().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else {
                workbook = new HSSFWorkbook(is);
            }

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row == null || isEmpty(row) || row.getRowNum() == 0) {
                    continue;

                }

                try {
                    String name = getStringCellValue(row.getCell(0));
                    String email = getStringCellValue(row.getCell(1));
                    String teacherName = getStringCellValue(row.getCell(2));
                    String guestName1 = getStringCellValue(row.getCell(3));
                    String guestName2 = getStringCellValue(row.getCell(4));
                    String guestName3 = getStringCellValue(row.getCell(5));
                    String guestName4 = getStringCellValue(row.getCell(6));
                    //  int tickets = getNumericCellValue(row.getCell(2));
                    // boolean paid = getBooleanCellValue(row.getCell(3));

                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setTeacherName(teacherName);
                    user.setGuestName1(guestName1);
                    user.setGuestName2(guestName2);
                    user.setGuestName3(guestName3);
                    user.setGuestName4(guestName4);
                    // user.setTickets(tickets);
                    //  user.setPaid(paid);
                    userRepository.save(user);
                } catch (Exception e) {
                    System.err.println("Error processing row " + row.getRowNum() + ": " + e.getMessage());
                }
            }
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to process Excel file: " + e.getMessage(), e);
        }
    }


    private boolean isEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getCell(0) == null) {
            return true;
        }
        return getStringCellValue(row.getCell(0)).trim().isEmpty();
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            //  case NUMERIC:
            //     return String.valueOf((int) cell.getNumericCellValue());
            // case BOOLEAN:
            //    return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

//    private int getNumericCellValue(Cell cell) {
//        if (cell == null) {
//            return 0;
//        }
//        switch (cell.getCellType()) {
//            case NUMERIC:
//                return (int) cell.getNumericCellValue();
//            case STRING:
//                try {
//                    return Integer.parseInt(cell.getStringCellValue().trim());
//                } catch (NumberFormatException e) {
//                    return 0;
//                }
//            default:
//                return 0;
//        }
//    }

//    private boolean getBooleanCellValue(Cell cell) {
//        if (cell == null) {
//            return false;
//        }
//        switch (cell.getCellType()) {
//            case BOOLEAN:
//                return cell.getBooleanCellValue();
//            case STRING:
//                return Boolean.parseBoolean(cell.getStringCellValue().trim());
//            default:
//                return false;
//        }
    //   }




    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false; // Roll not found
    }

}