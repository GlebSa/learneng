package learneng.ui;

import learneng.questionarie.Question;
import learneng.questionarie.Questionnaire;

import java.util.List;
import java.util.Scanner;

public class Console {

    public void start(Questionnaire questionnaire) {
        System.out.println("For exit, please enter \'q\'.");
        System.out.println("For skip question, please enter \'s\'.");
        mainLoop:
        while (questionnaire.hasQuestions()) {
            Question question = questionnaire.getQuestion();
            List<String> variants = question.getVariants();

            printSeparateLine();
            printQuestionPhrase(question);
            printAnswerVariants(variants);

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
                        System.out.println("Right answer!");
                    } else {
                        System.out.println("Wrong answer!");
                    }
                    printRightVariants(question);
                    break;
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Please inter the number between 1 and " + variants.size() + ", or \"q\" for exit!");
                }
            }
        }
    }

    private void printAnswerVariants(List<String> variants) {
        for (int i = 0; i < variants.size(); i++) {
            System.out.println(i + 1 + ". " + variants.get(i));
        }
    }

    private void printQuestionPhrase(Question question) {
        System.out.println(question.getValue());
        System.out.println();
    }

    private void printSeparateLine() {
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    private void printRightVariants(Question question) {
        System.out.println(question.getValue() + ": " + String.join("; ", question.getRightVariants()));
    }

}
