package com.tfg.auth.application.controllers;

import com.tfg.auth.application.dto.CapabilityResponse;
import com.tfg.auth.application.services.CapabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/capabilities")
@RequiredArgsConstructor
public class CapabilityController {

    private final CapabilityService capabilityService;

    @PreAuthorize("hasAuthority('VIEW_CAPABILITIES')")
    @GetMapping
    public ResponseEntity<List<CapabilityResponse>> getAll() {
        return ResponseEntity.ok(capabilityService.getAllCapabilities());
    }
}
