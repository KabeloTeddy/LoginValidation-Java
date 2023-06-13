package com.example.demo.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {
    //to check if the email format is legal or valid
    static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    @Override
    public boolean test(String s) {
        Matcher matcher = EMAIL_PATTERN.matcher(s);

        return matcher.matches();
    }
}
