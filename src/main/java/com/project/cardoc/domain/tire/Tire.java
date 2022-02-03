package com.project.cardoc.domain.tire;

import com.project.cardoc.domain.BaseTime;
import com.project.cardoc.domain.usertire.UserTire;
import java.util.ArrayList;
import java.util.List;
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
public class Tire extends BaseTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int width;

  private int aspectRatio;

  private int wheelDiameter;

  @OneToMany(mappedBy = "tire")
  @Builder.Default
  private final List<UserTire> users = new ArrayList<>();

}
