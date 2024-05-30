package se.norrwing.itslek4demo.model;


import jakarta.persistence.*;
import org.jboss.aerogear.security.otp.api.Base32;

import java.io.Serializable;

@Entity
@Table(name="users")
public class User implements Serializable {

    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String secret;
    private String role;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.secret = Base32.random();
        this.role = "USER";
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
