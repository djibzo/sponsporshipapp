package sn.dev.sponsorshipapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sn.dev.sponsorshipapp.entities.Utilisateur;
import sn.dev.sponsorshipapp.repositories.utilisateur.IUtilisateur;
import sn.dev.sponsorshipapp.repositories.utilisateur.UtilisateurImpl;
import sn.dev.sponsorshipapp.tools.Notification;
import sn.dev.sponsorshipapp.tools.Outils;

public class LoginController {
    @FXML
    private TextField loginTfd;
    @FXML
    private PasswordField passwordTfd;
    private IUtilisateur userDao=new UtilisateurImpl();
    @FXML
    void login(ActionEvent event) {
        String login = loginTfd.getText();
        String password = passwordTfd.getText();
        if (login.trim().isEmpty() || password.trim().isEmpty()) {
            Notification.NotifError("Erreur ! ","Tous les champs sont obligatoires");
        } else {
            Utilisateur user=userDao.seConnecter(login,password);
            if (user!=null){
                try {
                    Notification.NotifSuccess("Succès !","Connexion reussie !");
                    if (user.getProfil().getName().equalsIgnoreCase("RO_ADMIN"))
                        Outils.load(event,"Bienvenue","/pages/admin.fxml");//admin
                    else if (user.getProfil().getName().equalsIgnoreCase("RO_ELECTEUR"))
                        Outils.load(event,"Parrainage d'un candidat", "/pages/eleParCan.fxml");//candidat
                    else
                        Outils.load(event,"Bienvenue", "/pages/admin.fxml");//electeur


                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                Notification.NotifError("Erreur !","Login et/ou mot de passe incorrect(s)");
            }
        }
    }
}
