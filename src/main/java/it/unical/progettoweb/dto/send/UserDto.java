package it.unical.progettoweb.dto.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
@Data
public class UserDto {

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private Date birthDate;
    private String authProvider;

    //la passwoed entra dal frontend ma non viene mai restituita nelle risposte json
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public UserDto(Integer id, String name, String surname, String email, Date birthDate, String authProvider) {
            this.id = id;
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.birthDate = birthDate;
            this.authProvider = authProvider;
    }
}