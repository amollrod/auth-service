package com.tfg.auth.domain.models;

/**
 * Enum representing the capabilities of the system.
 * Each capability corresponds to a specific action that can be performed.
 * <p> 
 * The available capabilities are:
 * <ul>
 *     <li>CREATE_PACKAGE: Ability to create a new package.</li>
 *     <li>FIND_PACKAGE: Ability to find an existing package.</li>
 *     <li>VIEW_HISTORY: Ability to view the history of a package.</li>
 *     <li>SEARCH_PACKAGES: Ability to search for packages based on various criteria.</li>
 *     <li>UPDATE_STATUS: Ability to update the status of a package.</li>
 * </ul>
 */
public enum Capability {
    CREATE_PACKAGE,
    FIND_PACKAGE,
    VIEW_HISTORY,
    SEARCH_PACKAGES,
    UPDATE_STATUS
}
