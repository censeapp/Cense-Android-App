package com.app.cense.ui.testActivity;

import android.util.Log;

import com.app.cense.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestActivityModel {
    private int targetClass;
    private String targetTag;
    private int targetDifficult;
    private String targetSubject;
    private boolean targetTagIsExist = false;
    private int downgradeLevel = 0;
    private List<QuestionModel> questionModels = new ArrayList<>();
    private List<QuestionModel> currentQuestion = new ArrayList<>();
    private int realSize = 0;

    public TestActivityModel(int targetClass, String targetTag, String targetSubject, List<QuestionModel> questionModels)  {
      //  System.out.println("TestActivityModel");
        this.targetClass = targetClass;
        this.targetTag = targetTag;
        this.targetSubject = targetSubject;
        this.questionModels = questionModels;
        Collections.shuffle(questionModels);
        Collections.shuffle(this.questionModels);

        int countToUnlock = App.getInstance().getSharedPreferences().getAnswersToUnlock();

        for (QuestionModel questionModel : questionModels) {//основной цикл
            if (currentQuestion.size() < countToUnlock) {
                if (questionModel.getClassName() == targetClass && questionModel.getSubjectName().equalsIgnoreCase(targetSubject)) {
                    System.out.println("основной цикл"+targetSubject+" "+questionModel.toString());
                    currentQuestion.add(questionModel);
                }
            } else {
                break;
            }
        }
        if (currentQuestion.size()<countToUnlock){//резервный цикл

            for (QuestionModel questionModel : questionModels) {
                if (currentQuestion.size() < countToUnlock) {
                        if (questionModel.getSubjectName()!=null){
                            if (questionModel.getSubjectName().equals(targetSubject)) {
                                System.out.println("резервный цикл" + targetSubject);
                                currentQuestion.add(questionModel);
                            }
                        }
                } else {
                    break;
                }
            }
        }

        realSize = currentQuestion.size();
    }

    int getSize() {
        return realSize;
    }

    public void pop() throws IndexOutOfBoundsException {
        currentQuestion.remove(currentQuestion.get(0));
    }

    public int downgrade() {
        System.out.println("downgrade()");

        List<QuestionModel> questions = new ArrayList<>();
        downgradeLevel++;
        if (targetTag.equalsIgnoreCase("")) {
            System.out.println("targetTag.equalsIgnoreCase(\"\")");
            targetTagIsExist = true;
        }
        switch (downgradeLevel) {
            case 1:
                Collections.shuffle(questionModels);
                //System.out.println("level 1 size "+ questionModels.size());
                for (QuestionModel model : questionModels) {
                   // System.out.println("sout "+String.valueOf((model.getClassName() == targetClass)+" = "+model.getClassName()+" "+targetClass+" $$ "+(model.getDifficult() < targetDifficult)+" = "+ model.getDifficult() +" "+targetDifficult));

                    if (model.getClassName() == targetClass /*&& model.getDifficult() < targetDifficult*/) {

                      if (model.tag1.equals(targetTag) || model.tag2.equals(targetTag) || model.tag3.equals(targetTag) || model.tag4.equals(targetTag) || model.tag5.equals(targetTag) || targetTagIsExist) {
                        //    System.out.println("questions.add(model);");
                            questions.add(model);
                        }
                    }
                }
                if (questions.size() > 0) {
                    currentQuestion.clear();
                    currentQuestion.addAll(questions);
                    return questions.size();
                } else {
              //      System.out.println("return");
                    return 0;
                }
            case 2:

                targetClass--;
                for (QuestionModel model : questionModels) {

                   // System.out.println("level 2 "+targetClass+" "+targetTag+(model.getClassName() == targetClass));
                    if (model.getClassName() == targetClass /*&&
                            model.getDifficult() < targetDifficult*/) {
                       // System.out.println("targetTag "+targetTag);
                        if (model.tag1.equals(targetTag) ||
                                model.tag2.equals(targetTag) ||
                                model.tag3.equals(targetTag) ||
                                model.tag4.equals(targetTag) ||
                                model.tag5.equals(targetTag) ||
                                targetTagIsExist) {
                            questions.add(model);
                        }
                    }
                }
                if (questions.size() > 0) {
                    currentQuestion.clear();
                    currentQuestion.addAll(questions);
                    realSize = currentQuestion.size();
                    return questions.size();
                } else {
                    return 0;
                }
        }
        return 0;
    }

    public QuestionModel getQuestion() throws IllegalStateException{
        if (currentQuestion.size() == 0) {
            throw new IllegalStateException("Questions list is empty");
        }
        return currentQuestion.get(0);
    }

    public boolean answer(String answer) throws IllegalStateException{
        if (currentQuestion.size() == 0) {
            throw new IllegalStateException("Questions list is empty");
        }
        return currentQuestion.get(0).getCorrectAnswer().getAnswerBody().equalsIgnoreCase(answer);
    }
}
