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

            System.out.println("-------------------------------------------------------------------------------------------");
            System.out.println(question.getWord());
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
                        System.out.println("Right answer!");
                    } else {
                        System.out.println("Wrong answer!");
                    }
                    printRightVariants(question);
                    break;
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.printf("Please inter the number between 1 and %d, or \"q\" for exit!\n", variants.size());
                }
            }
        }
    }

    private void printRightVariants(Question question) {
        System.out.println(question.getWord() + ": " + String.join(", ", question.getRightVariants()));
    }

}
