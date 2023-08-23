package edu.aubg.analyticsyoutube.service;

import edu.aubg.analyticsyoutube.model.User;
import edu.aubg.analyticsyoutube.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * Retrieves a user by their username
     *
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Saves a new or modified user
     *
     * @param user
     */
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Retrieves the username of the currently logged in user
     *
     * @return
     */
    public String findLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }
}
