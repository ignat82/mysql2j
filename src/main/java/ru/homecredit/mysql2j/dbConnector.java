package ru.homecredit.mysql2j;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


/******************************************************************************
 * Classname - dbConnector
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

public class dbConnector {
    private Connection conn = null;
    private final Logger logger;
    private Statement stmt;

    dbConnector(String dbUri, String dbUser, String dbPassword) {
        logger = LoggerUtils.getLogger();
        logger.log(Level.INFO, "dbConnector constructor started");
        try {
            logger.log(Level.INFO, "Establishing connect to DB with URI " + dbUri);
            conn = DriverManager.getConnection(dbUri, dbUser, dbPassword);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException sqlEx) {
            // handle any errors
            logger.log(Level.WARNING, "SQLException: " + sqlEx.getMessage());
            logger.log(Level.WARNING, "SQLState: " + sqlEx.getSQLState());
            logger.log(Level.WARNING, "VendorError: " + sqlEx.getErrorCode());
        }
    }

    public String[] receiveOptionsFromDB(String table, String column) {
        LinkedList<String> optionsQueue = new LinkedList<>();
        ResultSet rs = null;
        String[] optionsFromDB;

        String query = "SELECT * FROM " + table;
        try {
            logger.log(Level.INFO, "Executing query \"" + query + "\"");
            rs = stmt.executeQuery(query);
            logger.log(Level.INFO, "Got response");
            while (rs.next()) {
                optionsQueue.add(rs.getString(column));
            }
            } catch (SQLException sqlEx) {
            logger.log(Level.WARNING, "SQLException: " + sqlEx.getMessage());
            logger.log(Level.WARNING, "SQLState: " + sqlEx.getSQLState());
            logger.log(Level.WARNING, "VendorError: " + sqlEx.getErrorCode());
        } finally {
            logger.log(Level.INFO, "Releasing resources");
            if (rs != null) {
                try {
                    logger.log(Level.INFO, "Closing Response");
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore
            }
        }
        optionsFromDB = new String[optionsQueue.size()];
        int i = 0;
        StringBuilder receivedOptions = new StringBuilder("Received options are: ");
        while (!optionsQueue.isEmpty()) {
            optionsFromDB[i] = optionsQueue.pop();
            receivedOptions.append(optionsFromDB[i++]).append(", ");
        }
        receivedOptions.setLength(receivedOptions.length() - 2);
        logger.log(Level.INFO, receivedOptions.toString());
        return Arrays.copyOf(optionsFromDB, optionsFromDB.length);
    }

    public void close() {
        if (stmt != null) {
            try {
                logger.log(Level.INFO, "Closing Statement");
                stmt.close();
                logger.log(Level.INFO, "Closing Connection");
                conn.close();
            } catch (SQLException sqlEx) { } // ignore
            stmt = null;
            conn = null;
        }
    }

    public static void main(String[] args) {
        String dbUri = "jdbc:mysql://localhost:3306/autos";
        String dbUser = "ignat";
        String dbPassword = "pass";
        String optionsTable = "options_table";
        String optionsColumn = "options";

        new LoggerUtils();
        dbConnector dbConnector = new dbConnector(dbUri, dbUser, dbPassword);
        String[] mySqlOptions = dbConnector
                .receiveOptionsFromDB(optionsTable, optionsColumn);
        for (String mySqlOption : mySqlOptions) {
            System.out.println(mySqlOption);
        }
        dbConnector.close();

        LoggerUtils.closeLogFiles();
    }
}
