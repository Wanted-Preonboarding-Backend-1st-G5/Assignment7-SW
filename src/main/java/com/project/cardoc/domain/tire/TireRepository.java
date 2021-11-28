package com.project.cardoc.domain.tire;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TireRepository extends JpaRepository<Tire, Long> {

    Tire findByWidthAndAspectRatioAndWheelDiameter(int width, int aspectRatio, int wheelDiameter);

}
