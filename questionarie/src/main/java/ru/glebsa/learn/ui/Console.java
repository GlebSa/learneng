package ru.glebsa.learn.ui;

import com.google.inject.Inject;
import ru.glebsa.learn.questionarie.Question;
import ru.glebsa.learn.questionarie.Questionnaire;
import ru.glebsa.tts.player.Player;

import java.util.List;
import java.util.Scanner;
import java.util.logging.LogManager;

public class Console {
    private static final String ANSI_RESET = "\u001B[0m";
    //private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    //private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    //private static final String ANSI_WHITE = "\u001B[37m";

    private static final String ANSI_UNDERLINE = "\u001B[4m";
    private static final String ANSI_LIGHT = "\u001B[1m";
    private static final String ANSI_DARK = "\u001B[2m";

    @Inject
    private Questionnaire questionnaire;

    @Inject
    private Player player;

    public void start() {
        LogManager.getLogManager().reset(); //suppress Watson logger

        System.out.println("For exit, please enter \'q\'.");
        System.out.println("For skip question, please enter \'s\'.");
        mainLoop:
        while (questionnaire.hasQuestions()) {
            Question question = questionnaire.getQuestion();
            List<String> variants = question.getVariants();

            printSeparateLine();
            printQuestionPhrase(question);
            printAnswerVariants(variants);

            player.play(question.getValue());

            //input
            while (true) {
                System.out.println();
                System.out.println("Enter:");
                Scanner sc = new Scanner(System.in);
                String ans = sc.next();

                if (ans.startsWith("q") || ans.startsWith("Q")) {
                    printRightVariants(question);
                    break mainLoop;
                }
                if (ans.startsWith("s") || ans.startsWith("S")) {
                    printRightVariants(question);
                    continue mainLoop;
                }

                try {
                    int res = Integer.valueOf(ans);
                    String userAnswer = variants.get(res - 1);
                    System.out.println();
                    if (questionnaire.answer(userAnswer)) {
                        System.out.println(ANSI_GREEN + "Right!" + ANSI_RESET);
                    } else {
                        System.out.println(ANSI_RED + "Wrong!" + ANSI_RESET);
                    }
                    printRightVariants(question);
                    break;
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Please inter the number between 1 and " + variants.size() + ", or \"q\" for exit!");
                }
            }
        }
        printSeparateLine();
        System.out.println(ANSI_LIGHT + "Right answers: " + questionnaire.rightAnswered()
                + "; Wrong answers: " + questionnaire.wrongAnswered()
                + "; Skipped: " + questionnaire.notAnswered()
                + "; Left to answer: " + questionnaire.leftToAnswer()
                + ANSI_RESET);

        questionnaire.save();
    }

    private void printAnswerVariants(List<String> variants) {
        for (int i = 0; i < variants.size(); i++) {
            System.out.println(i + 1 + ". " + ANSI_LIGHT + variants.get(i) + ANSI_RESET);
        }
    }

    private void printQuestionPhrase(Question question) {
        System.out.println(ANSI_LIGHT + ANSI_YELLOW + question.getValue() + ANSI_RESET);
        System.out.println();
    }

    private void printSeparateLine() {
        System.out.println(ANSI_UNDERLINE + ANSI_DARK
                + "                                                                                                       " + ANSI_RESET);
    }

    private void printRightVariants(Question question) {
        System.out.println(ANSI_CYAN + question.getValue() + ": " + ANSI_RESET +
                ANSI_LIGHT + ANSI_BLUE + String.join("; ", question.getRightVariants()) + ANSI_RESET);
    }

}
