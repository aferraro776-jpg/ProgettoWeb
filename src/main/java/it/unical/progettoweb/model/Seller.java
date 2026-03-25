package it.unical.progettoweb.model;

import lombok.Data;
import java.util.Date;

@Data
public class Seller {
    private int id;
    private String vatNumber;
    private String name;
    private String surname;
    private String email;
    private Date birthDate;
    private String password;
}
