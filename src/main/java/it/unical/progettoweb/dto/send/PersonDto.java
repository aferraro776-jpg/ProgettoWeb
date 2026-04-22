package it.unical.progettoweb.dto.send;

public class PersonDto {

    private int id;
    private String name;
    private String surname;
    private String email;
    // non c'è la password, nel dto è rischioso

    public PersonDto() {}

    public PersonDto(int id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}