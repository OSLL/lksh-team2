package team2.lksh.p.formuland.parser

import android.content.Context
import android.util.Log
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ErrorNode
import team2.lksh.p.formuland.JsonDataProcessor
import team2.lksh.p.formuland.R
import java.util.*
import kotlin.math.*

//Get list of string representations of functions
class FormulaAnalyzer(function: MutableList<String>, val dataProc : JsonDataProcessor, val context : Context) : BaseVisitor<Double?>() {

    private val parserRootCtx = mutableListOf<GenParser.RootContext>()
    private val rootCtxHashMap = mutableMapOf<String, GenParser.RootContext>()
    private val tag = "PRS"
    init {
        function.forEach {parserRootCtx.add(GenParser(CommonTokenStream(GenLexer(CharStreams.fromString(it)))).root())}
        parserRootCtx.forEach { rootCtxHashMap[it.variable().ID().toString()] = it}
    }
    private var variables = mutableMapOf<String, Double>()
    private val functions = hashMapOf<String,(List<Double>)-> Double>(
            "sin" to {i -> sin(i[0])},
            "cos" to {i -> cos(i[0])},
            "tan" to {i -> tan(i[0])},
            "cot" to {i -> 1 / tan(i[0])},
            "sqrt" to {i -> sqrt(i[0])},
            "abs" to {i -> abs(i[0])},
            "min" to {i -> min(i[0], i[1])},
            "max" to {i -> max(i[0], i[1])},
            "log" to {i -> log(i[0], i[1])},
            "log2" to {i -> log(i[0], 2.0)},
            "log10" to {i -> log(i[0], 10.0)},
            "ceil" to {i -> ceil(i[0])},
            "floor" to {i -> floor(i[0])},
            "randInt" to { _ -> Random().nextDouble()},
            "randTo" to {arr -> Random().nextInt() % arr[0]},
            "asin" to {i -> asin(i[0])},
            "acos" to {i -> acos(i[0])},
            "atan" to {i -> atan(i[0])}
    )

    private val errors = mutableListOf<String>()


    //It's need for calculating value
    //parameters:
    //hashMap - map of pairs (name_of_variable, its_value)
    //v - variable that we need to calculate
    fun run(hashMap: MutableMap<String, Double>, v : String): String? {
        errors.clear()
        this.variables = hashMap
        try {
            val num = visitRoot(rootCtxHashMap[v])
            if (num == Double.NEGATIVE_INFINITY || num == Double.POSITIVE_INFINITY || num == Double.NaN)
                return num.toString()
            //TODO: Localize infinity string
            var a = num?.toString() ?: return errorsToString()

            val point = a.indexOfLast { it == '.' }
            val e = a.indexOfLast { it == 'E' } + 1
            var exp = ""
            if (e != 0)
                exp = a.slice( e until a.length)
            var c = min (point + 7, a.length)
            if (e != 0)
                c = min(c, e - 1)
            a = a.slice(0 until c)
            if (exp != "")
                a += "*10^$exp"
            return a
        } catch (e : Exception) {
            errors.add(0, context.getString(R.string.wrong_input_error))
            Log.e(tag, context.getString(R.string.wrong_input_error))
            return errorsToString()
        }

    }

    override fun visitVariable(ctx: GenParser.VariableContext?) : Double?{
        val name = ctx?.ID()?.text
        return variables[name]
    }

    override fun visitExpr(ctx: GenParser.ExprContext?): Double? {
        if (ctx == null)
            return null
        if (ctx.childCount == 1) {
            when {
                ctx.variable() != null -> return visitVariable(ctx.variable())
                ctx.con() != null -> return visitCon(ctx.con())
                ctx.NUMBER() != null -> return ctx.NUMBER().text.toDoubleOrNull()
            }
        }
        if (ctx.childCount == 2) {
            when {
                ctx.MINUS() != null -> {
                    val res = visitExpr(ctx.expr(0))
                    return if (res != null) -res else null
                }
            }
        }
        if (ctx.childCount == 3) {
            if (ctx.LBR() != null)
                return visitExpr(ctx.expr(0))
            val a = visitExpr(ctx.expr(0))
            val b = visitExpr(ctx.expr(1))
            if (a == null || b == null)
                return null
            when {
                ctx.PLUS() != null -> return a + b
                ctx.MINUS() != null -> return a - b
                ctx.DIV() != null -> {
                    return if (b != 0.0)
                        a / b
                    else {
                        errors.add(context.getString(R.string.divide_by_zero_error))
                        Log.e(tag, context.getString(R.string.divide_by_zero_error))
                        null
                    }

                }
                ctx.MULT() != null -> return a * b
                ctx.CAP() != null -> return a.pow(b)
            }
        }
        if (ctx.func() != null) {
            if (!functions.containsKey(ctx.func().ID().toString())) {
                errors.add(context.getString(R.string.function_doesnt_exist_error))
                Log.e(tag, context.getString(R.string.function_doesnt_exist_error))
                return null
            }
            val args = mutableListOf<Double>()
            ctx.expr().forEach {args.add(visitExpr(it) ?: return null)}
            return try {
                functions[ctx.func().ID().toString()]?.invoke(args)
            } catch (e : ArithmeticException) {
                errors.add(context.getString(R.string.arithmetic_error))
                Log.e(tag, context.getString(R.string.arithmetic_error))
                null
            }
        }
        return null
    }

    override fun visitErrorNode(node: ErrorNode?): Double? {
        errors.add(context.getString(R.string.wrong_input_error))
        Log.e(tag, context.getString(R.string.wrong_input_error))
        return super.visitErrorNode(node)
    }

    override fun visitCon(ctx: GenParser.ConContext?): Double? {
        val tmp = dataProc.getConstValue(ctx?.ID().toString())
        if (tmp == null) {
            errors.add(context.getString(R.string.const_doesnt_exist_error))
            Log.e(tag, context.getString(R.string.const_doesnt_exist_error))
        }
        return tmp
    }

    private fun errorsToString() : String{
        var res = ""
        errors.map { res += "$it\n" }
        return res
    }
}
fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)
