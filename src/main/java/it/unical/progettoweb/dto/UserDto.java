package it.unical.progettoweb.dto;

import java.util.Date;

public class UserDto {

    // Campi da Person (senza password: non va mai esposta)
    private int id;
    private String name;
    private String surname;
    private String email;

    // Campi specifici di User
    private Date birthDate;
    private String authProvider;

    public UserDto() {}

    public UserDto(int id, String name, String surname, String email,
                   Date birthDate, String authProvider) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.birthDate = birthDate;
        this.authProvider = authProvider;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public String getAuthProvider() { return authProvider; }
    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }
}