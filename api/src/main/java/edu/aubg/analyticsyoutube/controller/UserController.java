package edu.aubg.analyticsyoutube.controller;

import edu.aubg.analyticsyoutube.model.User;
import edu.aubg.analyticsyoutube.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Registers a new user
     *
     * @param user the new user to be registered
     */
    @PostMapping(value = "/registration")
    public ResponseEntity<Boolean> signUp(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (!bindingResult.hasErrors() && userService.findByUsername(user.getUsername()) == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.save(user);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }
}
