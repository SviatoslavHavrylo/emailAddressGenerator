package com.emailAddressGenerator.service;

import com.emailAddressGenerator.model.EmailName;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailAddressService {

    private static final Logger LOG = Logger.getLogger(EmailAddressService.class);

    private List<EmailName> emailNames = new LinkedList();

    private static final String MAIL_DOMAIN = "@revevol.it";

    private static final String SAVED_NAMES_FILE = "saved_names.txt";

    @PostConstruct
    public void loadSavedNamesFromFile() {
        LOG.info("initial loading of saved files");
        try {
            File initialFile = new File(SAVED_NAMES_FILE);
            if (initialFile.exists()) {
                InputStream inputStream = new FileInputStream(initialFile);
                new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines()
                        .forEach(this::handleName);
            }
        } catch (IOException exception) {
            LOG.error(exception.getMessage(), exception);
        }
    }

    @PreDestroy
    public void saveNamesToFile() {
        try (FileWriter writer = new FileWriter(SAVED_NAMES_FILE)) {
            for (EmailName emailName : emailNames) {
                writer.write(concatDuplicationNumber(emailName).concat(System.lineSeparator()));
            }
            LOG.info("names saved to file");
        } catch (IOException exception) {
            LOG.error(exception.getMessage(), exception);
        }
    }

    public void saveEmailNames(MultipartFile multipartFile) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .forEach(this::handleName);
        } catch (IOException exception) {
            LOG.error(exception.getMessage(), exception);
            throw new RuntimeException("");
        }
    }

    private void handleName(String name) {
        if (StringUtils.hasText(name)) {
            emailNames.add(createEmailName(name));
        }
    }

    private EmailName createEmailName(String name) {
        String nameCorrected = name.replaceAll("\\s+", "");
        EmailName emailNameDuplicate = emailNames.stream()
                .filter(emailName -> emailName.getName().equals(nameCorrected))
                .sorted(Comparator.comparing(EmailName::getDuplicateNumber).reversed())
                .findFirst()
                .orElse(null);

        if (emailNameDuplicate == null) {
            return new EmailName(nameCorrected);
        } else {
            int duplicateNumber = emailNameDuplicate.getDuplicateNumber();

            return new EmailName(nameCorrected, ++duplicateNumber);
        }
    }

    public List<String> getEmailAddresses() {
        return emailNames.stream()
                .sorted(Comparator.comparing(EmailName::getName))
                .map(this::concatDuplicationNumberAndDomain)
                .collect(Collectors.toList());
    }

    private String concatDuplicationNumberAndDomain(EmailName emailName) {
        return concatDuplicationNumber(emailName)
                .concat(MAIL_DOMAIN);
    }

    private String concatDuplicationNumber(EmailName emailName) {
        int duplicateNumber = emailName.getDuplicateNumber();

        return emailName.getName()
                .concat(duplicateNumber == 0 ? "" : "_".concat(String.valueOf(duplicateNumber)));
    }

    public void cleanEmailAddress() {
        emailNames = new LinkedList();
        File initialFile = new File(SAVED_NAMES_FILE);
        if (initialFile.exists()) {
            if (!initialFile.delete()) {
                LOG.error("Can not delete saved file");
            }
        }
    }
}
