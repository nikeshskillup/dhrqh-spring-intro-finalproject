package com.app.quiz.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private String questionText;
    private List<String> options;
    private String correctAnswer;

    // Default Constructor
    public Question() {
        this.options = new ArrayList<>();
    }

    // Constructor with all attributes
    public Question(int id, String questionText, List<String> options, String correctAnswer) {
        this.id = id;
        this.questionText = questionText;
        this.options = new ArrayList<>(options);
        this.correctAnswer = correctAnswer;
    }

    // Overloaded constructor (Without ID)
    public Question(String questionText, List<String> options, String correctAnswer) {
        this.questionText = questionText;
        this.options = new ArrayList<>(options);
        this.correctAnswer = correctAnswer;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = new ArrayList<>(options);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    // toString method (Excluding correctAnswer for security reasons)
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", options=" + options +
                '}';
    }
}
