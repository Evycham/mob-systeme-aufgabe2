package com.example.mob_systeme1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.exp


/**
 * First home task for the subject "Mobile Systeme".
 *
 * Simple application which represent calculator. There are only basic functions
 * such a multiplication, dividing, addition and subtraction
 *
 * @author Yurii Gruzevych
 * Mat. num. = 20367
 * */

class MainActivity : AppCompatActivity() {

    /**
     * Global variables
     */
    private lateinit var tvDisplay: TextView
    // input
    private var expression = ""
    // last result
    private var lastResult = ""
    // memory
    private var memory1 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // dafür, dass das APP an den Rand des Bildschirms gehen kann
        enableEdgeToEdge()
        // XML verbunden
        setContentView(R.layout.activity_main)
        init()
    }

    /**
     * Function for initialization of whole object taken from the XML and also adding functional
     * */
    fun init(){
        // display field
        tvDisplay = findViewById<TextView>(R.id.tvDisplay)

        // cleaning
        findViewById<Button>(R.id.btnClean).setOnClickListener{
            expression = ""
            lastResult = ""
            updateDisplay(expression)
        }

        // backspace
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if(!expression.isEmpty()){
                expression.dropLast(1)
            }
            // if there is no sybwols left
            updateDisplay(if(expression.isEmpty()) "0" else expression)
        }

        // numbers-buttons setup
        buttonNumbers(R.id.btn9, "9")
        buttonNumbers(R.id.btn8, "8")
        buttonNumbers(R.id.btn7, "7")
        buttonNumbers(R.id.btn6, "6")
        buttonNumbers(R.id.btn5, "5")
        buttonNumbers(R.id.btn4, "4")
        buttonNumbers(R.id.btn3, "3")
        buttonNumbers(R.id.btn2, "2")
        buttonNumbers(R.id.btn1, "1")
        buttonNumbers(R.id.btn0, "0")
        buttonNumbers(R.id.btnDot, ".")

        // setup of all buttons responding for the operators
        findViewById<Button>(R.id.btnPlus).setOnClickListener { appendOperator("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { appendOperator("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { appendOperator("*") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { appendOperator("/") }

        // setup for the "equals" Button
        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            calculateResult()
        }

        // saving
        findViewById<Button>(R.id.btnMS).setOnClickListener { saveMemory() }
        // loading
        findViewById<Button>(R.id.btnMR).setOnClickListener { loadMemory() }

        // Trigonometry
        findViewById<Button>(R.id.btnSin).setOnClickListener { trigonometry("sin") }
        findViewById<Button>(R.id.btnCos).setOnClickListener { trigonometry("cos") }
        findViewById<Button>(R.id.btnTan).setOnClickListener { trigonometry("tan") }
        findViewById<Button>(R.id.btnCtg).setOnClickListener { trigonometry("ctg") }
    }
    /**
    * Function for the calculating of the input
    * */
    private fun calculateResult(){
        if(expression.isEmpty()) return

        try{
            val result = ExpressionBuilder(expression).build().evaluate()

            if (result.isInfinite() || result.isNaN()){
                updateDisplay("ERROR")
                expression = ""
            } else{
                // prüfen ob float oder int um die Anzeige zu verbessern
                val formatted = if(result % 1.0 == 0.0){
                    result.toLong().toString()
                } else {
                    // falls int
                    result.toString()
                }

                expression = formatted
                lastResult = formatted
                updateDisplay(formatted)
            }

        } catch (e: Exception){
            updateDisplay("ERROR")
            expression = ""
            lastResult = ""
        }
    }

    /**
     * Help-function for the permanent updating of the screen
     * @param value - the text has to be shown
     * */
    private fun updateDisplay(value: String){
        tvDisplay.text = value
    }

    /**
     * Function for the setup of all button which are respond for the numbers and a dot
     * @param buttonId - id of the button to add event listener
     * @param value - the number which stays on the button
     * */
    private fun buttonNumbers(buttonId: Int, value: String){
        findViewById<Button>(buttonId).setOnClickListener {

            if(tvDisplay.text.toString() == "ERROR"){
                expression = ""
            }

            if(expression.isNotEmpty() && expression == lastResult){
                expression = ""
                lastResult = ""
            }

            if(value == "." && expression.last() == '.'){
                return@setOnClickListener
            }

            expression += value
            updateDisplay(expression)
        }
    }

    /**
     * Function for adding operator. The empty expression allows to put also negative numbers
     * */
    private fun appendOperator(operator: String){
        // if we want to enter negative number
        if(expression.isEmpty()){
            if(operator == "-"){
                expression += operator
            }
        }

        // if last char is one of the operators - change
        val lastChar = expression.last()

        if(lastChar == '-' || lastChar == '+' || lastChar == '*' || lastChar == '/'){
            expression.dropLast(1)
        }
        expression += operator
        updateDisplay(expression)
    }

    /**
     * Function for saving of the result or just current expression
     * */
    private fun saveMemory(){
        if(!expression.isEmpty() || tvDisplay.text.toString() != "ERROR"){
            memory1 = tvDisplay.text.toString()
            Toast.makeText(this, "Your result was successfully saved!", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this, "There is nothing to save!", Toast.LENGTH_SHORT).show()
    }

    /**
     * Function for loading of the result from local memory
     * */
    private fun loadMemory(){
        if(tvDisplay.text.toString() == "ERROR"){
            expression = ""
            updateDisplay(expression)
        }

        if(!memory1.isEmpty()){
            expression += memory1
            updateDisplay(expression)
            Toast.makeText(this, "The memory was successfully loaded!", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this, "Warning!\nYou haven't save something yet!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun trigonometry(selectedFunction: String){
        when (selectedFunction) {
            "sin" -> expression += "sin("
            "cos" -> expression += "cos("
            "tan" -> expression += "tan("
            "ctg" -> expression += "ctg("
        }
        updateDisplay(expression)
    }
}