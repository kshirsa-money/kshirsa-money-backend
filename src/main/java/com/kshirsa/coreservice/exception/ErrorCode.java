package com.kshirsa.coreservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Authentication and Authorization Errors (6xx)
    USER_NOT_FOUND(404, 601, "User with this email ID does not exist."),
    REFRESH_TOKEN_NOT_FOUND(404, 602, "Refresh token not found. Please authenticate again."),
    REFRESH_TOKEN_EXPIRED(403, 603, "Refresh token has expired. Please re-authenticate."),
    JWT_EXPIRED(401, 604, "JWT has expired. Please log in again."),
    JWT_WRONG_SIGNATURE(401, 605, "JWT signature is invalid. Please provide a valid token."),
    UNKNOWN_DEVICE(401, 606, "Device ID mismatch. Access denied."),

    // User Input Validation Errors (7xx)
    INVALID_OTP(400, 701, "OTP is invalid or has expired."),
    INVALID_USER_ID(400, 702, "The provided user ID is not valid."),
    INVALID_MOBILE(400, 703, "The provided mobile number is in an invalid format."),
    INVALID_EMAIL(400, 704, "The provided email address is in invalid format."),
    NOT_BLANK(400, 705, "This field cannot be blank or null."),
    DISPOSABLE_EMAIL(400, 707, "Disposable email addresses are not allowed."),
    INVALID_EMAIL_DOMAIN(400, 708, "The email domain is invalid."),
    INVALID_CATEGORY(400, 709, "The provided category id is invalid."),
    INVALID_TRANSACTION_ID(400, 710, "The provided transaction ID is not valid."),
    MISSING_HEADER(400, 711, "The required header is missing."),

    // Business Logic and Entity Errors (8xx)
    EMAIL_ALREADY_EXISTS(422, 801, "Email ID is already in use. Please use a different email address."),
    LOAN_DETAILS_REQUIRED(400, 802, "Loan details are required for a loan transaction."),
    WRONG_TRANSACTION_TYPE(400, 803, "The transaction type is not valid for this category."),
    NO_LOAN_DETAILS(400, 804, "No loan details found for this transaction."),
    INVALID_LOAN_REPAYMENT_ID(400, 805, "The provided loan repayment ID is not valid."),
    CATEGORY_IN_USE(409, 806, "This category is still in use and cannot be deleted."),

    // Server or General Errors (9xx)
    GENERAL_EXCEPTION(500, 901, "An unexpected error occurred. Please try again later.");

    private final int httpStatusCode;
    private final int errorCode;
    private final String errorMessage;
}
