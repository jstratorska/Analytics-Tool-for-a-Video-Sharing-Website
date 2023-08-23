package edu.aubg.analyticsyoutube.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
public class User {
    @Id
    @NotEmpty
    private String username;

    @Size(min = 6)
    private String password;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiresIn;
}
