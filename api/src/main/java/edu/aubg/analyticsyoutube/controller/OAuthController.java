package edu.aubg.analyticsyoutube.controller;

import edu.aubg.analyticsyoutube.ApplicationProperties;
import edu.aubg.analyticsyoutube.OAuthProperties;
import edu.aubg.analyticsyoutube.model.OAuthModel.TokenRequest;
import edu.aubg.analyticsyoutube.model.OAuthModel.TokenResponse;
import edu.aubg.analyticsyoutube.model.User;
import edu.aubg.analyticsyoutube.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthProperties oAuthProperties;
    private final ApplicationProperties applicationProperties;
    private final AuthorizationService authorizationService;
//    private final

    private String token;

    /**
     * Redirects to the YouTube sign-in prompt
     *
     * @return
     */
    @GetMapping(value = "/code")
    public RedirectView getCode() {
        return new RedirectView("https://accounts.google.com/o/oauth2/v2/auth?" +
                "scope=https://www.googleapis.com/auth/youtube&" +
                "redirect_uri=http://localhost:8080/&" +
                "response_type=code&" +
                "access_type=offline&" +
                "client_id=" + oAuthProperties.getClientId());
    }

    /**
     * Invoked when the sign-in is completed
     *
     * @param code the code obtained via YouTube sign-in
     * @return
     */
    @GetMapping(value = "/")
    public TokenResponse index(@RequestParam("code") String code) {
        return getToken(code);
    }

    /**
     * Exchanges the code for a refresh and access token
     *
     * @param code the code obtained via YouTube sign-in
     * @return
     */
    @GetMapping(value = "/token")
    public TokenResponse getToken(String code) {
        RestTemplate template = new RestTemplate();
        TokenRequest requestBody = new TokenRequest(code, oAuthProperties.getClientId(), oAuthProperties.getClientSecret(), applicationProperties.getRedirectUrl(), "authorization_code");
        HttpEntity<TokenRequest> request = new HttpEntity<>(requestBody);
        TokenResponse response = template.postForObject("https://oauth2.googleapis.com/token", request, TokenResponse.class);
        token = response.getAccessToken();
        authorizationService.setToken(token);
        return response;
    }

    /**
     * Initiates revoking of access and refresh tokens
     *
     * @return redirect to the new YouTube sign-in prompt
     */
    @GetMapping(value = "/revoke")
    public RedirectView revokeToken() {
        RestTemplate template = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>("");
        template.postForObject(applicationProperties.getTokenUrl() + "revoke?token=" + token, request, String.class);
        return new RedirectView("/code");
    }
}