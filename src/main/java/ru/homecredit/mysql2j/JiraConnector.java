package ru.homecredit.mysql2j;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JiraConnector {

    Logger logger;
    // dbConnector mySQLConnector;

    JiraConnector() {
        logger = LoggerUtils.getLogger();
        logger.log(Level.INFO, logger.getName() + " started log..." +
                " JiraConnector constructor running");
        // mySQLConnector = new dbConnector(uri, user, password);
    }

    public JiraResponse addJiraFieldOption(String proj_key,
                                              String field_key,
                                              String new_opt) {
        JiraResponse jiraResponse = null;
        try {
            URL url = new URL(
                    "http://localhost:2990/jira/rest/cfoptchange/1.0/options/?"
                            + "field_key=" + field_key
                            + "&proj_key=" + proj_key
                            + "&new_opt=" + new_opt);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            logger.log(Level.INFO, "connected");
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setRequestProperty("accept", "application/json");
            // This line makes the request
            InputStream responseStream = con.getInputStream();
            logger.log(Level.INFO, "sent request");
            // converting the response body InputStream to JiraResponse using Jackson
            ObjectMapper mapper = new ObjectMapper();
            jiraResponse = mapper.readValue(responseStream
                    , JiraResponse.class);
            // Finally we have the response
            logger.log(Level.INFO, "option " + new_opt + " appended = " +
                    jiraResponse.result);
            StringBuilder optionsString = new StringBuilder();
            for (String option : jiraResponse.fieldOptions) {
                optionsString.append(option).append(", ");
            }
            optionsString.setLength(optionsString.length() - 2);
            logger.log(Level.INFO, "returned options are: " +
                    optionsString.toString());
        } catch (Exception e) {
            logger.log(Level.WARNING, "got error " + e.getMessage());
        }
        return jiraResponse;
    }

    public JiraResponse getJiraFieldOptions (String proj_key,
                                             String field_key) {
        return addJiraFieldOption(proj_key, field_key, "");
    }


    public static void main(String[] args) {
        String field_key = "customfield_10000";
        String proj_key = "TES";
        String new_opt = "new11";

        new LoggerUtils();
        JiraConnector jiraConnector = new JiraConnector();

        String[] jiraOptions = jiraConnector
                .getJiraFieldOptions(proj_key, field_key).fieldOptions;
        for (String jiraOption : jiraOptions) {
            System.out.println(jiraOption);
        }
        jiraConnector.addJiraFieldOption(proj_key, field_key, new_opt);

        LoggerUtils.closeLogFiles();
    }
}
