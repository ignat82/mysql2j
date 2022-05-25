package ru.homecredit.mysql2j;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.logging.*;

/******************************************************************************
 * Classname - MySQLConnector
 * Version - 1.0.0
 *
 * DESCRIPTION
 * The code sample connects to the Oracle Database and creates a table 'todoitem'.
 * It inserts few records into this table and displays the list of tasks and task completion status.
 * Edit this file and update the connection URL along with the database username and password
 * that point to your database.
 * NOTES
 * Use JDK 1.8 and above
 *
 *  MODIFIED    (MM/DD/YY)
 * inushtaev    05/24/22 - Creation
 *****************************************************************************/

public class MySQLConnector {

    public static void main(String[] args) {
        String URI = "jdbc:mysql://localhost:3306/autos";
        String USER = "ignat";
        String PASSWORD = "pass";
        Connection conn = null;
        Statement stmt = null;
        String query;
        ResultSet rs = null;
        Logger logger;

        new LoggerUtils();
        logger = LoggerUtils.getLogger();
        logger.log(Level.INFO, logger.getName() + " started log");
        // initialize connection
        try {
            logger.log(Level.INFO, "Establishing connect to DB with URI " + URI);
            conn = DriverManager.getConnection(URI, USER, PASSWORD);
            // Do something with the Connection
            stmt = conn.createStatement();
            query = "SELECT * FROM profile";
            logger.log(Level.INFO, "Executing query \"" + query + "\"");
            rs = stmt.executeQuery(query);
            // Now do something with the ResultSet ....
            while (rs.next()) {
                logger.log(Level.INFO, "Got response");
                System.out.println(rs.getString("first_name"));
            }

        } catch (SQLException sqlEx) {
            // handle any errors
            logger.log(Level.WARNING, "Something goes wrong");
            logger.log(Level.WARNING, "SQLException: " + sqlEx.getMessage());
            System.out.println("SQLException: " + sqlEx.getMessage());
            logger.log(Level.WARNING, "SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            logger.log(Level.WARNING, "SQLException: " + sqlEx.getMessage());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
        }

        finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed
            logger.log(Level.INFO, "Releasing resources");
            if (rs != null) {
                try {
                    logger.log(Level.INFO, "Closing response");
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore
                rs = null;
            }

            if (stmt != null) {
                try {
                    logger.log(Level.INFO, "Closing Statement");
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore
                stmt = null;
            }
        }
    }
}
