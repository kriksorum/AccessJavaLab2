package sample.database;

import sample.DateTime;
import sample.User;
import sample.auditlog.Audit;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws SQLException, ClassNotFoundException {
        String connectionString = "jdbc:postgresql://" + dbHost + ":" +
                dbPort + "/" + dbName;
        Class.forName("org.postgresql.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public boolean signUpUser(User user) {
        boolean bool = false;
        String insert = "INSERT INTO " + Const.USER_TABLE + " (" +
                Const.USERS_USERNAME + "," + Const.USERS_PASSWORD +
                "," + Const.USERS_DATEBAN + "," + Const.USERS_ROLE + ") " +
                "VALUES(?,?,?,?)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, user.getUsername());
            prSt.setString(2, user.getPassword());
            prSt.setString(3, DateTime.currentDateToStr());
            prSt.setString(4, "user");

            prSt.executeUpdate();
            System.out.println("Пользователь " + user.getUsername() + " зарегистрировался в системе");
            Audit.writeFile(DateTime.currentDateToStr() + " Пользователь " + user.getUsername() + " зарегистрировался в системе");
            bool = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return bool;
    }

    public ResultSet getUser(User user) {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " +
                Const.USERS_USERNAME + "=? AND " + Const.USERS_PASSWORD + "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getUsername());
            prSt.setString(2, user.getPassword());

            resSet = prSt.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public boolean checkUsers(User user) {
        ResultSet resSet = null;

        boolean flag = false;
        String select = "SELECT * FROM " + Const.USER_TABLE;

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                if (user.getUsername().equals(resSet.getString(Const.USERS_USERNAME))) {
                    flag = true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void changePass(User user) {
        String update = "UPDATE " + Const.USER_TABLE + " SET " +
                Const.USERS_PASSWORD + "=? WHERE " +
                Const.USERS_USERNAME + "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.setString(1, user.getPassword());
            prSt.setString(2, user.getUsername());

            prSt.executeUpdate();
            System.out.println("Пользователь " + user.getUsername() + " изменил пароль");
            Audit.writeFile(DateTime.currentDateToStr() + " Пользователь " + user.getUsername() + " изменил пароль");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean checkBan(User user) {
        ResultSet resSet = null;
        boolean flag = false;

        String select = "SELECT * FROM " + Const.USER_TABLE
                + " WHERE " + Const.USERS_USERNAME + "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getUsername());

            resSet = prSt.executeQuery();
            String dateStr = "";
            while (resSet.next()) {
                dateStr = resSet.getString(Const.USERS_DATEBAN);
            }

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = formatter.parse(dateStr);
            if (date.getTime() > DateTime.currentDate()) {
                //System.out.println("blocked");
                flag = false;
            } else {
                //System.out.println("unblocked");
                flag = true;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void banUser(User user) {
        String update = "UPDATE " + Const.USER_TABLE + " SET " +
                Const.USERS_DATEBAN + "=? WHERE " +
                Const.USERS_USERNAME + "=?";

        Date banDate = new Date(System.currentTimeMillis() + 2 * 60 * 1000);
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //System.out.println("banDate " + dateFormat.format(banDate));

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.setString(1, dateFormat.format(banDate));
            prSt.setString(2, user.getUsername());

            prSt.executeUpdate();
            System.out.println("Пользователь " + user.getUsername() + " был заблокирован до " + dateFormat.format(banDate));
            Audit.writeFile(DateTime.currentDateToStr() + " Пользователь " + user.getUsername() + " был заблокирован до " + dateFormat.format(banDate));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void deleteUser(String username) {
        String delete = "DELETE FROM " + Const.USER_TABLE + " WHERE " +
                Const.USERS_USERNAME + "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.setString(1, username);
            prSt.executeUpdate();
            System.out.println("Пользователь " + username + " удален");
            Audit.writeFile(DateTime.currentDateToStr() + "Пользователь " + username + " удален");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String getUserRole(User user) {
        String roleStr = "user";
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE
                + " WHERE " + Const.USERS_USERNAME + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getUsername());

            resSet = prSt.executeQuery();

            while (resSet.next()) {
                roleStr = resSet.getString(Const.USERS_ROLE);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return roleStr;
    }

    public ArrayList getAllUsers() {
        ArrayList<String> strArr = new ArrayList<>();
        ResultSet resSet = null;
        String str;
        String select = "SELECT * FROM " + Const.USER_TABLE;

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                str = resSet.getString(Const.USERS_USERNAME) + " "
                        + resSet.getString(Const.USERS_PASSWORD) + " "
                        + resSet.getString(Const.USERS_DATEBAN) + " "
                        + resSet.getString(Const.USERS_ROLE);
                strArr.add(str);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return strArr;
    }

}



