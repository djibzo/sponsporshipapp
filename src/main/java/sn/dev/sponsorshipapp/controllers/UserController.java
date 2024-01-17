package sn.dev.sponsorshipapp.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sn.dev.sponsorshipapp.DBConnexion;
import sn.dev.sponsorshipapp.entities.Role;
import sn.dev.sponsorshipapp.entities.Utilisateur;
import sn.dev.sponsorshipapp.tools.Notification;
import sn.dev.sponsorshipapp.tools.Outils;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserController  implements Initializable {
    private DBConnexion db= new DBConnexion();
    @FXML
    private TextField NomTfd;;

    @FXML
    private TextField PrenomTfd;

    private  int id;

    @FXML
    private Button desactiverBtn;

    @FXML
    private Button enregistrerBtn;

    @FXML
    private Button activerBtn;

    @FXML
    private TableColumn<Utilisateur, String> nomCol;
    @FXML
    private TableColumn<Utilisateur, Integer> idCol;

    @FXML
    private TableColumn<Utilisateur, String> prenomCol;

    @FXML
    private TableColumn<Utilisateur, Role> profilCol;

    @FXML
    private TableView<Utilisateur> usersTb;

    @FXML
    private ComboBox<Role> profilCb;

    @FXML
    private MenuItem ActiveBT;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        loadComboBox();
    }
    private int getNombreDeCandidats() {
        String countSql = "SELECT COUNT(*) FROM user WHERE profil = 2 AND activated = 1"; // Assurez-vous que le profil pour ROLE_CANDIDAT est 2
        try {
            db.initPrepar(countSql);
            ResultSet resultSet = db.executeSelect();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERREUR lors de la récupération du nombre de candidats", e);
        }

        return 0; // En cas d'erreur, retourner 0
    }
    public void loadComboBox() {
        if (profilCb != null) {
            ObservableList<Role> roles = getRolesFromDatabase();
            if (getNombreDeCandidats() >= 5) {
                roles.removeIf(role -> "RO_CANDIDAT".equals(role.getName()));
            }

            profilCb.setItems(roles);
        } else {
            System.err.println("Le ComboBox (profilCb) est nul.");
        }
    }

    public ObservableList<Role> getRolesFromDatabase() {
        ObservableList<Role> roles = FXCollections.observableArrayList();
        String sql = "SELECT * FROM role WHERE id!=1";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setName(rs.getString("name"));
                roles.add(role);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return roles;
    }
    public ObservableList<Utilisateur> getUtilisateurs(){
        ObservableList<Utilisateur> utilisateurs= FXCollections.observableArrayList();
        String sql= "SELECT * FROM user JOIN role ON user.profil = role.id WHERE profil != 1 AND activated=1";
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

    private int getRoleIdByName(String roleName) {
        String sql = "SELECT id FROM role WHERE name = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, roleName);
            ResultSet rs = db.executeSelect();
            if (rs != null && rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération de l'ID du rôle depuis la base de données", e);
        }
        return -1;
    }


    @FXML
    void save(ActionEvent event) {
        String sql=" INSERT INTO user(nom, prenom, login, password, activated, profil) VALUES( ? , ? , ? , ? , ? , ? ) ";
        String selectedRoleName = profilCb.getValue().getName();
        int roleId = getRoleIdByName(selectedRoleName);
        try {

            db.initPrepar(sql);
            db.getPstm().setString(1,NomTfd.getText());
            db.getPstm().setString(2,PrenomTfd.getText());
            db.getPstm().setString(3,NomTfd.getText().toLowerCase()+PrenomTfd.getText()+"2024");
            db.getPstm().setString(4,"passer123");
            db.getPstm().setInt(5,1);
            db.getPstm().setInt(6,roleId);
            db.executeMaj();
            db.closeConnection();
            loadTable();
            clear();
        }catch (SQLException e){
            throw new RuntimeException("ERREUR");
        }
    }
    public void clear(){
        NomTfd.setText("");
        PrenomTfd.setText("");
        loadComboBox();

    }

    @FXML
    void desactiver(ActionEvent event) {
        String sql=" UPDATE user SET activated= ? WHERE id = ? ";
        try {

            db.initPrepar(sql);
            db.getPstm().setInt(1,0);
            db.getPstm().setInt(2,id);
            db.executeMaj();
            db.closeConnection();
            Notification.NotifSuccess("Utilisateur Désactivé"," Vous avez desactivé l'utilisateur selectionné");
        }catch (SQLException e){
            throw new RuntimeException("ERREUR");
        }
    }
    @FXML
     void getData(MouseEvent event) {
        Utilisateur u =usersTb.getSelectionModel().getSelectedItem();
        id=u.getId();

    }

    @FXML
    void Active(ActionEvent event) throws IOException {

            Outils.load(event,"Activer un utilisateur ", "/pages/ActiverUser.fxml");

    }

    @FXML
    void Stats(ActionEvent event) throws IOException {

        Outils.load(event,"Statistiques Candidats ", "/pages/statistiquePage.fxml");

    }



}
