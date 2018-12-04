package com.class539.class539project2.Service;

import com.class539.class539project2.Configuration.BigQueryConfiguration;
import com.class539.class539project2.Service.Util.GoogleCredentialsUtility;
import com.google.cloud.bigquery.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BigQueryService {

    private final BigQueryConfiguration properties = new BigQueryConfiguration();

    public List<Map<String, Object>> querySQL(String sql) throws Exception {
        BigQuery bigQuery = BigQueryOptions.newBuilder().setProjectId(properties.getProjectId()).setCredentials(GoogleCredentialsUtility.getCreds(properties.getCredentialsPath(), properties.getCredientalsName())).build().getService();

        final QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(sql).setUseLegacySql(false).build();

        final UUID reqeustId = UUID.randomUUID();
        final JobId jobId = JobId.of(reqeustId.toString());
        Job queryJob = bigQuery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        queryJob = queryJob.waitFor();

        // Check for errors
        if (queryJob == null) {
            throw new Exception("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            // You can also look at queryJob.getStatus().getExecutionErrors() for all
            // errors, not just the latest one.
            throw new Exception(queryJob.getStatus().getError().toString());
        }

        final TableResult result = queryJob.getQueryResults();

        if(result.getSchema() == null && result.getTotalRows() == 0) return new ArrayList<>();
        //build list of fields in table results expected from query schema
        final List<String> fieldList = result.getSchema().getFields().stream()
                .map(Field::getName)
                .collect(Collectors.toList());
        return convertTableResultToListOfMap(result, fieldList);
    }

    private List<Map<String, Object>> convertTableResultToListOfMap(TableResult result, List<String> fields) {
        final List<Map<String, Object>> queryList = new ArrayList<>();
        for(FieldValueList row:result.iterateAll()) {
            Map<String, Object> curMap = new HashMap<>();
            for(String field:fields) {
                curMap.put(field, row.get(field).getStringValue());
            }
            queryList.add(curMap);
        }
        return queryList;
    }
}
