package com.app.cense;

import com.app.cense.data.SharedPreferences.TimeTrakcingPreferences;
import com.yandex.metrica.YandexMetrica;

public class TimeTracker {
    public String classname = "null";//имя текущего экрана
    public long onResumeTime = 0;//время запуска экрана
    public long onPauseTime = 0;//время остановки экрана

    final static TimeTrakcingPreferences ttp = App.getInstance().getTtp();

    public void saveTime() {
        ttp.saveActivityTime(classname, (onPauseTime-onResumeTime)/1000+(ttp.getActivityTime(classname)));
        onResumeTime = 0;
        onPauseTime = 0;
    }
    public static void sendTime() {
        long parentTime = ttp.getActivityTime("main");
        parentTime+=ttp.getActivityTime("auth");
        parentTime+=ttp.getActivityTime("select");

        long childTime = ttp.getActivityTime("lock");
        childTime += ttp.getActivityTime("test");
        childTime += ttp.getActivityTime("done");
        childTime += ttp.getActivityTime("achieve");
        long sumTime = parentTime+childTime;

        if (sumTime != 0) {
            System.out.println("send " + parentTime + " " + childTime);
            String str = App.getDate(App.DATE_FORMAT)+" Parent "+parentTime+"sec, Children "+childTime+"sec, Sum "+sumTime+"sec";
        YandexMetrica.reportEvent(App.getInstance().getSharedPreferences().getLogin(), "{\"time\": \""+str+"\"}");
            ttp.saveActivityTime("main", 0);
            ttp.saveActivityTime("auth", 0);
            ttp.saveActivityTime("select", 0);
            ttp.saveActivityTime("lock", 0);
            ttp.saveActivityTime("test", 0);
            ttp.saveActivityTime("done", 0);
            ttp.saveActivityTime("achieve", 0);
        }
    }
}
