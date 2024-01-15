package sn.dev.sponsorshipapp.controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sn.dev.sponsorshipapp.DBConnexion;
import sn.dev.sponsorshipapp.entities.Utilisateur;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EleParCan implements Initializable {

    @FXML
    private TableView<Utilisateur> candidatTb;

    @FXML
    private TableColumn<Utilisateur, Integer> idCol;

    @FXML
    private TableColumn<Utilisateur, String> nomCol;

    @FXML
    private Button parrainerBtn;

    @FXML
    private TableColumn<Utilisateur, String> prenomCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }
    private DBConnexion db=new DBConnexion();
    public ObservableList<Utilisateur> getUsers(){
        ObservableList<Utilisateur> users= FXCollections.observableArrayList();
        String sql="SELECT * FROM user WHERE profil=2 ORDER BY nom ASC";
        try {
            db.initPrepar(sql);
            ResultSet rs=db.executeSelect();
            while (rs.next()){
                Utilisateur user=new Utilisateur();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setActived(rs.getInt("activated"));
                users.add(user);
            }
            db.closeConnection();
        }catch (SQLException e){
            throw new RuntimeException();
        }
        return users;
    }
    public void loadTable(){
        ObservableList<Utilisateur> liste=getUsers();
        candidatTb.setItems(liste);
        idCol.setCellValueFactory(new PropertyValueFactory<Utilisateur,Integer>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<Utilisateur,String>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<Utilisateur,String>("prenom"));
    }
}
