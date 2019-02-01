package learneng;

import learneng.questionarie.Questionnaire;
import learneng.questionarie.QuestionnaireFactory;
import learneng.ui.Console;

public class Main {

    public static void main(String[] args) {
        String path = null;
        int variants = 5;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-p") || args[i].equalsIgnoreCase("--path")) {
                path = args[++i];
            }
            if (args[i].equalsIgnoreCase("-v") || args[i].equalsIgnoreCase("--variants")) {
                variants = Integer.valueOf(args[++i]);
            }
            if (args[i].equalsIgnoreCase("--help")) {
                System.out.println("-p {pathToFile} or --path {pathToFile} for path for text file\n" +
                        "-v {number} or --variants {number} for number of shown variants for a question");
                return;
            }
        }
        if (path == null) {
            System.out.println("You must set the path, enter --help for more info!");
            return;
        }

        Questionnaire questionnaire = QuestionnaireFactory.create(path, variants);
        new Console().start(questionnaire);
    }


}
