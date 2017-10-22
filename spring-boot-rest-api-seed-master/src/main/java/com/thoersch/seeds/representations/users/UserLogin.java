package com.thoersch.seeds.representations.users;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

public class UserLogin {
    @Length(max = 255)
    @NotNull
    private String emailAddress;

    @Length(max = 100)
    @NotNull
    private String password;


    public UserLogin() {

    }

    public UserLogin(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

}
