package com.tfg.auth.application.services;

import com.tfg.auth.application.dto.CapabilityResponse;
import com.tfg.auth.application.mapper.CapabilityMapper;
import com.tfg.auth.domain.services.CapabilityDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CapabilityService {

    private final CapabilityDomainService capabilityDomainService;

    public List<CapabilityResponse> getAllCapabilities() {
        return capabilityDomainService.getAllCapabilities().stream()
                .map(CapabilityMapper::toResponse)
                .collect(Collectors.toList());
    }
}
