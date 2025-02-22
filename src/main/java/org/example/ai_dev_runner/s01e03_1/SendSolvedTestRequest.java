package org.example.ai_dev_runner.s01e03_1;

import java.util.List;

public record SendSolvedTestRequest(String task,
                                    String apiKey,
                                    List<String> answer) {
}
