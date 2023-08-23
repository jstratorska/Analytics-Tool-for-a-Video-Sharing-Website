package edu.aubg.analyticsyoutube;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("api")
@Data
public class ApplicationProperties {
    private String redirectUrl;
    private String codeRequestUrl;
    private String tokenUrl;
    private String baseUrl;
}
