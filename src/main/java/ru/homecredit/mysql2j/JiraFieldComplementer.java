package ru.homecredit.mysql2j;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JiraFieldComplementer {
    private final String uri;
    private final String dbUser;
    private final String dbPassword;
    private final Logger logger;

    JiraFieldComplementer(String uri, String dbUser, String dbPassword) {
        this.uri = uri;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        logger = LoggerUtils.getLogger();
        logger.log(Level.INFO, "constructed JiraFieldComplementer");
    }

    public void complementField(String optionsTable,
                                String optionsColumn,
                                String field_key,
                                String proj_key) {
        logger.log(Level.INFO, "starting to complement Jira field " + field_key
            + " from project " + proj_key + " by field options from column " +
                optionsColumn + " of table " + optionsTable);
        dbConnector dbConnector = new dbConnector(uri, dbUser, dbPassword);
        String[] mySqlOptions = dbConnector
                .receiveOptionsFromDB(optionsTable, optionsColumn);
        logger.log(Level.INFO, "closing dbConnector");
        dbConnector.close();

        JiraConnector jiraConnector = new JiraConnector();
        String[] jiraOptions = jiraConnector
                .getJiraFieldOptions(proj_key, field_key).fieldOptions;

        for (String mySqlOption : mySqlOptions) {
            if (!Arrays.asList(jiraOptions).contains(mySqlOption)) {
                logger.log(Level.INFO,"mySqlOption " + mySqlOption +
                        " is missing at jira - adding");
                jiraConnector.addJiraFieldOption(proj_key, field_key, mySqlOption);
            } else {
                logger.log(Level.INFO,"mySqlOption " + mySqlOption +
                        " is present at jira - skipping");
            }
        }
    }

    public static void main(String[] args) {
        String field_key = "customfield_10000";
        String proj_key = "TES";
        String uri = "jdbc:mysql://localhost:3306/autos";
        String user = "ignat";
        String password = "pass";
        String optionsTable = "options_table";
        String optionsColumn = "options";

        new LoggerUtils();

        JiraFieldComplementer jfc = new JiraFieldComplementer(uri, user, password);
        jfc.complementField(optionsTable, optionsColumn, field_key, proj_key);

        LoggerUtils.closeLogFiles();
    }
}
