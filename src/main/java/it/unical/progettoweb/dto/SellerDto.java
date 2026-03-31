package it.unical.progettoweb.dto;

import java.util.Date;

public class SellerDto {

    // Campi da Person (senza password)
    private int id;
    private String name;
    private String surname;
    private String email;

    // Campi specifici di Seller
    private String vatNumber;
    private Date birthDate;

    public SellerDto() {}

    public SellerDto(int id, String name, String surname, String email,
                     String vatNumber, Date birthDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.vatNumber = vatNumber;
        this.birthDate = birthDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getVatNumber() { return vatNumber; }
    public void setVatNumber(String vatNumber) { this.vatNumber = vatNumber; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
}