package learneng.ui;

import learneng.questionarie.Question;
import learneng.questionarie.Questionnaire;

import java.util.List;
import java.util.Scanner;

public class Console {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public void start(Questionnaire questionnaire) {
        System.out.println("For exit, please enter \'q\'.");
        System.out.println("For skip question, please enter \'s\'.");
        mainLoop:
        while (questionnaire.hasQuestions()) {
            Question question = questionnaire.getQuestion();
            List<String> variants = question.getVariants();

            System.out.println("-------------------------------------------------------------------------------------------");
            System.out.println(ANSI_YELLOW + question.getWord() + ANSI_RESET);
            System.out.println();
            for (int i = 0; i < variants.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, variants.get(i));
            }

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
                        System.out.println(ANSI_GREEN + "Right answer!" + ANSI_RESET);
                    } else {
                        System.out.println(ANSI_RED + "Wrong answer!" + ANSI_RESET);
                    }
                    printRightVariants(question);
                    break;
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.printf(ANSI_RED + "Please inter the number between 1 and %d, or \"q\" for exit!\n" + ANSI_RESET, variants.size());
                }
            }
        }
    }

    private void printRightVariants(Question question) {
        System.out.println(ANSI_PURPLE + question.getWord() + ": " + String.join(", ", question.getRightVariants()) + ANSI_RESET);
    }

}
