package com.app.cense.data.room.testResults;

import com.app.cense.App;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    String startDate;

    List<Long> millsTimes = new ArrayList<>();//currentMills
    List<Integer>secTimes = new ArrayList<>();//время на ответ
    int trueAnswers;
    int falseAnswers;

    public String date;
    public int averageTime;
    public int rightAnswers;

    public TestData(String startDate, List<Long> times, int trueAnswers, int falseAnswers){
        System.out.println("times = "+times.toString()+" true = "+trueAnswers+" false = "+falseAnswers);
        this.startDate = startDate;
        this.millsTimes = times;
        this.trueAnswers = trueAnswers;
        this.falseAnswers = falseAnswers;
        setSecAnswers();
        setAverageTime();
        setRightAnswers();
        date = startDate;
        System.out.println("date = "+date);
        System.out.println("secTimes.size = "+secTimes.size());
        System.out.println("rightAnswers = "+rightAnswers);
        System.out.println("averageTime = "+averageTime);
    }
    public TestData(){}

    public void setSecAnswers(){
        for(int i = 0; i !=(millsTimes.size()-1); i++){
            Long start = millsTimes.get(i);
            Long end = millsTimes.get(i+1);
            Long result = (end-start)/1000;
            System.out.println("setSecAnswersRes = "+result);
            secTimes.add(Math.toIntExact(result));
        }
    }

    public void setAverageTime(){
        int sum = 0;
        for (int i: secTimes){
           sum = sum+i;
        }
        averageTime = sum/secTimes.size();
    }

    public void setRightAnswers(){
        int sum = trueAnswers+falseAnswers;
        rightAnswers = 100*trueAnswers/sum;
    }


}
