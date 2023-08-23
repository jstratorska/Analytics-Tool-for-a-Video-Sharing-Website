package edu.aubg.analyticsyoutube;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("oauth.properties")
@ConfigurationProperties("oauth")
@Getter
@Setter
public class OAuthProperties {

    private String clientId;
    private String clientSecret;
}
