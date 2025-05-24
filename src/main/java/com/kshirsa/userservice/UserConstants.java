package com.kshirsa.userservice;

public class UserConstants {

    public static final String JWT_BEARER_TEXT= "Bearer ";
    public static final String JWT_HEADER_NAME= "Authorization";

    public static final int OTP_VALIDITY= 10; // in minutes

    /**
     *  DB cleanup process in Cron.
     *  Current Time : 6am everyday
     */
    public static final String FIXED_DELAY= "0 0 6 * * ?";

    public static final String EMAIL_REGEX = "[A-Za-z0-9-\\.]+@[A-Za-z0-9]+\\.[A-Za-z]+";

    public static final String PHONE_NUMBER_REGEX = "^(?=[6-9])\\d{10}$";

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String TRUE_CLIENT_IP = "True-Client-IP";
    public static final String CF_CONNECTING_IP = "CF-Connecting-IP";

    public static final String DEVICE_ID = "device-id";
}
