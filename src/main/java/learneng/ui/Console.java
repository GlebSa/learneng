package learneng.ui;

import com.diogonunes.jcdp.color.ColoredPrinter;
import learneng.questionarie.Question;
import learneng.questionarie.Questionnaire;

import java.util.List;
import java.util.Scanner;

import static com.diogonunes.jcdp.color.api.Ansi.*;

public class Console {
    private final ColoredPrinter p = new ColoredPrinter.Builder(1, false).build();

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
                        p.setForegroundColor(FColor.GREEN);
                        p.println("Right answer!");
                        p.clear();
                    } else {
                        p.setForegroundColor(FColor.RED);
                        p.println("Wrong answer!");
                        p.clear();
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
            System.out.print(i + 1 + ". ");

            p.setAttribute(Attribute.BOLD);
            p.println(variants.get(i));
            p.clear();
        }
    }

    private void printQuestionPhrase(Question question) {
        p.setForegroundColor(FColor.YELLOW);
        p.setAttribute(Attribute.BOLD);
        p.println(question.getValue());
        p.clear();
        System.out.println();
    }

    private void printSeparateLine() {
        p.setAttribute(Attribute.UNDERLINE);
        p.setForegroundColor(FColor.WHITE);
        p.println("                                                                                                      ");
        p.clear();
    }

    private void printRightVariants(Question question) {
        p.setAttribute(Attribute.BOLD);
        p.setForegroundColor(FColor.MAGENTA);
        p.print(question.getValue());
        p.clear();

        p.setForegroundColor(FColor.MAGENTA);
        p.println(": " + String.join("; ", question.getRightVariants()));
        p.clear();
    }

}
