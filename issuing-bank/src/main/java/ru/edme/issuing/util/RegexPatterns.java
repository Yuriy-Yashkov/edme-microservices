package ru.edme.issuing.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexPatterns {

    public static final String SIMPLE_EMAIL = "^.{1,150}@[a-z]+\\.[a-z]{2,3}$";
}
