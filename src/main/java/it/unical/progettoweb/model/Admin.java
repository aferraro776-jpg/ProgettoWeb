package it.unical.progettoweb.model;

import lombok.Data;

@Data
public class Admin {
    private int id;
    private String name;
    private String surname;
    private String password;
    private String email;

}
