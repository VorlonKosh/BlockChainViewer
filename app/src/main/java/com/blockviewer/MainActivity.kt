package com.blockviewer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //for the sake of this demo app permit all
        StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder().permitAll().build())

    }

    fun viewChain (view: View) {

        // Create an Intent to start the second activity
        val viewBlocksIntent = Intent(this, SecondActivity::class.java)

        // Start the new activity.
        startActivity(viewBlocksIntent)
    }
}
