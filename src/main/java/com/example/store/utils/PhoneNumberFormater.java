package com.example.store.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberFormater {
    @Value("${sms.country-code}")
    private String countryCode;

    private static final String PHONE_NUMBER_REGEX = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";

    public String format(String phoneNumber) {
        if (!phoneNumber.replace(" ", "").matches(PHONE_NUMBER_REGEX))
            throw new IllegalArgumentException("Invalid Phone Number");

        String validPhoneNumber;
        if (phoneNumber.startsWith(countryCode))
            validPhoneNumber = phoneNumber;
        else if (phoneNumber.startsWith("0"))
            validPhoneNumber = phoneNumber.replaceFirst("0", countryCode);
        else
            throw new IllegalArgumentException("Unsupported country code");
        return validPhoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
