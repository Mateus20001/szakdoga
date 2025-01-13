package com.szakdoga.backend.auth.model;

import java.util.ArrayList;
import java.util.List;

public enum RoleValue {
    STUDENT,
    ADMIN,
    TEACHER;

    // Static method to convert a string to RoleValue
    public static RoleValue fromString(String str) {
        for (RoleValue role : RoleValue.values()) {
            if (role.name().equalsIgnoreCase(str)) {
                return role;
            }
        }
        return null;
    }

    // Static method to parse a list of strings into a list of RoleValue
    public static List<RoleValue> parseStringList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        List<RoleValue> roleValues = new ArrayList<>();
        for (String str : list) {
            RoleValue role = fromString(str);
            if (role != null) {
                roleValues.add(role);
            }
        }
        return roleValues;
    }
}
