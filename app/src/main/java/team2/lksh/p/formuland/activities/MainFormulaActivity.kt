package team2.lksh.p.formuland.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.activity_main_formul.*
import kotlinx.android.synthetic.main.argument_row.view.*
import team2.lksh.p.formuland.JsonDataProcessor
import team2.lksh.p.formuland.R
import team2.lksh.p.formuland.adapters.ArgumentsAdapter
import team2.lksh.p.formuland.adapters.getImgDrawable
import team2.lksh.p.formuland.parser.FormulaAnalyzer
import team2.lksh.p.formuland.signN
import java.util.*

fun findElem(n: Int, arr: List<Int>) : Int {
    for (i in 0..arr.size) {
        if (arr[i] == n) {
            return i
        }
    }
    return -1
}

fun findStr(str: String, arr: List<String>) : Int {
    for (i in 0..arr.size) {
        if (arr[i] == str) {
            return i
        }
    }
    return -1
}

fun findChar(ch : Char, str : String) : Int {
    for (i in 0..str.length) {
        if (str[i] == ch) {
            return i
        }
    }

    return -1
}


fun getTrueVars(expressions: List<String>, vars : List<String>) : List<String> {
    if (vars.size == expressions.size) {
        return vars
    }

    val trueVars = mutableListOf<String>()

    for (expr in expressions) {

        var flag = false

        var substr = ""
        for (i in 0 until expr.length) {
            if (expr[i] == '=') {
                val lastLetter = i - 2

                if (!flag) {
                    substr = expr.substring(1..lastLetter)
                    flag = true
                }
            }
        }
        trueVars.add(substr)
    }

//    return Arrays.toString(expressions.toTypedArray()) +
//            "|||" + Arrays.toString(vars.toTypedArray()) +
//            "|||" + Arrays.toString(trueVars.toTypedArray())

    return vars - trueVars

}

class MainFormulaActivity : AppCompatActivity() {

    lateinit var adapter: ArgumentsAdapter
    var formulaId = -1
    private lateinit var jsonData: JsonDataProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_formul)

        formulaId = intent.getIntExtra("id", 0)
        val subj = intent.getStringExtra("subject")

        jsonData = JsonDataProcessor(this)


        val menuData = jsonData.getMenuData(subj)

        val index = findElem(formulaId, menuData.idList)

        val name = menuData.names[index]
        val imgName = menuData.images[index]

//        val vars = getTrueVars(expressions, jsonData.getExprVars(formulaId))
        val vars = jsonData.getExprVars(formulaId)
        val units = jsonData.getUnits(formulaId)

        Log.i("tryqsdfujfkjdg", Arrays.toString(vars.toTypedArray()))



        img_formula.setImageDrawable(getImgDrawable(this, imgName))

        val argAdapter = ArgumentsAdapter(vars, units, this)

        args.layoutManager = LinearLayoutManager(this)
        args.adapter = argAdapter

        adapter = argAdapter


        setSupportActionBar(toolbar_formula)

        toolbar_formula.setNavigationIcon(R.drawable.sharp_arrow_back_white_24)
        toolbar_formula.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar?.title = name
    }

    fun onCalculate(v: View) {

        val pares: MutableMap<String, Double> = mutableMapOf()
        var unknownVar = ""

        for (view in adapter.cardList) {

            val variable = view.d_variable.text.toString()
            val input = view.arg_edit.text.toString()

            if (view.radio.isChecked) {
                unknownVar = variable
            } else {
                if (!input.isBlank())
                try {
                    pares[variable] = input.toDouble()
                } catch (e : Exception) {
                    answer.text = getString(R.string.wrong_input_error)
                    return
                }
            }
        }

        val a = FormulaAnalyzer(jsonData.getExpr(formulaId).toMutableList(), jsonData, this)

        answer.text = a.run(pares, unknownVar)

    }

//    fun onInfoClick(v: View) {
//        jsonData.getMenuData()
//    }
}
