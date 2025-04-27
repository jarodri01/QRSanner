//package com.example.demo.controller;
//
//import com.example.demo.model.User;
//import com.example.demo.repositories.UserRepository;
//import com.example.demo.service.EmailService;
//import com.example.demo.service.QRCodeService;
//import com.google.zxing.WriterException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.MediaType;
//
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Optional;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(UserController.class)
//@AutoConfigureMockMvc
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private QRCodeService qrCodeService;
//
//    @Mock
//    private EmailService emailService;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize the mocks before each test
//        MockitoAnnotations.openMocks(this);
//    }
//    @TestConfiguration
//    static class MockConfiguration {
//
//        @Bean
//        public UserRepository userRepository() {
//            return Mockito.mock(UserRepository.class);
//        }
//
//        @Bean
//        public QRCodeService qrCodeService() {
//            return Mockito.mock(QRCodeService.class);
//        }
//
//        @Bean
//        public EmailService emailService() {
//            return Mockito.mock(EmailService.class);
//        }
//    }
//
//
//    @Test
//    void testGenerateQRCode_UserExists() throws Exception {
//        // Arrange: Set up test data and mocked behavior
//        User user = new User();
//        user.setId(1L);
//        user.setName("John Doe");
//        user.setEmail("john.doe@example.com");
//        user.setPaid(true);
//        user.setTickets(3);
//
//        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        Mockito.when(qrCodeService.generateQRCode(1L)).thenReturn("GeneratedQRCode");
//
//
//        // Act & Assert: Perform the request and validate the response
//        mockMvc.perform(get("/user/generate-qr/1")
//                        .accept(MediaType.TEXT_HTML))
//                .andExpect(status().isOk()) // Verifying HTTP status
//                .andExpect(view().name("qr-page")) // Verifying the view name
//                .andExpect(model().attributeExists("qrCode", "user")) // Verifying attributes exist
//                .andExpect(model().attribute("qrCode", "GeneratedQRCode")) // Validating qrCode content
//                .andExpect(model().attribute("user", user)) // Validating user object
//                .andExpect(content().string(containsString("GeneratedQRCode"))) // Checking response contains QR code
//                .andExpect(content().string(containsString("John Doe"))); // Checking response contains user's name
//
//        // Verify: Ensure methods were invoked as expected
//        Mockito.verify(userRepository).findById(1L);
//        Mockito.verify(qrCodeService).generateQRCode(1L);
//
//    }
//
//    @Test
//    void testGenerateQRCode_UserDoesNotExist() throws Exception {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/user/generate-qr/1")
//                        .accept(MediaType.TEXT_HTML))
//                .andExpect(status().isOk())
//                .andExpect(view().name("error"));
//    }
//
//    @Test
//    void testGenerateQRCode_ExceptionThrown() throws Exception {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
//        when(qrCodeService.generateQRCode(anyLong())).thenThrow(new WriterException());
//
//        mockMvc.perform(get("/user/generate-qr/1")
//                        .accept(MediaType.TEXT_HTML))
//                .andExpect(status().isOk())
//                .andExpect(view().name("error"));
//    }
//}