package learneng;

import learneng.questionarie.Questionnaire;
import learneng.questionarie.QuestionnaireFactory;
import learneng.ui.Console;

public class Main {

    public static void main(String[] args) {
        Questionnaire questionnaire = QuestionnaireFactory.create("/home/gleb/Literature/Eng/выражения.txt", 5);
        new Console().start(questionnaire);
    }


}
