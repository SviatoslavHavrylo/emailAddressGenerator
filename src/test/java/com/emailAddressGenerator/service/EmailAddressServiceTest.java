package com.emailAddressGenerator.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmailAddressServiceTest {

    private EmailAddressService emailAddressService;

    @Before
    public void setUp() {
        emailAddressService = new EmailAddressService();
    }

    @Test
    public void saveNamesToFile() {
        saveExampleName();
        emailAddressService.saveNamesToFile();
        emailAddressService.loadSavedNamesFromFile();
        List<String> emailNames = emailAddressService.getEmailAddresses();
        assertFalse(emailNames.isEmpty());
    }

    @Test
    public void saveEmailNames() {
        saveExampleName();
        List<String> emailNames = emailAddressService.getEmailAddresses();
        assertFalse(emailNames.isEmpty());
    }

    private void saveExampleName() {
        String str = "Example String";
        byte[] content = str.getBytes();
        MultipartFile multipartFile = new MockMultipartFile("test_file.txt", content);
        emailAddressService.saveEmailNames(multipartFile);
    }

    @Test
    public void cleanEmailAddress() {
        saveExampleName();
        emailAddressService.cleanEmailAddress();
        List<String> emailNames = emailAddressService.getEmailAddresses();
        assertTrue(emailNames.isEmpty());
    }
}
