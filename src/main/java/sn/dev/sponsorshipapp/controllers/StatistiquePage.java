package sn.dev.sponsorshipapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sn.dev.sponsorshipapp.DBConnexion;
import sn.dev.sponsorshipapp.entities.Utilisateur;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StatistiquePage implements Initializable {

    @FXML
    private TableColumn<Utilisateur, String> candidat;

    @FXML
    private TableColumn<Utilisateur, Integer> nbpars;

    @FXML
    private TableView<Utilisateur> statParrainages;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(getStats());
        loadTablePar();
    }
    private DBConnexion db=new DBConnexion();
    public ObservableList<Utilisateur>getStats(){
        ObservableList<Utilisateur> can =  FXCollections.observableArrayList();
        String sql = "SELECT CONCAT(UPPER(prenom),' ',UPPER(nom)) AS CANDIDAT,COUNT(*) AS NOMBRE_PARRAINS FROM parrainer,user WHERE parrainer.candidat=user.id GROUP BY user.id;";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Utilisateur u=new Utilisateur(rs.getInt("NOMBRE_PARRAINS"),rs.getString("CANDIDAT"));
                can.add(u);
            }
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return can;
    }
    public void loadTablePar() {
        ObservableList<Utilisateur> l= getStats();
        statParrainages.setItems(l);
        candidat.setCellValueFactory(new PropertyValueFactory<Utilisateur,String>("nom"));
        nbpars.setCellValueFactory(new PropertyValueFactory<Utilisateur,Integer>("id"));
    }
}
