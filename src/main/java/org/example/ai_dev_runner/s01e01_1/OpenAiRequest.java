package org.example.ai_dev_runner.s01e01_1;

import java.util.List;

public record OpenAiRequest(String model, List<MessageResponse> messages) {

    public static OpenAiRequest sendUserMessageWithGpt4(String message) {
        return new OpenAiRequest("gpt-4", List.of(new MessageResponse("user", message)));
    }
}
