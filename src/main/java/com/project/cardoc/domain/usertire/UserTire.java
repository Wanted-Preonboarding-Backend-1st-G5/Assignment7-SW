package com.project.cardoc.domain.usertire;

import com.project.cardoc.domain.BaseTime;
import com.project.cardoc.domain.tire.Tire;
import com.project.cardoc.domain.user.User;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserTire extends BaseTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "tire_id")
  private Tire tire;

}
