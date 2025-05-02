package com.tfg.auth.domain.models;

/**
 * Enum representing system-level permissions.
 */
public enum Capability {
    // System capabilities
    VIEW_CAPABILITIES("Allow system capabilities view"),

    // User capabilities
    CREATE_USER("Allow user creation"),
    UPDATE_USER("Allow user update"),
    DELETE_USER("Allow user deletion"),
    VIEW_USERS("Allow single user or list view"),

    // Role capabilities
    CREATE_ROLE("Allow role creation"),
    UPDATE_ROLE("Allow role update"),
    DELETE_ROLE("Allow role deletion"),
    VIEW_ROLES("Allow single role or list view"),

    // Package capabilities
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
