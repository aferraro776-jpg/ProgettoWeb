package it.unical.progettoweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SellerDto {

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String vatNumber;
    private Date birthDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public SellerDto() {}

    public SellerDto(Integer id, String name, String surname, String email,
                     String vatNumber, Date birthDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.vatNumber = vatNumber;
        this.birthDate = birthDate;
    }
}