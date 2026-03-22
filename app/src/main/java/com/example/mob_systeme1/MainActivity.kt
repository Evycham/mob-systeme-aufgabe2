package com.example.mob_systeme1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.exp

// TODO: in ReadMe has to be said that trigonometrical functions work only with radiant and not with grads

/**
 * First home task for the subject "Mobile Systeme".
 *
 * Simple application which represent calculator. There are only basic functions
 * such a multiplication, dividing, addition and subtraction
 *
 * @author Yurii Gruzevych
 * Mat. num. = 20367
 * Version 2
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
            expression = "0"
            lastResult = ""
            memory1 = ""
            updateDisplay(expression)
        }

        // delete last input
        findViewById<Button>(R.id.btnCleanLast).setOnClickListener { clearLastInput() }

        // backspace
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if(!expression.isEmpty()){
                expression = expression.dropLast(1)
            }
            // if there is no symbols left
            if(expression.isEmpty()) expression = "0"

            updateDisplay(expression)
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

        // Trigonometry
        findViewById<Button>(R.id.btnSin).setOnClickListener { trigonometry("sin") }
        findViewById<Button>(R.id.btnCos).setOnClickListener { trigonometry("cos") }
        findViewById<Button>(R.id.btnTan).setOnClickListener { trigonometry("tan") }

        // Square
        findViewById<Button>(R.id.btnSqr).setOnClickListener { trigonometry("sqr") }

        // setup for the "equals" Button
        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            calculateResult()
        }

        // saving
        findViewById<Button>(R.id.btnMS).setOnClickListener { saveMemory() }
        // loading
        findViewById<Button>(R.id.btnMR).setOnClickListener { loadMemory() }

        // symbol swapping
        findViewById<Button>(R.id.btnChange).setOnClickListener {
            try{
                processingSymbol()
            } catch(e: Exception){
                print(e)
            }
        }
    }
    /**
    * Function for the calculating of the input
    * */
    private fun calculateResult(){
        if(expression.isEmpty()) return

        try{
            expression = countSymbols(expression)
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

            if(tvDisplay.text.toString() == "ERROR" || tvDisplay.text.toString() == "0"){
                expression = ""
            }

            if(expression.isNotEmpty() && expression == lastResult){
                expression = ""
                lastResult = ""
            }


            // special cases for the "."
            if(value == ".") {

                // empty -> 0.
                if(expression.isEmpty()){
                    expression = "0."
                    updateDisplay(expression)
                    return@setOnClickListener
                }

                val lastChar = expression.last()

                // after operator or after "(" -> 0.
                if(isOperator(lastChar) || lastChar == '('){
                    expression += "0."
                    updateDisplay(expression)
                    return@setOnClickListener
                }

                // if the number contains one "."
                if(currentNumberHasDot()){
                    return@setOnClickListener
                }

                if(expression.last() == '.'){
                    return@setOnClickListener
                }
            }

            expression += value
            updateDisplay(expression)
        }
    }

    /**
     * Help function to analyze the last number whether it contains a "."
     * */
    private fun currentNumberHasDot(): Boolean {
        var ix = expression.lastIndex

        while (ix >= 0) {
            val c = expression[ix]

            if (c == '.') return true
            if (isOperator(c) || c == '(') break

            ix--
        }

        return false
    }


    /**
     * Function just for
     * @param c - symbol
     * @return True - if it is on of the parameters
     * @return False - if it is not
    */
    private fun isOperator(c: Char): Boolean {
        return c == '+' || c == '-' || c == '*' || c == '/'
    }


    /**
     * Function for adding operator. The empty expression allows to put also negative numbers
     * @param operator - operator
     * */
    private fun appendOperator(operator: String) {
        if (tvDisplay.text.toString() == "ERROR") {
            expression = "0"
        }

        if (expression.isEmpty() || expression == "0"){
            expression = "0$operator"
            updateDisplay(expression)
            return
        }

        if(isTrigonometry()) {
            expression += "0$operator"
            updateDisplay(expression)
            return
        }

        val lastChar = expression.last()

        if(isOperator(lastChar)){
            expression = expression.dropLast(1) + operator
        } else{
            expression += operator
        }

        updateDisplay(expression)
    }

    /**
     * Function for saving of the result or just current expression
     * */
    private fun saveMemory(){
        if(expression.isNotEmpty() && tvDisplay.text.toString() != "ERROR"){
            memory1 = tvDisplay.text.toString()
            Toast.makeText(this, "Your result was successfully saved!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "There is nothing to save!", Toast.LENGTH_SHORT).show()
        }
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

    /**
     * Basic setup for the buttons which respond fpr the trigonometry
     * */
    private fun trigonometry(selectedFunction: String){
        if(tvDisplay.text.toString() == "ERROR" || tvDisplay.text.toString() == "0"){
            expression = ""
            updateDisplay(expression)
        }
        when (selectedFunction) {
            "sin" -> expression += "sin("
            "cos" -> expression += "cos("
            "tan" -> expression += "tan("
            "sqr" -> expression += "sqr("
        }
        updateDisplay(expression)
    }

    /**
     * Function which helps to count all "(" wich was used in trigonometrical functions
     * @param input - the String where it has to be counted
     * */
    private fun countSymbols(input: String): String{
        var result = input

        val opened = input.count{ it == '(' }

        for(i in 0 until opened){
            result += ")"
        }

        return result
    }

    /**
     * Function for swapping the operator
     * */
    private fun processingSymbol(){
        if(tvDisplay.text.toString() == "ERROR" || tvDisplay.text.toString() == "0"){
            expression = "0"
            updateDisplay(expression)
            return
        }

        val lastChar = expression.last()
        when(lastChar){
            '-' -> {
                expression = expression.dropLast(1)
                expression += '+'
            }
            '+' -> {
                expression = expression.dropLast(1)
                expression += '-'
            }
            '*' -> return
            '/' -> return
            else -> changeSymbol()
        }
    }


    /**
     * Function for swapping the operator
     * */
    private fun changeSymbol(){
        var currentIx = expression.lastIndex

        while(currentIx >= 0){
            if(expression[currentIx].isDigit() || expression[currentIx] == '.'){
                currentIx--
            } else{
                break
            }
        }

        // if there is just one positive number -> get information
        // from the "expression" and add minus at the beginning
        if(currentIx < 0){
            expression = "-$expression"
        } else{
            val currentChar = expression[currentIx]

            when(currentChar){
                // '-' -> expression = expression.substring(0, currentIx) + '+'+ expression.substring(currentIx + 1)
                '-' -> expression = expression.replaceRange(currentIx, currentIx + 1, "+")
                '+' -> expression = expression.replaceRange(currentIx, currentIx + 1, "-")
                '(' -> expression = expression.replaceRange(currentIx, currentIx + 1, "(-")
            }
        }
        updateDisplay(expression)
    }

    /**
     * Function for the button "CE" - clean last input
     * */
    private fun clearLastInput(){
        if(tvDisplay.text.toString() == "ERROR" || expression.isEmpty() || expression == "0"){
            expression = "0"
            updateDisplay(expression)
            return
        }

        if(isOperator(expression.last())){
            expression = expression.dropLast(1)
        } else if(isTrigonometry()){
            expression = expression.dropLast(4)
        } else{
            var currentIx = expression.lastIndex

            while(currentIx >= 0 && (expression[currentIx].isDigit() || expression[currentIx] == '.')){
                currentIx--
            }

            expression = expression.substring(0, currentIx + 1)
        }

        if(expression.isEmpty()){
            expression = "0"
        }

        updateDisplay(expression)
    }

    /**
     * Function which says if the expression contains trigonometrical function at the end
     * @return false - if no
     * @return true - if yes
     * */
    private fun isTrigonometry(): Boolean{
        return (expression.endsWith("sin(") ||
            expression.endsWith("cos(") ||
            expression.endsWith("tan(") ||
            expression.endsWith("sqr(")
            )
    }

}






