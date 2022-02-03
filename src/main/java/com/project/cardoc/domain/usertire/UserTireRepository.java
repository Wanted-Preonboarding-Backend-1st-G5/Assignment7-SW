package com.project.cardoc.domain.usertire;

import com.project.cardoc.domain.tire.Tire;
import com.project.cardoc.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTireRepository extends JpaRepository<UserTire, Long> {

  boolean existsByUserAndTire(User user, Tire tire);

  List<UserTire> findAllByUser(User user);

}
