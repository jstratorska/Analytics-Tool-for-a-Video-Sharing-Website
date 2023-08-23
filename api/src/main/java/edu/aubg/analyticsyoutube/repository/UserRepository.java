package edu.aubg.analyticsyoutube.repository;

import edu.aubg.analyticsyoutube.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
}
