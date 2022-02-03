package com.project.cardoc.payload.response;

import com.project.cardoc.domain.tire.Tire;
import lombok.*;

@NoArgsConstructor
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TireResponse {

  private Long id;
  private int width;
  private int aspectRatio;
  private int wheelDiameter;

  public static TireResponse fromEntity(final Tire tire) {
    return TireResponse.builder()
        .id(tire.getId())
        .width(tire.getWidth())
        .aspectRatio(tire.getAspectRatio())
        .wheelDiameter(tire.getWheelDiameter())
        .build();
  }

}
