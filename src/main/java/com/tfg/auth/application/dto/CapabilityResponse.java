package com.tfg.auth.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CapabilityResponse {
    private String name;
    private String description;
}
