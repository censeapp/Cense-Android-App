package com.app.cense.data.SharedPreferences

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(val context: Context, ) { var sharedPreferences: SharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    var ed: SharedPreferences.Editor = sharedPreferences.edit()
    fun getProperty(key: String?, defValue: String?): String? {
        return sharedPreferences.getString(key, defValue)
    }
    fun getLogin(): String? { //получить логин из хранилище, если пусто - вернуть noName
        return sharedPreferences.getString("login", "noName")
    }
    fun updateTrueAnswers(mode: Int) { //обновить количество правильных ответов подряд, если 1 то прибавить, если 0 то обнулить
        if (mode == 1) { //добавить 1
            ed.putInt("countTrueAnswers", getCountTrueAnswers() + 1)
        } else if (mode == 0) { //обнулить
            ed.putInt("countTrueAnswers", 0)
        }
        ed.commit()
    }
    fun getCountTrueAnswers(): Int {
        return sharedPreferences.getInt("countTrueAnswers", 0)
    }//вернуть количество правильных ответов подряд
    fun isAchievementUnlocked(text: String): Boolean{//сохранить запись о том, что достижение уже было получено
        val res = sharedPreferences.getBoolean(text, false)
        if (!res){
            ed.putBoolean(text, true)
            ed.commit()
            return false
        }; return true
    }
    fun saveLogin(login: String?) {//сохранить логин
        ed.putString("login", login)
        ed.commit()
    }

    fun saveAnswersToUnlock(number: Int){
        ed.putInt("numToUnlock", number)
        ed.commit()
    }
    fun getAnswersToUnlock(): Int{
        return sharedPreferences.getInt("numToUnlock", 3)
    }
}