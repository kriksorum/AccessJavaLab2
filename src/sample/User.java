package sample;

public class User {
    private Integer iduser;
    private String username;
    private String password;
    private String dateban;
    private String role;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getDateban() {
        return dateban;
    }

    public void setDateban(String dateban) {
        this.dateban = dateban;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(Integer iduser, String username, String password, String dateban, String role) {
        this.iduser = iduser;
        this.username = username;
        this.password = password;
        this.dateban = dateban;
        this.role = role;
    }

    public User(){

    }

    public Integer getIduser() {
        return iduser;
    }

    public void setIduser(Integer iduser) {
        this.iduser = iduser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
