package com.clubhub.validation;

import com.clubhub.dto.UserDTO;

/**
 * Used for validating the form data from register form
 */
public class RegisterValidator {

    public static final String ERROR_FIRST_NAME_NULL_FIELD = "Please Provide A First Name";
    public static final String ERROR_FIRST_NAME_TOO_LONG = "First Name Must Be Less Than 16 Characters";
    public static final String ERROR_FIRST_NAME_NON_ALPHA = "First Name Must Be Alpha Character Or '-'";
    public static final String ERROR_LAST_NAME_NULL_FIELD = "Please Provide A Last Name";
    public static final String ERROR_LAST_NAME_TOO_LONG = "Last Name Must Be Less Than 16 Characters";
    public static final String ERROR_LAST_NAME_NON_ALPHA = "Last Name Must Be Alpha Character Or '-'";
    public static final String ERROR_PASSWORD_NULL_FIELD = "Please Provide A Password";
    public static final String ERROR_PASSWORD_TOO_LONG = "Password Must Be Less Than 32 Characters";
    public static final String ERROR_PASSWORD_TOO_SHORT = "Password Must Be 8 or More Characters Characters";
    public static final String ERROR_EMAIL_NULL_FIELD = "Please Provide A Email";
    public static final String ERROR_EMAIL_INVALID_FORMAT = "Email Address Must Be In The Form 'jane@doe.nz'";

    private UserDTO userDTO;

    public RegisterValidator() {

    }

    /**
     * Calls all validation methods
     *
     * @param userDTO the object that carry's all the form data
     * @return if any errors occurred
     */
    public Boolean validateFormData(UserDTO userDTO) {

        this.userDTO = userDTO;

        // Check firstName
        validateFirstName();

        // Check lastName
        validateLastName();

        // Check password
        validatePassword();

        // Check email
        validateEmail();

        return !userDTO.response.isEmpty();
    }

    /**
     * Validate the form field for first name
     */
    private void validateFirstName() {
        String field = userDTO.firstName;

        String error =  validateNameFields(field, ERROR_FIRST_NAME_NULL_FIELD, ERROR_FIRST_NAME_TOO_LONG, ERROR_FIRST_NAME_NON_ALPHA);

        if (error != null) {
            userDTO.response.put("firstNameError", error);
        }
    }

    /**
     * Validate the form field for last name
     */
    private void validateLastName() {
        String field = userDTO.lastName;

        String error = validateNameFields(field, ERROR_LAST_NAME_NULL_FIELD, ERROR_LAST_NAME_TOO_LONG, ERROR_LAST_NAME_NON_ALPHA);

        if (error != null) {
            userDTO.response.put("lastNameError", error);
        }
    }

    /**
     * Validate name fields
     * @param field either first or last name
     * @param errorLastNameNullField error for null name
     * @param errorLastNameTooLong error for name too long
     * @param errorLastNameNonAlpha error for non alpha name
     */
    private String validateNameFields(String field, String errorLastNameNullField, String errorLastNameTooLong, String errorLastNameNonAlpha) {
        if (field == null) {
            return errorLastNameNullField;
        }
        if (field.isEmpty() || field.trim().isEmpty()) {
            return errorLastNameNullField;
        }
        if (field.length() > 16) {
            return errorLastNameTooLong;
        }
        if (!field.matches("^[\\p{L}-]+$")) {
            return errorLastNameNonAlpha;
        }

        return null;
    }

    /**
     * Validates the users password on the register form
     */
    private void validatePassword() {
        String field = userDTO.password;
        String error;

        if (field == null) {
            error = ERROR_PASSWORD_NULL_FIELD;
        } else if (field.isEmpty() || field.trim().isEmpty()) {
            error = ERROR_PASSWORD_NULL_FIELD;
        } else if (field.length() > 32) {
            error = ERROR_PASSWORD_TOO_LONG;
        } else if (field.length() < 8) {
            error = ERROR_PASSWORD_TOO_SHORT;
        } else {
            return;
        }

        userDTO.response.put("passwordError", error);
    }

    /**
     * Validates the email password on the register form
     */
    private void validateEmail() {
        String field = userDTO.email;
        String error;

        if (field == null) {
            error = ERROR_EMAIL_NULL_FIELD;
        } else if (field.isEmpty() || field.trim().isEmpty()) {
            error = ERROR_EMAIL_NULL_FIELD;
        } else if (!field.matches("^[a-zA-Z0-9][a-zA-Z0-9._%+-]*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+$")) {
            error = ERROR_EMAIL_INVALID_FORMAT;
        } else {
            return;
        }

        userDTO.response.put("error", error);
    }
}
