package sn.dev.sponsorshipapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sn.dev.sponsorshipapp.DBConnexion;
import sn.dev.sponsorshipapp.entities.Utilisateur;

import java.net.URL;
import java.util.ResourceBundle;

public class StatistiquePage implements Initializable {

    @FXML
    private TableColumn<Utilisateur, Integer> candidat;

    @FXML
    private TableColumn<Utilisateur, Integer> nbpars;

    @FXML
    private TableView<Utilisateur> statParrainages;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    private DBConnexion db=new DBConnexion();
    public void getStats(){

    }
}
