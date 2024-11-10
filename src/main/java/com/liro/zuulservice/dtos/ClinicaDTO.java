package com.liro.zuulservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ClinicaDTO {

    private String name;
    private ClinicaType clinicaType;
    private String address;
    private String phoneNumber;
}
