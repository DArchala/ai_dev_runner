package org.example.ai_dev_runner.s01e01_1;

import lombok.extern.slf4j.Slf4j;
import org.example.ai_dev_runner.BaseTest;
import org.example.ai_dev_runner.global.should_be_hidden.BearerToken;
import org.example.ai_dev_runner.global.utils.UrlProvider;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SuppressWarnings("all")
@SpringBootTest
class ExerciseS01Test extends BaseTest {

    private static final String FORM_URL = "https://xyz.ag3nts.org";

    @Test
    void shouldReturnCorrectFlag() {
        var htmlContent = restClient.get()
                                    .uri(FORM_URL)
                                    .retrieve()
                                    .body(String.class);

        var rawQuestion = Jsoup.parse(htmlContent)
                               .getElementById("human-question")
                               .childNodes()
                               .getLast()
                               .toString()
                               .trim();

        var finalQuestion = "Odpowiedz jak najkrócej na pytanie, bez żadnych zbędnych znaków: " + rawQuestion;

        var openAiRequest = OpenAiRequest.sendUserMessageWithGpt4(finalQuestion);

        var response = restClient.post()
                                 .uri(UrlProvider.OPEN_AI_URL)
                                 .header("Authorization", BearerToken.VALUE)
                                 .body(openAiRequest)
                                 .retrieve()
                                 .body(OpenAiResponse.class);

        log.info("Pytanie pobrane z formularza: {}", rawQuestion);

        var openAiAnswer = response.choices()
                               .getFirst()
                               .message()
                               .content();

        var formData = new LinkedMultiValueMap<String, String>();
        formData.put("username", List.of("tester"));
        formData.put("password", List.of("574e112a"));
        formData.put("answer", List.of(openAiAnswer));

        log.info("Odpowiedź AI: {}", openAiAnswer);

        var secretResponse = restClient.post()
                                       .uri(FORM_URL)
                                       .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                       .body(formData)
                                       .retrieve()
                                       .body(String.class);

        var getSecretResponse = restClient.get()
                                          .uri(FORM_URL + Jsoup.parse(secretResponse)
                                                               .getElementsByTag("a")
                                                               .attr("href"))
                                          .retrieve()
                                          .body(String.class);

        var flag = Jsoup.parse(getSecretResponse)
                        .getElementsContainingOwnText("FLG")
                        .textNodes()
                        .getFirst()
                        .toString();

        log.info("Odnaleziona flaga: {}", flag);

        assertEquals("{{FLG:FIRMWARE}}", flag);
    }

}