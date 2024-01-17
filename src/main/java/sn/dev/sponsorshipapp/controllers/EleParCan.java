package sn.dev.sponsorshipapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sn.dev.sponsorshipapp.DBConnexion;
import sn.dev.sponsorshipapp.entities.UserModel;
import sn.dev.sponsorshipapp.entities.Utilisateur;
import sn.dev.sponsorshipapp.tools.Notification;

import javax.xml.transform.Source;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    private int idCandidat;
    private int idUserConn;
    private DBConnexion db = new DBConnexion();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //System.out.println(getNb());
        loadTable();
        idUserConn = UserModel.getUserId();
        if (aParrainer()) {
            parrainerBtn.setDisable(true);
            Notification.NotifWarning("Remarque","Vous avez déjà parrainé un candidat");
        }
    }
    public ObservableList<Utilisateur> getUsers() {
        ObservableList<Utilisateur> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM user u " +
                "LEFT JOIN (SELECT candidat, COUNT(*) AS nombreParrains FROM parrainer GROUP BY candidat) p ON u.id = p.candidat" +
                " WHERE u.profil = 2 AND (p.nombreParrains IS NULL OR p.nombreParrains < 7) " +
                "ORDER BY u.nom ASC;";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Utilisateur user = new Utilisateur();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setActived(rs.getInt("activated"));
                users.add(user);
            }
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return users;
    }
    public List<Integer> getParrainage() {//selectionne la liste des electeur
        List<Integer> pars = new ArrayList<>();
        String sql = "SELECT electeur FROM parrainer";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                pars.add(rs.getInt("electeur"));
            }
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return pars;
    }
    public boolean aParrainer(){
        for (int i = 0; i < getParrainage().size(); i++)
            if (getParrainage().get(i).equals(idUserConn)) {
                return true;
            }
        return false;
    }

    public void loadTable() {
        ObservableList<Utilisateur> liste = getUsers();
        candidatTb.setItems(liste);
        idCol.setCellValueFactory(new PropertyValueFactory<Utilisateur, Integer>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("prenom"));
    }
    @FXML
    public void getData(javafx.scene.input.MouseEvent mouseEvent) {
        Utilisateur user = candidatTb.getSelectionModel().getSelectedItem();
        idCandidat = user.getId();
    }
    @FXML
    void parrainer(ActionEvent event) {
        String sql = "INSERT INTO parrainer(id,electeur,candidat) values(null, ?, ?) ";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, idUserConn);
            db.getPstm().setInt(2, idCandidat);
            db.executeMaj();
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        Notification.NotifSuccess("Parrainage validé !","Votre parrainage a été enregistré avec succes !");
        parrainerBtn.setDisable(true);
    }
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
    ///
//    public List<Integer> getNb() {//selectionne la liste des electeur
//        List<Integer> pars = new ArrayList<>();
//        String sql = " SELECT user.id AS ID,COUNT(*) AS NOMBRE_PARRAINS FROM parrainer,user WHERE parrainer.candidat=user.id GROUP BY user.id";
//        try {
//            db.initPrepar(sql);
//            ResultSet rs = db.executeSelect();
//            while (rs.next()) {
//                Utilisateur u=new Utilisateur(rs.getInt("NOMBRE_PARRAINS"),rs.getString("CANDIDAT"));
//                pars.add(rs.getInt("ID"));
//                pars.add(rs.getInt("NOMBRE_PARRAINS"));
//            }
//            db.closeConnection();
//        } catch (SQLException e) {
//            throw new RuntimeException();
//        }
//        return pars;
//    }
}
