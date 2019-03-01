package ru.glebsa.learn;

import ru.glebsa.learn.questionarie.Questionnaire;
import ru.glebsa.learn.questionarie.QuestionnaireService;
import ru.glebsa.learn.ui.Console;

public class Main {

    public static void main(String[] args) {
        String path = null;
        String savePath = "";
        int variants = 5;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-p") || args[i].equalsIgnoreCase("--path")) {
                path = args[++i];
            }
            if (args[i].equalsIgnoreCase("-sp") || args[i].equalsIgnoreCase("--save-path")) {
                savePath = args[++i];
            }
            if (args[i].equalsIgnoreCase("-v") || args[i].equalsIgnoreCase("--variants")) {
                variants = Integer.valueOf(args[++i]);
            }
            if (args[i].equalsIgnoreCase("--help")) {
                System.out.println("-p {pathToFile} or --path {pathToFile} for path for text file\n" +
                        "-sp {pathToDirectory} or --save-path {pathToDirectory} for path where to save temporary state\n" +
                        "-v {number} or --variants {number} for number of shown variants for a question");
                return;
            }
        }
        if (path == null || savePath == null) {
            System.out.println("You must set the path, enter --help for more info!");
            return;
        }

        Questionnaire questionnaire = QuestionnaireService.createCommon(path, savePath, variants);
        new Console().start(questionnaire);
        QuestionnaireService.save(questionnaire, savePath);
    }


}
