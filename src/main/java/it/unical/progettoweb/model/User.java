package it.unical.progettoweb.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String username;
    private String password;
    private String email;
    private Date birthDate;
    private int id;
}
