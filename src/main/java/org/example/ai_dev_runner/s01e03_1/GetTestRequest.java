package org.example.ai_dev_runner.s01e03_1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetTestRequest(String apikey,
                             String description,
                             String copyright,
                             @JsonProperty("test-data")
                             List<TestDataRecord> testData) {

    public GetTestRequest getWithTestData(List<TestDataRecord> testDataNew) {
        return new GetTestRequest(apikey, description, copyright, testDataNew);
    }
}
