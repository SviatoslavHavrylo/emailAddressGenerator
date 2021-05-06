package com.emailAddressGenerator.controller;

import com.emailAddressGenerator.service.EmailAddressService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    private static final Logger log = Logger.getLogger(EmailAddressController.class);

    private final EmailAddressService emailAddressService;

    @Autowired
    public EmailAddressController(EmailAddressService emailAddressService) {
        this.emailAddressService = emailAddressService;
    }

    @PostMapping
    public ResponseEntity<String> readNames(@RequestParam("file") MultipartFile multipartFile) {
        log.info("post file called");

        if (null == multipartFile.getOriginalFilename()) {
            log.error("multipartFile is incorrect");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            emailAddressService.saveEmailNames(multipartFile);
            log.info("file loaded");

            return new ResponseEntity<>("Names recieved", HttpStatus.OK);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);

            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity getEmailAddress() {
        log.info("get emails called");
        List<String> emailNames = emailAddressService.getEmailAddresses();

        return new ResponseEntity(emailNames, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity cleanEmailAddress() {
        log.info("delete saved emails called");

        try {
            emailAddressService.cleanEmailAddress();

            return new ResponseEntity<>("Names deleted", HttpStatus.OK);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);

            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
