package com.app.cense.ui.done

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.cense.R
import com.app.cense.ui.testActivity.TestActivity

@Suppress("DEPRECATION")
class DoneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done)
    }
    companion object{
        fun start(context: Context) {
            println("start")
            val intent = Intent(context, DoneActivity::class.java)
            context.startActivity(intent)
        }
    }


    fun change(view: View){
        if (view.id == R.id.continue_studying) {
            TestActivity.start(this)
        }
        if(view.id == R.id.go_home){
            getPackageManager().clearPackagePreferredActivities(getPackageName());
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
            finish()
        }
    }
}