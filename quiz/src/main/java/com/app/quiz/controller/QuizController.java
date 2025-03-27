package com.app.quiz.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.app.quiz.model.Quiz;
import com.app.quiz.model.Question;
import com.app.quiz.service.QuizUserDetailsService;
import com.app.quiz.service.QuestionsService;

@Controller
public class QuizController {

    private final QuizUserDetailsService userDetailsService;
    private final QuestionsService questionsService;
    private final AuthenticationManager authenticationManager;

    public QuizController(QuizUserDetailsService userDetailsService,
            AuthenticationManager authenticationManager,
            QuestionsService questionsService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.questionsService = questionsService;
    }

    private String getUserRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");
    }


    @GetMapping("/home")
    public String homepage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String role = getUserRole(authentication); // Ensure roles are properly assigned

        model.addAttribute("username", username);
        List<Quiz> quizzes = questionsService.getQuizzesList();
        model.addAttribute("quizzes", quizzes);

        if ("ROLE_ADMIN".equals(role)) {
            return "QuizList"; // Admin view
        } else {
            return "Quiz"; // User view
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role) {

        if (!userDetailsService.registerUser(username, email, password, role)) {
            return "redirect:/register?error";
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/login?success";
    }

    @PostMapping("/addQuiz")
    public String addQuiz(@ModelAttribute Quiz quiz,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if ("ROLE_ADMIN".equals(getUserRole(authentication))) {
            quiz.setId(questionsService.getNextId());
            questionsService.addQuiz(quiz);
            redirectAttributes.addFlashAttribute("success", "Quiz added successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Permission denied");
        }
        return "redirect:/home";
    }

    @PostMapping("/editQuiz/{id}")
    public String editQuiz(@PathVariable("id") int id,
            @ModelAttribute("quiz") Quiz quiz,
            RedirectAttributes redirectAttributes) {

        quiz.setId(id);
        questionsService.editQuiz(quiz);
        redirectAttributes.addFlashAttribute("success", "Quiz updated successfully!");
        return "redirect:/home";
    }

    @GetMapping("/deleteQuiz/{id}")
    public String deleteQuiz(@PathVariable("id") int id,
            RedirectAttributes redirectAttributes) {

        try {
            questionsService.deleteQuiz(id);
            redirectAttributes.addFlashAttribute("success", "Quiz deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Delete failed: " + e.getMessage());
        }
        return "redirect:/home";
    }

    @PostMapping("/submitQuiz")
    public String evaluateQuiz(@RequestParam Map<String, String> allParams, Model model) {
        int correctAnswers = 0;
        List<String> userAnswers = new ArrayList<>();
        List<Quiz> quizzes = questionsService.getQuizzesList();
        int questionIndex = 0;

        for (Quiz quiz : quizzes) {
            for (Question question : quiz.getQuestions()) {
                String userAnswer = allParams.get("answer" + questionIndex);
                userAnswers.add(userAnswer);
                if (question.getCorrectAnswer().equalsIgnoreCase(userAnswer)) {
                    correctAnswers++;
                }
                questionIndex++;
            }
        }

        model.addAttribute("totalQuestions", questionIndex);
        model.addAttribute("quizzes", quizzes);
        model.addAttribute("userAnswers", userAnswers);
        model.addAttribute("correctAnswers", correctAnswers);

        return "result";
    }
}