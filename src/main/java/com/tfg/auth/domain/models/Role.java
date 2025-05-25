package com.tfg.auth.domain.models;

import lombok.*;

import java.util.EnumSet;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class Role {

    @NonNull
    private final String name;

    @Builder.Default
    private Set<Capability> capabilities = EnumSet.noneOf(Capability.class);

    public boolean hasCapability(Capability cap) {
        return capabilities.contains(cap);
    }

    public void addCapability(Capability cap) {
        if (cap != null) {
            capabilities.add(cap);
        }
    }

    public void removeCapability(Capability cap) {
        capabilities.remove(cap);
    }

    public void clearCapabilities() {
        capabilities.clear();
    }

    public void setCapabilities(Set<Capability> newCapabilities) {
        if (newCapabilities.isEmpty()) {
            clearCapabilities();
            return;
        }
        this.capabilities = EnumSet.copyOf(newCapabilities);
    }
}
