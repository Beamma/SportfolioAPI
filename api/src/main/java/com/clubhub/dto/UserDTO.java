package com.clubhub.dto;

public class UserDTO {

    public Long id;

    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String hashedPassword;

    public Boolean errors = false;
    public String firstNameError;
    public String lastNameError;
    public String emailError;
    public String passwordError;

}
