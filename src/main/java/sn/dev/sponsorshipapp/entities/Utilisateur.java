package sn.dev.sponsorshipapp.entities;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data
public class Utilisateur {
    private int id;
    private String nom, prenom, login, password;
    private int actived;
    private Role profil;
}