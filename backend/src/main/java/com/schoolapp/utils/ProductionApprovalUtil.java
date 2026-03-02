package com.schoolapp.utils;

import com.schoolapp.enums.Role;

//package com.Crmemp.utils;

//import com.Crmemp.enums.Role;

public class ProductionApprovalUtil {

    public static boolean canApprove(String stage, Role role) {

        if ("NONE".equals(stage)) {
            return role == Role.DIRECTOR;
        }

        if (stage.startsWith("L")) {
            int current = Integer.parseInt(stage.substring(1));
            return role.getLevel() == current + 1;
        }

        return false;
    }

    public static String nextStage(String stage) {

        if ("NONE".equals(stage))
            return "L1";

        if (stage.startsWith("L")) {
            int lvl = Integer.parseInt(stage.substring(1));
            return lvl == 7 ? "FINAL" : "L" + (lvl + 1);
        }

        return "FINAL";
    }
}
