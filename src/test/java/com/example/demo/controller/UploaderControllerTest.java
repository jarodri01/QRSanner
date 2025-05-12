//package com.example.demo.controller;
//
//import com.example.demo.service.UploaderService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.web.multipart.MultipartFile;
//
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
//
//@WebMvcTest(UploaderController.class)
//public class UploaderControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private UploaderService uploaderService;
//
//    @Test
//    void uploadUsers_shouldRedirectToUploaderAfterSuccessfulFileUpload() throws Exception {
//        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
//        doNothing().when(uploaderService).uploadUsersFromFile(Mockito.any(MultipartFile.class));
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/uploader/upload")
//                        .file("file", "test content".getBytes())
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/uploader"));
//
//        verify(uploaderService).uploadUsersFromFile(Mockito.any(MultipartFile.class));
//    }
//}