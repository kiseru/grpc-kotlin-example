package ru.grpc.simple.proto.util;

import java.util.HashMap;
import java.util.Map;

public class DataUtil {
    private static UserPassword userPassword;
    private static Map<String, String> usersMap = new HashMap<>();
    private static final String CORRECT_PASSWORD = "qwe123";

    static {
        userPassword = new UserPassword(CORRECT_PASSWORD.toCharArray(), usersMap);
    }

    public static Map<String, String> getUsersMap() {
        return userPassword.usersMap;
    }

    public static String getCorrectPassword() {
        return CORRECT_PASSWORD;
    }

    private record UserPassword(char[] password, Map<String, String> usersMap) {}
}
