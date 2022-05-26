package ru.homecredit.mysql2j;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JiraResponse {
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
