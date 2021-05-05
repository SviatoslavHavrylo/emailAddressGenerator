package com.emailAddressGenerator.service;

import com.emailAddressGenerator.model.EmailName;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
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

    private List<EmailName> names = new LinkedList();

    private static final String MAIL_DOMAIN = "@revevol.it";

    public void saveEmailNames(MultipartFile multipartFile) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .forEach(this::handleName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("");
        }
    }

    private void handleName(String name) {
        if (StringUtils.hasText(name)) {
            names.add(createEmailName(name));
            System.out.println(name);
        }
    }

    private EmailName createEmailName(String name) {
        EmailName emailNameDuplicate = names.stream()
                .filter(emailName -> emailName.getName().equals(name))
                .sorted(Comparator.comparing(EmailName::getDuplicateNumber))
                .findFirst()
                .orElse(null);

        if (emailNameDuplicate == null) {
            return new EmailName(name);
        } else {
            int duplicateNumber = emailNameDuplicate.getDuplicateNumber();

            return new EmailName(name, duplicateNumber++);
        }
    }

    public List<String> getEmailAddresses() {
        return names.stream()
                .sorted()
                .map(name -> concatDomain(name))
                .collect(Collectors.toList());
    }

    private String concatDomain(EmailName emailName) {
        int duplicateNumber = emailName.getDuplicateNumber();

        return emailName.getName()
                .concat(duplicateNumber == 0 ? "" : String.valueOf(duplicateNumber))
                .concat(MAIL_DOMAIN);
    }
}
