package sn.dev.sponsorshipapp.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sn.dev.sponsorshipapp.DBConnexion;
import sn.dev.sponsorshipapp.entities.Role;
import sn.dev.sponsorshipapp.entities.Utilisateur;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserController  implements Initializable {
    private DBConnexion db= new DBConnexion();
    @FXML
    private TextField NomTfd;

    @FXML
    private TextField PrenomTfd;

    @FXML
    private TextField ProfilTfd;

    @FXML
    private Button desactiverBtn;

    @FXML
    private Button enregistrerBtn;

    @FXML
    private TableColumn<Utilisateur, String> nomCol;

    @FXML
    private TableColumn<Utilisateur, String> prenomCol;

    @FXML
    private TableColumn<Utilisateur, Role> profilCol;

    @FXML
    private TableView<Utilisateur> usersTb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }
    public ObservableList<Utilisateur> getUtilisateurs(){
        ObservableList<Utilisateur> utilisateurs= FXCollections.observableArrayList();
        String sql= "SELECT * FROM user ";
        try {
            db.initPrepar(sql);
            ResultSet rs= db.executeSelect();
            while (rs.next()){
                Utilisateur u=new Utilisateur();
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
               // u.setProfil(rs.getInt("Profil"));
                utilisateurs.add(u);
            }
        }catch (SQLException e){
            throw new RuntimeException();
        }
        return  utilisateurs;
    }
    public void loadTable(){
        ObservableList<Utilisateur> liste=getUtilisateurs();
        usersTb.setItems(liste);
        nomCol.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("prenom"));
        //nomCol.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("nom"));
    }
}
