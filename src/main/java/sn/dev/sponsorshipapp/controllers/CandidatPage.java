package sn.dev.sponsorshipapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sn.dev.sponsorshipapp.DBConnexion;
import sn.dev.sponsorshipapp.entities.Role;
import sn.dev.sponsorshipapp.entities.UserModel;
import sn.dev.sponsorshipapp.entities.Utilisateur;
import sn.dev.sponsorshipapp.tools.Outils;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CandidatPage implements Initializable {

    @FXML
    private TableColumn<Utilisateur, Integer> id;

    @FXML
    private TextField nbpars;

    @FXML
    private TableColumn<Utilisateur, String > nom;

    @FXML
    private TableView<Utilisateur> parrainsTb;

    @FXML
    private TableColumn<Utilisateur, String > prenom;
    private DBConnexion db = new DBConnexion();
    private int idUserConn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idUserConn = UserModel.getUserId();
        loadTablePar();
    }
    public List<Utilisateur> getParrains() {//selectionne la liste des parrains pour chaque candidat connecte
        ObservableList<Utilisateur> pars =  FXCollections.observableArrayList();
        String sql = "SELECT *  FROM user,parrainer WHERE user.id=parrainer.electeur AND parrainer.candidat= ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, idUserConn);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Utilisateur u=new Utilisateur();
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setId(rs.getInt("id"));
                pars.add(u);
            }
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return pars;
    }

    public int getNbParrains() {
        String sql = "SELECT COUNT(*) AS count FROM parrainer WHERE candidat = ?";
        int nb = 0;

        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, idUserConn);

            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                // Obtenir la valeur du COUNT(*) depuis la colonne nommée "count"
                nb = rs.getInt("count");
            }

            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nb;
    }

    public void loadTablePar() {
        ObservableList<Utilisateur> listeParrains = (ObservableList<Utilisateur>) getParrains();
        parrainsTb.setItems(listeParrains);
        id.setCellValueFactory(new PropertyValueFactory<Utilisateur, Integer>("id"));
        nom.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("prenom"));
        nbpars.setText(String.valueOf(getNbParrains()));
    }
    @FXML
    void DECONNECTER(ActionEvent event) throws IOException {
        Outils.load(event,"Connexion ", "/pages/login.fxml");
    }
}
