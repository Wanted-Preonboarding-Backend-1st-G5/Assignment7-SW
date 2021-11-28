package com.project.cardoc.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UserTireRequest {

    @NotNull
    private String id;

    @NotNull
    private String trimId;

}
