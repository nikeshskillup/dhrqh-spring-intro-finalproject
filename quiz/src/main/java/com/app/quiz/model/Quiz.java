package com.app.quiz.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Quiz {
    private Integer id;
    private String questionText;
    private List<Question> questions; // Changed from options to List<Question>
    private String correctAnswer;

    // No-argument constructor (required for Thymeleaf binding)
    public Quiz() {
        this.questions = new ArrayList<>(); // Initialize as an empty list
    }

    // Constructor with parameters
    public Quiz(Integer id, String questionText, List<Question> questions, String correctAnswer) {
        this.id = id;
        this.questionText = questionText;
        this.questions = questions;
        this.correctAnswer = correctAnswer;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<Question> getQuestions() {
        return questions; // Return the list of questions
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return ("ID: " + id +
                "\nQuestion: " + questionText +
                "\nQuestions: " + questions +
                "\nCorrect Answer: " + correctAnswer);
    }
}
