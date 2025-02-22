package org.example.ai_dev_runner.s01e03_1;

public record TestDataRecord(String question,
                             int answer,
                             MiniTestRecord test) {

    public boolean containsMiniTest() {
        return test != null;
    }

    public boolean isCorrectAnswer() {
        return answer == getCorrectAnswer();
    }

    public TestDataRecord getWithCorrectAnswer() {
        return new TestDataRecord(question, getCorrectAnswer(), test);
    }

    public int getCorrectAnswer() {
        var split = question.split(" ");
        var x = Integer.parseInt(split[0]);
        var y = Integer.parseInt(split[2]);
        return x + y;
    }

    public TestDataRecord withCorrectMiniTestAnswer(String aiResponseContent) {
        return new TestDataRecord(question, answer, new MiniTestRecord(test.q(), aiResponseContent));
    }
}
