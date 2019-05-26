package uy1.info430.etiq.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Utilisateur {
    @DatabaseField(generatedId = true)
    private int idUtilisateur;
    @DatabaseField
    private String username;
    @DatabaseField
    private String email;
    @DatabaseField
    private String password;
    @DatabaseField
    private String confPassword;


    public Utilisateur() {
    }

    public Utilisateur(String username, String email, String password, String confPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confPassword = confPassword;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }
}
