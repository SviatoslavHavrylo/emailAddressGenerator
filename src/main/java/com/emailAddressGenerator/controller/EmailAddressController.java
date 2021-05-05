package com.emailAddressGenerator.controller;

import com.emailAddressGenerator.service.EmailAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/emailAddressGenerator/v1/")
public class EmailAddressController {

    private final EmailAddressService emailAddressService;

    @Autowired
    public EmailAddressController(EmailAddressService emailAddressService) {
        this.emailAddressService = emailAddressService;
    }

    @PostMapping
    public ResponseEntity<String> readNames(@RequestParam("file") MultipartFile multipartFile) {
        if (null == multipartFile.getOriginalFilename()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            emailAddressService.saveEmailNames(multipartFile) ;

            return new ResponseEntity<>("Names recieved", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity getEmailAddress() {
        List<String> emailNames = emailAddressService.getEmailAddresses();
        ResponseEntity responseEntity = new ResponseEntity(emailNames, HttpStatus.OK);

        return  responseEntity;
    }
}
