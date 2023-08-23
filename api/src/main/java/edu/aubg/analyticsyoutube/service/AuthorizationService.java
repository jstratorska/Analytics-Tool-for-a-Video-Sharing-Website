package edu.aubg.analyticsyoutube.service;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class AuthorizationService {
    private String token;
}
