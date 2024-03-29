package com.laioffer.job.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQLTableCreation {



    public static void main(String[] args) {
        try {
          // step 1 Connect to MySQL
          System.out.println("Connecting to" + MySQLDBUtil.URL);
          Class.forName("com.mysql.cj.jdbc.Driver");
          Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);

          if (conn == null) {
              return;
          }

          System.out.println("successfully connected");

          // step 2 Drop tables if they exist
            Statement statement = conn.createStatement();
          String sql = "DROP TABLE IF EXISTS keywords";
          statement.executeUpdate(sql);

          sql = "DROP TABLE IF EXISTS history";
          statement.executeUpdate(sql);

          sql = "DROP TABLE IF EXISTS items";
          statement.executeUpdate(sql);

          sql = "DROP TABLE IF EXISTS users";
          statement.executeUpdate(sql);

            // step 3 create new tables

            sql = "CREATE TABLE items ("
                    + "item_id VARCHAR(255) NOT NULL,"
                    + "name VARCHAR(255),"
                    + "address VARCHAR(255),"
                    + "image_url VARCHAR(255),"
                    + "url VARCHAR(255),"
                    + "PRIMARY KEY (item_id)"
                    + ")";
            statement.executeUpdate(sql);

            sql = "CREATE TABLE users ("
                    + "user_id VARCHAR(255) NOT NULL,"
                    + "password VARCHAR(255) NOT NULL,"
                    + "first_name VARCHAR(255),"
                    + "last_name VARCHAR(255),"
                    + "PRIMARY KEY (user_id)"
                    + ")";
            statement.executeUpdate(sql);


            sql = "CREATE TABLE keywords ("
                    + "item_id VARCHAR(255) NOT NULL,"
                    + "keyword VARCHAR(255) NOT NULL,"
                    + "PRIMARY KEY (item_id, keyword),"
                    + "FOREIGN KEY (item_id) REFERENCES items(item_id)"
                    + ")";
            statement.executeUpdate(sql);


            sql = "CREATE TABLE history ("
                    + "user_id VARCHAR(255) NOT NULL,"
                    + "item_id VARCHAR(255) NOT NULL,"
                    + "last_favor_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "PRIMARY KEY (user_id, item_id),"
                    + "FOREIGN KEY (user_id) REFERENCES users(user_id),"
                    + "FOREIGN KEY (item_id) REFERENCES items(item_id)"
                    + ")";
            statement.executeUpdate(sql);



            // step 4 insert fake user for testing


            sql = "INSERT INTO users VALUES('1111', '3229c1097c00d497a0fd282d586be050', 'John', 'Smith')";
            statement.executeUpdate(sql);


            //
            conn.close();
          System.out.println("Import done successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
