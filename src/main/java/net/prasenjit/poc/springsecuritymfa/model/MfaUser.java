package net.prasenjit.poc.springsecuritymfa.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class MfaUser {
    @Id
    private String username;
    private String password;
    private String mfaSecret;
}
