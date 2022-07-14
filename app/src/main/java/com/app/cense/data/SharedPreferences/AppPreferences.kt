package com.app.cense.data.SharedPreferences

import android.content.Context
import android.content.SharedPreferences
import android.util.ArraySet

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

    fun addTryPurchase(purchaseId: String){
        val countTryPurchase = sharedPreferences.getInt("countTryPurchase", 0);
        ed.putInt("countTryPurchase", countTryPurchase+1)
        ed.putString("tryPurchase${countTryPurchase+1}", purchaseId)
        ed.commit()
    }

    fun getAllTryPurchaseIds(): HashSet<String> {
        val count = sharedPreferences.getInt("countTryPurchase", 0)
        val set = HashSet<String>(count)
        for (i in 1..count){
            sharedPreferences.getString("tryPurchase${i}", "n")?.let { set.add(it) }
        }
        return set
    }

    fun savePurchaseStatus(buy: String){
        ed.putBoolean(buy, true)
        ed.commit()
    }

    fun getPurchaseStatus(buy: String){
        sharedPreferences.getBoolean(buy, false)
    }


}