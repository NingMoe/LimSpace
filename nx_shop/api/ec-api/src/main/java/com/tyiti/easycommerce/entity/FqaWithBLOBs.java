package com.tyiti.easycommerce.entity;
/**
 * @author wangqi
 * @date 2016-4-22 上午10:10:28
 * @description 常见问题
 */
public class FqaWithBLOBs extends Fqa {
    private String question;

    private String answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }
}