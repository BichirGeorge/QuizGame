package quizgame;

import quizgame.dao.DatabaseConnection;
import quizgame.model.*;
import quizgame.model.Leaderboard;
import quizgame.service.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static Connection connection;
    private static UserService userService;
    private static TopicService topicService;
    private static QuestionService questionService;
    private static GameSessionService gameSessionService;
    private static PlayerService playerService;
    private static AchievementService achievementService;
    private static PlayerAchievementService playerAchievementService;

    public static void main(String[] args) {
        try {
            connection = DatabaseConnection.getConnection();

            userService = new UserService(connection);
            topicService = new TopicService(connection);
            questionService = new QuestionService(connection);
            gameSessionService = new GameSessionService(connection);
            playerService = new PlayerService(connection);
            achievementService = new AchievementService(connection);
            playerAchievementService = new PlayerAchievementService(connection);
            Scanner scanner = new Scanner(System.in);
            User loggedUser = null;

            while (true) {
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();

                    loggedUser = userService.loginUser(name, password);

                    if (loggedUser != null) {
                        AuditService.getInstance().logAction("User logged in: " + loggedUser.getName());
                        System.out.println("Welcome " + loggedUser.getName() + "!");
                        userMenu(scanner, loggedUser);
                    } else {
                        AuditService.getInstance().logAction("Failed login attempt for username: " + name);
                        System.out.println("Invalid login credentials.");
                    }
                } else if (choice == 2) {
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();
                    System.out.println("Select your role (1 for PLAYER, 2 for ADMIN): ");
                    int roleChoice = scanner.nextInt();
                    scanner.nextLine();

                    User.Role role = (roleChoice == 1) ? User.Role.PLAYER : User.Role.ADMIN;

                    User newUser = userService.registerUser(name, password, role);

                    if (newUser != null) {
                        AuditService.getInstance().logAction("User registered: " + newUser.getName() + " with role " + newUser.getRole());
                        System.out.println("Registration successful! Please login now.");
                    } else {
                        AuditService.getInstance().logAction("Failed registration attempt for username: " + name);
                        System.out.println("Registration failed.");
                    }
                } else if (choice == 3) {
                    break;
                }
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void userMenu(Scanner scanner, User loggedUser) {
        if (loggedUser.getRole() == User.Role.PLAYER) {
            PlayerAchievement firstLoginAchievement = new PlayerAchievement(loggedUser.getId(), 3, new java.sql.Date(System.currentTimeMillis()));
            boolean firstLoginAwarded = playerAchievementService.createPlayerAchievement(firstLoginAchievement);
            if (firstLoginAwarded) {
                AuditService.getInstance().logAction("User " + loggedUser.getName() + " earned achievement: First Login");
                System.out.println("Congratulations! You have earned the 'First Login' achievement.");
            }

            playerMenu(scanner, loggedUser);
        } else if (loggedUser.getRole() == User.Role.ADMIN) {
            adminMenu(scanner, loggedUser);
        }
    }

    private static void playerMenu(Scanner scanner, User loggedUser) {
        while (true) {
            System.out.println("\n1. Start Game");
            System.out.println("2. View Achievements");
            System.out.println("3. View Leaderboard");
            System.out.println("4. Logout");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    startGame(scanner, loggedUser);
                    break;
                case 2:
                    viewAchievements(loggedUser);
                    break;
                case 3:
                    viewLeaderboard();
                    break;
                case 4:
                    AuditService.getInstance().logAction("Logout user: " + loggedUser.getName());
                    System.out.println("Logged out successfully.");
                    return;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void adminMenu(Scanner scanner, User loggedAdmin) {
        while (true) {
            System.out.println("\n1. Add Topic");
            System.out.println("2. Add Question");
            System.out.println("3. Delete Question");
            System.out.println("4. View Leaderboard");
            System.out.println("5. Logout");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addTopic(scanner);
                    break;
                case 2:
                    addQuestion(scanner);
                    break;
                case 3:
                    deleteQuestion(scanner);
                    break;
                case 4:
                    viewLeaderboard();
                    break;
                case 5:
                    AuditService.getInstance().logAction("Logout admin: " + loggedAdmin.getName());
                    System.out.println("Logged out successfully.");
                    return;
                case 6:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void startGame(Scanner scanner, User loggedUser) {
        AuditService.getInstance().logAction("Game started by user: " + loggedUser.getName());
        System.out.println("\nSelect a topic:");

        var topics = topicService.getAllTopics();
        for (int i = 0; i < topics.size(); i++) {
            System.out.println((i + 1) + ". " + topics.get(i).getName());
            System.out.println("\t-" + topics.get(i).getDescription());
        }

        System.out.print("Enter the topic number: ");
        int topicChoice = scanner.nextInt();
        scanner.nextLine();

        if (topicChoice > 0 && topicChoice <= topics.size()) {
            var selectedTopic = topics.get(topicChoice - 1);
            System.out.println("You selected: " + selectedTopic.getName());
            AuditService.getInstance().logAction("User " + loggedUser.getName() + " selected topic: " + selectedTopic.getName());

            gameSessionService.createGameSession(loggedUser.getId(), selectedTopic.getId());
            AuditService.getInstance().logAction("Game session created for user: " + loggedUser.getName() + " on topic: " + selectedTopic.getName());

            try {
                var questions = questionService.getQuestionsByTopic(selectedTopic.getId());
                int score = 0;

                for (int i = 0; i < questions.size(); i++) {
                    System.out.println("Q" + (i + 1) + ": " + questions.get(i).getQuestionText());
                    System.out.println("1. " + questions.get(i).getOption1());
                    System.out.println("2. " + questions.get(i).getOption2());
                    System.out.println("3. " + questions.get(i).getOption3());
                    System.out.println("4. " + questions.get(i).getOption4());

                    System.out.print("Your answer (1-4): ");
                    int answer = scanner.nextInt();
                    scanner.nextLine();
                    AuditService.getInstance().logAction("User " + loggedUser.getName() + " answered question: " + questions.get(i).getQuestionText() + " with answer: " + answer);
                    if (answer == questions.get(i).getCorrectAnswerIndex()) {
                        score++;
                    }
                }

                playerService.updateScore(loggedUser.getId(), score);
                AuditService.getInstance().logAction("Score updated for user " + loggedUser.getName() + ": " + score + " points");


                PlayerAchievement quizParticipantAchievement = new PlayerAchievement(loggedUser.getId(), 2, new java.sql.Date(System.currentTimeMillis()));
                boolean quizParticipantAwarded = playerAchievementService.createPlayerAchievement(quizParticipantAchievement);
                if (quizParticipantAwarded) {
                    AuditService.getInstance().logAction("User " + loggedUser.getName() + " earned achievement: First Quiz");
                    System.out.println("Congratulations! You have earned the 'First Quiz' achievement.");
                }

                if (score == questions.size()) {
                    PlayerAchievement perfectScoreAchievement = new PlayerAchievement(loggedUser.getId(), 1, new java.sql.Date(System.currentTimeMillis()));
                    boolean perfectScoreAwarded = playerAchievementService.createPlayerAchievement(perfectScoreAchievement);
                    if (perfectScoreAwarded) {
                        AuditService.getInstance().logAction("User " + loggedUser.getName() + " earned achievement: Perfect Score");
                        System.out.println("Congratulations! You have earned the 'Perfect Score' achievement.");
                    }
                }


                System.out.println("Your score: " + score + "/" + questions.size());
            } catch (NoQuestionsInTopicException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please add questions to this topic before starting a quiz.");
            }
        } else {
            System.out.println("Invalid topic selected.");
        }
    }

    private static void viewAchievements(User loggedUser) {
        AuditService.getInstance().logAction("User " + loggedUser.getName() + " viewed achievements");
        try {
            var achievements = playerAchievementService.getPlayerAchievements(loggedUser.getId());
            System.out.println("\nYour achievements:");
            for (PlayerAchievement pa : achievements) {
                try {
                    Achievement achievement = achievementService.getAchievementById(pa.getAchievementId());
                    System.out.println(achievement.getName() + ": " + achievement.getDescription());
                } catch (NoAchievementsException e) {
                    System.out.println("Error retrieving achievement: " + e.getMessage());
                }
            }
        } catch (NoAchievementsException e) {
            System.out.println("\nYou don't have any achievements yet.");
        }
    }

    private static void viewLeaderboard() {
        AuditService.getInstance().logAction("Leaderboard viewed");
        Leaderboard.getInstance(playerService).displayLeaderboard();
    }

    private static void addTopic(Scanner scanner) {
        System.out.print("Enter topic name: ");
        String name = scanner.nextLine();
        System.out.print("Enter topic description: ");
        String description = scanner.nextLine();

        boolean success = topicService.createTopic(name, description);
        if (success) {
            AuditService.getInstance().logAction("Admin added topic: " + name);
            System.out.println("Topic added successfully!");
        } else {
            AuditService.getInstance().logAction("Admin failed to add topic: " + name);
            System.out.println("Failed to add topic.");
        }
    }

    private static void addQuestion(Scanner scanner) {
        System.out.println("Select topic to add question to:");
        var topics = topicService.getAllTopics();
        for (int i = 0; i < topics.size(); i++) {
            System.out.println((i + 1) + ". " + topics.get(i).getName());
            System.out.println("\t-" + topics.get(i).getDescription());
        }

        System.out.print("Enter the topic number: ");
        int topicChoice = scanner.nextInt();
        scanner.nextLine();

        if (topicChoice > 0 && topicChoice <= topics.size()) {
            System.out.print("Enter question text: ");
            String questionText = scanner.nextLine();
            System.out.print("Enter option 1: ");
            String option1 = scanner.nextLine();
            System.out.print("Enter option 2: ");
            String option2 = scanner.nextLine();
            System.out.print("Enter option 3: ");
            String option3 = scanner.nextLine();
            System.out.print("Enter option 4: ");
            String option4 = scanner.nextLine();
            System.out.print("Enter correct answer (1-4): ");
            int correctAnswerIndex = scanner.nextInt();
            scanner.nextLine();

            boolean success = questionService.createQuestion(topics.get(topicChoice - 1).getId(), questionText, option1, option2, option3, option4, correctAnswerIndex);
            if (success) {
                AuditService.getInstance().logAction("Admin added question to topic " + topics.get(topicChoice - 1).getName());
                System.out.println("Question added successfully!");
            } else {
                AuditService.getInstance().logAction("Admin failed to add question to topic " + topics.get(topicChoice - 1).getName());
                System.out.println("Failed to add question.");
                System.out.println("Failed to add question.");
            }
        } else {
            System.out.println("Invalid topic selected.");
        }
    }

    private static void deleteQuestion(Scanner scanner) {
        System.out.println("Select topic to delete question from:");
        var topics = topicService.getAllTopics();
        for (int i = 0; i < topics.size(); i++) {
            System.out.println((i + 1) + ". " + topics.get(i).getName());
            System.out.println("\t-" + topics.get(i).getDescription());
        }

        System.out.print("Enter the topic number: ");
        int topicChoice = scanner.nextInt();
        scanner.nextLine();

        if (topicChoice > 0 && topicChoice <= topics.size()) {
            try {
                var selectedTopic = topics.get(topicChoice - 1);
                var questions = questionService.getQuestionsByTopic(selectedTopic.getId());

                System.out.println("Select question to delete:");
                for (int i = 0; i < questions.size(); i++) {
                    System.out.println((i + 1) + ". " + questions.get(i).getQuestionText());
                }

                System.out.print("Enter the question number: ");
                int questionChoice = scanner.nextInt();
                scanner.nextLine();

                if (questionChoice > 0 && questionChoice <= questions.size()) {
                    var selectedQuestion = questions.get(questionChoice - 1);
                    boolean success = questionService.deleteQuestion(selectedQuestion.getId());

                    if (success) {
                        AuditService.getInstance().logAction("Admin deleted question from topic " + selectedTopic.getName());
                        System.out.println("Question deleted successfully!");
                    } else {
                        AuditService.getInstance().logAction("Admin failed to delete question from topic " + selectedTopic.getName());
                        System.out.println("Failed to delete question.");
                    }
                } else {
                    System.out.println("Invalid question selected.");
                }
            } catch (NoQuestionsInTopicException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("There are no questions in this topic to delete.");
            }
        } else {
            System.out.println("Invalid topic selected.");
        }
    }
}
