package pi.service.util.sqlitedb;
/*
 * Copyright (C) 2020 evanl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;/**
 *
 * @author evanl
 */
public class SQLiteConnection {
    private static Connection connection = null;
    
    
    public static Connection getConnection() throws SQLException {
        if(connection==null){
            try {
                Class.forName("org.sqlite.JDBC");
                // String path_sfs = App.getSucursal().invoice_path_sunat;
                // connection = DriverManager.getConnection("jdbc:sqlite:"+path_sfs+"/bd/BDFacturador.db");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connection;
    }

    public static void test(String[] args) {
        
        try {
            // create a database connection
            
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("select * from person");
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
}
