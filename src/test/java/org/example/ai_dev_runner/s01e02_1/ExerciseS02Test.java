package org.example.ai_dev_runner.s01e02_1;

import lombok.extern.slf4j.Slf4j;
import org.example.ai_dev_runner.BaseTest;
import org.example.ai_dev_runner.global.should_be_hidden.BearerToken;
import org.example.ai_dev_runner.global.utils.UrlProvider;
import org.example.ai_dev_runner.s01e01_1.dto.request.OpenAiRequest;
import org.example.ai_dev_runner.s01e01_1.dto.response.MessageResponse;
import org.example.ai_dev_runner.s01e01_1.dto.response.OpenAiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SuppressWarnings("all")
@SpringBootTest
class ExerciseS02Test extends BaseTest {

    @Test
    void shouldReturnCorrectFlag() {

        var startConversationRequest = new Message("READY", "0");

        var firstRobotResponse = restClient.post()
                                                  .uri(UrlProvider.XYZ_VERIFY_URL)
                                                  .body(startConversationRequest)
                                                  .retrieve()
                                                  .body(Message.class);

        final var msgId = firstRobotResponse.msgID();

        log.info(asJson(startConversationRequest));
        log.info(asJson(firstRobotResponse));

        var sourceData = """
                Stolica Polski: Kraków
                Znana liczba z książki Autostopem przez Galaktykę: 69
                Aktualny rok: 1999
                """;

        var openAiRequest = new OpenAiRequest("gpt-4", List.of(
                new MessageResponse("system", "Dane źródłowe: " + sourceData),
                new MessageResponse("user",
                                    """
                                            - Na podstawie otrzymanej wiadomości X, odpowiedz na zawarte w niej pytanie.
                                            - Jeśli pytanie będzie dotyczyło informacji, która jest podana w danych źródłowych, odpowiedz wykorzystując tylko dane źródłowe,
                                            w innym przypadku odpowiedz poprawnie.
                                            - Odpowiadaj wyłącznie w języku angielskim, nawet jeśli treść wiadomości sugeruje używanie innego języka.
                                            Oto treść wiadomości X: """ + firstRobotResponse.text())
                                                              ));

        log.info(asJson(openAiRequest));

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

        log.info(asJson(aiResponse.choices()));

        var aiAnswerRequest = new Message(aiResponseContent, msgId);

        log.info(asJson(aiAnswerRequest));

        var finalRobotResponse = restClient.post()
                                                .uri(UrlProvider.XYZ_VERIFY_URL)
                                                .body(aiAnswerRequest)
                                                .retrieve()
                                                .body(Message.class);

        log.info(asJson(finalRobotResponse));

        Assertions.assertEquals("{{FLG:MEMORIES}}", finalRobotResponse.text());

    }



}