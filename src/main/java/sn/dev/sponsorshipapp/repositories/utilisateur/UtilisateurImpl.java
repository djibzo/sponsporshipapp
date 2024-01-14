package sn.dev.sponsorshipapp.repositories.utilisateur;

import sn.dev.sponsorshipapp.DBConnexion;
import sn.dev.sponsorshipapp.entities.Role;
import sn.dev.sponsorshipapp.entities.Utilisateur;
import sn.dev.sponsorshipapp.repositories.role.IRole;
import sn.dev.sponsorshipapp.repositories.role.RoleImpl;

import java.sql.ResultSet;

public class UtilisateurImpl implements IUtilisateur {
    private DBConnexion db =new DBConnexion();
    private ResultSet rs;
    @Override
    public Utilisateur seConnecter(String login, String password) {
        String sql ="SELECT * from user where login=? AND password=?";
        Utilisateur user=null;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1,login);
            db.getPstm().setString(2,password);
            rs =db.executeSelect();
            if (rs.next()){
                user=new Utilisateur();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setActived(rs.getInt("activated"));
                IRole iRole=new RoleImpl();
                int idRole=rs.getInt("profil");
                Role role=iRole.getRoleById(idRole);
                user.setProfil(role);
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  user;
    }
}
