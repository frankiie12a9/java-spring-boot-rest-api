package com.github.frankiie.springboot.domain.user.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.github.frankiie.springboot.domain.mail.model.Addressable;

public class UpdateUserProps implements Addressable {

    @NotEmpty(message = "{user.name.not-empty}")
    private String name;

    @NotEmpty(message = "{user.email.not-empty}")
    @Email(message = "{user.email.is-valid}")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
