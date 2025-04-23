package com.tfg.auth.domain.models;

/**
 * Enum representing system-level permissions.
 */
public enum Capability {
    CREATE_PACKAGE("Allow package creation"),
    FIND_PACKAGE("Allow package search"),
    VIEW_HISTORY("Allow package history view"),
    SEARCH_PACKAGES("Allow package search"),
    UPDATE_STATUS("Allow package status update"),;

    private final String description;

    Capability(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
