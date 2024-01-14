package sn.dev.sponsorshipapp.repositories.utilisateur;

import sn.dev.sponsorshipapp.entities.Utilisateur;

public interface IUtilisateur {
    public Utilisateur seConnecter(String login,String password);
}
