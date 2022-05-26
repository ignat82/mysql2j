package ru.homecredit.mysql2j;

import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    private Connection conn = null;
    private Logger logger;
    private Statement stmt;
    private String[] optionsFromDB;
/*
    public static class JiraResponse {
        public final String fieldKey;
        public final String newOption;
        public final String projectKey;
        public final String fieldName;
        public final String projectName;
        public final String fieldConfigName;
        public final String[] fieldOptions;
        public final String result;

        public JiraResponse(
                @JsonProperty("fieldKey") String fieldKey,
                @JsonProperty("newOption") String newOption,
                @JsonProperty("projectKey") String projectKey,
                @JsonProperty("fieldName") String fieldName,
                @JsonProperty("projectName") String projectName,
                @JsonProperty("fieldConfigName") String fieldConfigName,
                @JsonProperty("fieldOptions") String[] fieldOptions,
                @JsonProperty("result") String result)
        {
            this.fieldKey = fieldKey;
            this.newOption = newOption;
            this.projectKey = projectKey;
            this.fieldName = fieldName;
            this.projectName = projectName;
            this.fieldConfigName = fieldConfigName;
            this.fieldOptions = fieldOptions;
            this.result = result;
        }
    }
*/
    MySQLConnector(String uri, String user, String password) {
        logger = LoggerUtils.getLogger();
        try {
            logger.log(Level.INFO, "Establishing connect to DB with URI " + uri);
            conn = DriverManager.getConnection(uri, user, password);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException sqlEx) {
            // handle any errors
            logger.log(Level.WARNING, "SQLException: " + sqlEx.getMessage());
            logger.log(Level.WARNING, "SQLState: " + sqlEx.getSQLState());
            logger.log(Level.WARNING, "VendorError: " + sqlEx.getErrorCode());
        }
    }

    public void receiveOptionsFromDB(String table, String column) {
        LinkedList<String> optionsQueue = new LinkedList<>();
        ResultSet rs = null;

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
        while (!optionsQueue.isEmpty()) {
            optionsFromDB[i] = optionsQueue.pop();
            System.out.println(optionsFromDB[i++]);
        }
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

    }
}
