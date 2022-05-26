package ru.homecredit.mysql2j;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sql2jService {
    public static void main(String[] args) {
        Logger logger;
        String uri = "jdbc:mysql://localhost:3306/autos";
        String user = "ignat";
        String password = "pass";
        ResultSet rs = null;

        System.out.println("I'm alife!");

        new LoggerUtils();
        logger = LoggerUtils.getLogger();
        logger.log(Level.INFO, logger.getName() + " started log..." +
                " initializing MySQLConnector");
        MySQLConnector mc = new MySQLConnector(uri, user, password);

        mc.receiveOptionsFromDB("profile", "first_name");
        mc.close();

        try {
            String optionToAdd = "";
            URL url = new URL(
                    "http://localhost:2990/jira/rest/cfoptchange/1.0/options/" +
                            "?field_key=customfield_10000&proj_key=TES&new_opt=" +
                            optionToAdd);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            logger.log(Level.INFO, "connected");
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setRequestProperty("accept", "application/json");
            // This line makes the request
            InputStream responseStream = con.getInputStream();
            logger.log(Level.INFO, "sent request");
            // Manually converting the response body InputStream to JiraResPonse using Jackson
            ObjectMapper mapper = new ObjectMapper();
            JiraResponse jiraResponse = mapper.readValue(responseStream
                    , JiraResponse.class);
            // Finally we have the response
            logger.log(Level.INFO, "option " + optionToAdd + " appended = " +
                    jiraResponse.result);
            for (String option : jiraResponse.fieldOptions) {
                System.out.println(option);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "got error " + e.getMessage());
        }
    }
}
