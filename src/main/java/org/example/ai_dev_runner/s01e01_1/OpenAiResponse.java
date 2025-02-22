package org.example.ai_dev_runner.s01e01_1;

import java.util.List;

public record OpenAiResponse(List<ChoiceResponse> choices) {
}
