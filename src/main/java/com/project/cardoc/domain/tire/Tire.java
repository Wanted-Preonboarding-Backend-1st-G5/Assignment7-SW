package com.project.cardoc.domain.tire;

import com.project.cardoc.domain.BaseTime;
import com.project.cardoc.domain.usertire.UserTire;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<UserTire> users = new ArrayList<>();
}
