package sn.dev.sponsorshipapp.repositories.role;

import sn.dev.sponsorshipapp.DBConnexion;
import sn.dev.sponsorshipapp.entities.Role;

import java.sql.ResultSet;

public class RoleImpl implements IRole{
    private DBConnexion db =new DBConnexion();
    private ResultSet rs;
    @Override
    public Role getRoleById(int id) {
        String sql="SELECT * FROM ROLE WHERE id=?";
        Role role=null;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1,id);
            rs= db.executeSelect();
            if (rs.next()){
                role=new Role(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("etat")
                        );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return role;
    }
}
