package com.schoolapp.enums;

//package com.Crmemp.enums;

//import com.Crmemp.enums.Role;

public enum Role {

    COMPANY_OWNER(0),
    DIRECTOR(1),
    MANAGER(2),
    SUPERVISOR(3),
    SENIOR_STAFF(4),
    STAFF(5),
    JUNIOR_STAFF(6),
    INTERN(7),
    USER(99);

    private final int level;

    Role(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static Role fromSpringRole(String role) {
        return Role.valueOf(role.replace("ROLE_", ""));
    }
}
