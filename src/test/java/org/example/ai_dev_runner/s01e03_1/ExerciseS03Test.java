package org.example.ai_dev_runner.s01e03_1;

import lombok.extern.slf4j.Slf4j;
import org.example.ai_dev_runner.BaseTest;
import org.example.ai_dev_runner.global.utils.UrlProvider;
import org.example.ai_dev_runner.global.should_be_hidden.BearerToken;
import org.example.ai_dev_runner.s01e01_1.OpenAiRequest;
import org.example.ai_dev_runner.s01e01_1.OpenAiResponse;
import org.example.ai_dev_runner.shared.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SuppressWarnings("all")
@SpringBootTest
class ExerciseS03Test extends BaseTest {

    @Test
    void shouldReturnCorrectFlag() {
        var getTestRequest = FileUtil.getFileContentAsObject("s01e03/test.json", GetTestRequest.class);

        var solvedTestData = getTestRequest.testData()
                                           .stream()
                                           .map(TestDataRecord::getWithCorrectAnswer)
                                           .map(this::getWithCorrectMiniTestAnswer)
                                           .toList();
        var solvedTest = getTestRequest.getWithTestData(solvedTestData);


    }

    private TestDataRecord getWithCorrectMiniTestAnswer(TestDataRecord testDataRecord) {
        if (!testDataRecord.containsMiniTest()) {
            return testDataRecord;
        }

        var miniTestQuestion = testDataRecord.test()
                                             .q();
        var finalQuestion = "Answer as short as possible: " + miniTestQuestion;

        var openAiRequest = OpenAiRequest.sendUserMessageWithGpt4(finalQuestion);

        var aiResponse = restClient.post()
                                   .uri(UrlProvider.OPEN_AI_URL)
                                   .header("Authorization", BearerToken.VALUE)
                                   .body(openAiRequest)
                                   .retrieve()
                                   .body(OpenAiResponse.class);

        var aiResponseContent = aiResponse.choices()
                                          .getFirst()
                                          .message()
                                          .content();

        log.info(String.valueOf(aiResponse.choices().getFirst()));

        return testDataRecord.withCorrectMiniTestAnswer(aiResponseContent);
    }

}