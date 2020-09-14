package com.example.madlevel4task1.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.madlevel4task1.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

    }

}