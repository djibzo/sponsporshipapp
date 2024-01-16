package sn.dev.sponsorshipapp.entities;

import lombok.Getter;

public class UserModel {
    @Getter
    private static int userId;

    public static void setUserId(int newUserId) {
        userId = newUserId;
    }
}
