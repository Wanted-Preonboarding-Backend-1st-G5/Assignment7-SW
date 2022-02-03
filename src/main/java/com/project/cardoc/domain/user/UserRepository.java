package com.project.cardoc.domain.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByName(String name);

  boolean existsByName(String name);

}
