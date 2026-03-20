package com.example.mob_systeme1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }

    fun init(){
        val display = findViewById<TextView>(R.id.tvDisplay)
        val btnMR = findViewById<Button>(R.id.btnMR)
        val btnMS = findViewById<Button>(R.id.btnMS)
        val btnClean = findViewById<Button>(R.id.btnClean)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btn9 = findViewById<Button>(R.id.btn9)
        val btn8 = findViewById<Button>(R.id.btn8)
        val btn7 = findViewById<Button>(R.id.btn7)
        val btn6 = findViewById<Button>(R.id.btn6)
        val btn5 = findViewById<Button>(R.id.btn5)
        val btn4 = findViewById<Button>(R.id.btn4)
        val btn3 = findViewById<Button>(R.id.btn3)
        val btn2 = findViewById<Button>(R.id.btn2)
        val btn1 = findViewById<Button>(R.id.btn1)
        val btn0 = findViewById<Button>(R.id.btn0)
        val btnDot = findViewById<Button>(R.id.btnDot)
        val btnDivide = findViewById<Button>(R.id.btnDivide)
        val btnMultiply = findViewById<Button>(R.id.btnMultiply)
        val btnMinus = findViewById<Button>(R.id.btnMinus)
        val btnPlus = findViewById<Button>(R.id.btnPlus)
        val btnEquals = findViewById<Button>(R.id.btnEquals)
    }
}