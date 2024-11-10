package com.liro.zuulservice.clients;

import com.liro.zuulservice.dtos.ClinicaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@FeignClient(name = "clinics-service")
public interface FeignClinicClient {

    @GetMapping(value = "/clinics/findAllByVet")
    ResponseEntity<List<ClinicaResponse>> getUsersByClinicId(@RequestHeader(name = "Authorization") String token);
}
