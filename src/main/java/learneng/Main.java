package learneng;

import learneng.questionarie.QuestionnaireFactory;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        QuestionnaireFactory.create("/home/gleb/Literature/Eng/выражения.txt");
        //new Main().start();
    }

    public void start() {
        while (true) {
            System.out.println("Ответ: ");
            Scanner sc = new Scanner(System.in);

            String ans = sc.next();

            if (ans.startsWith("q") || ans.startsWith("Q")) System.exit(0);

            try {
                int res = Integer.valueOf(ans);
                System.out.println(res);
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста введите число либо символ \"q\" для выхода!");
            }
        }
    }

}
