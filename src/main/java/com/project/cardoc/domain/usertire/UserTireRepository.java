package com.project.cardoc.domain.usertire;

import com.project.cardoc.domain.tire.Tire;
import com.project.cardoc.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTireRepository extends JpaRepository<UserTire, Long> {

    boolean existsByUserAndTire(User user, Tire tire);

    List<UserTire> findAllByUser(User user);
}
