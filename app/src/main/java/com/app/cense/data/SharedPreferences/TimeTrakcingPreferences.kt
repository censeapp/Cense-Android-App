package com.app.cense.data.SharedPreferences

import android.content.Context
import android.content.SharedPreferences

class TimeTrakcingPreferences(context: Context) {
    var sharedPreferences: SharedPreferences = context.getSharedPreferences("times", Context.MODE_PRIVATE)
    var ed: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveActivityTime(classname: String, time: Long){
        ed.putLong(classname, time)
        ed.commit()
    }

    fun getActivityTime(classname: String): Long{
       return sharedPreferences.getLong(classname, 0)
    }


}