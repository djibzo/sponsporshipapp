package sn.dev.sponsorshipapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sn.dev.sponsorshipapp.DBConnexion;
import sn.dev.sponsorshipapp.entities.Role;
import sn.dev.sponsorshipapp.entities.Utilisateur;
import sn.dev.sponsorshipapp.tools.Notification;
import sn.dev.sponsorshipapp.tools.Outils;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminActController  implements Initializable{

    @FXML
    private TableColumn<Utilisateur, String> nomCol;
    @FXML
    private TableColumn<Utilisateur, Integer> idCol;

    @FXML
    private TableColumn<Utilisateur, String> prenomCol;

    @FXML
    private TableColumn<Utilisateur, Role> profilCol;

    private DBConnexion db= new DBConnexion();
    private int id;
    @FXML
    private TableView<Utilisateur> usersTb;



    @FXML
    void getData(MouseEvent event) {
        Utilisateur u =usersTb.getSelectionModel().getSelectedItem();
        id=u.getId();

    }

    public ObservableList<Utilisateur> getUtilisateurs(){
        ObservableList<Utilisateur> utilisateurs= FXCollections.observableArrayList();
        String sql= "SELECT * FROM user JOIN role ON user.profil = role.id WHERE profil != 1 AND activated=0";
        try {
            db.initPrepar(sql);
            ResultSet rs= db.executeSelect();
            while (rs.next()){
                Utilisateur u=new Utilisateur();
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                Role role = new Role();
                role.setName(rs.getString("name"));
                u.setProfil(role);
                u.setId(rs.getInt("id"));
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
        profilCol.setCellValueFactory(new PropertyValueFactory<Utilisateur, Role>("profil"));
        idCol.setCellValueFactory(new PropertyValueFactory<Utilisateur, Integer>("id"));
    }

    @FXML
    void activer(ActionEvent event) {
        String sql=" UPDATE user SET activated= ? WHERE id = ? ";
        try {

            db.initPrepar(sql);
            db.getPstm().setInt(1,1);
            db.getPstm().setInt(2,id);
            db.executeMaj();
            db.closeConnection();
            Notification.NotifSuccess("Utilisateur Activé"," Vous avez activé l'utilisateur selectionné");
            loadTable();
        }catch (SQLException e){
            throw new RuntimeException("ERREUR");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }
    @FXML
    void retour(ActionEvent event) throws IOException {
        Outils.load(event,"Bienvenue","/pages/admin.fxml");//admin

    }
}
