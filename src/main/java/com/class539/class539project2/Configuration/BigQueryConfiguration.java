package com.class539.class539project2.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.springapp")
public class BigQueryConfiguration {
    private String credentialsPath = "/resources/credentials/";
    private String credientalsName = "cs539-c076eb05bc71.json";
    private String projectId = "alpine-surge-224001";

    public String getCredentialsPath() {
        return credentialsPath;
    }

    public String getCredientalsName() {
        return credientalsName;
    }

    public String getProjectId() {
        return projectId;
    }
}
