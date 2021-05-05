package com.emailAddressGenerator.model;

public class EmailName {
    private String name;

    private int duplicateNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuplicateNumber() {
        return duplicateNumber;
    }

    public void setDuplicateNumber(int duplicateNumber) {
        this.duplicateNumber = duplicateNumber;
    }

    public EmailName(String name) {
        this.name = name;
    }

    public EmailName(String name, int duplicateNumber) {
        this.name = name;
        this.duplicateNumber = duplicateNumber;
    }
}
