package com.liro.zuulservice.clients;

import com.liro.zuulservice.dtos.ClinicaResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicsService {

    private final FeignClinicClient feignClinicClient;

    public ClinicsService(FeignClinicClient feignClinicClient) {
        this.feignClinicClient = feignClinicClient;
    }

    @Cacheable(value = "clinicUsers", key = "#token")
    public List<ClinicaResponse> getUsersByClinicId(String token) {
        return feignClinicClient.getUsersByClinicId(token).getBody();
    }
}