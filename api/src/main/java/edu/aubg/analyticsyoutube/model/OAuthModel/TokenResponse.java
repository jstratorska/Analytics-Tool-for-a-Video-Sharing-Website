package edu.aubg.analyticsyoutube.model.OAuthModel;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
    private String scope;
    private String tokenType;
}
