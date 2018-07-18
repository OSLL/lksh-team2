package team2.lksh.p.formuland

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import kotlin.reflect.KClass

class JsonDataProcessor(private val context: Context) {

    private lateinit var data: Array<Expression>
    private lateinit var const: Array<Const>
    private lateinit var typesInfoArr: Array<TypeInfo>

    init {
        data = jsonParse(R.raw.formula, Array<Expression>::class)
        const = jsonParse(R.raw.constant, Array<Const>::class)
        typesInfoArr = jsonParse(R.raw.subtypes, Array<TypeInfo>::class)
        if (JsonTypes.subtypes.isEmpty())
            setJsonTypes()
    }

    private fun <T : Any> jsonParse(res: Int, clazz: KClass<Array<T>>): Array<T> {
        val gson = Gson()

        val inputStream = context.resources.openRawResource(res)
        val reader = BufferedReader(InputStreamReader(inputStream))

        var fileContent = ""
        val lines = reader.readLines()
        lines.forEach {
            fileContent += it
        }
        val typeToken = TypeToken.get(clazz.java)
        val data: Array<T> = gson.fromJson(fileContent, typeToken.type)
        return data
    }

    private fun setJsonTypes() {
        for (typeInfo in typesInfoArr) {
            JsonTypes.subtypes.put(typeInfo.typeCode, mutableListOf())
            JsonTypes.typeNameList.add(typeInfo.typeName)
            JsonTypes.typeCodeList.add(typeInfo.typeCode)
        }
        for (typeInfo in typesInfoArr)
            for (subtypeInfo in typeInfo.subtypes) {
                JsonTypes.subtypes[typeInfo.typeCode]?.add(
                        subtypeInfo.code to subtypeInfo.subtypeName)
            }
    }


    private class SubtypeInfo(val code: String,
                              val subtypeName: String)


    private class TypeInfo(val typeCode: String,
                           val typeName: String,
                           val subtypes: List<SubtypeInfo>)

    private class Expression(val name: String,
                             val expression: List<String>,
                             val type: String,
                             val image: String,
                             val units: List<String>)

    private class Const(val name: String,
                        val value: Double,
                        val mantissa: Int)

    class MenuData(val names: List<String>,
                   val images: List<String>,
                   val idList: List<Int>,
                   val units: List<List<String>>)

    /**
     * All json data types:
     * -math.base
     *
     * getMenuData() returns MenuData object:
     * List of names, images, idList, list of units
     *
     * getExprVars() returns list of variables (String type)
     *
     * getExpr() returns list of all expressions in Strings
     *
     * getImage() returns image name in String format
     *
     * getConstValue() returns value of const in Double type
     *
     * getUnits() returns all units of expression in List<String>
     *
     * getExprConst() returns list of constants (String type)
     **/

    fun getMenuData(type: String): MenuData {
        val names: MutableList<String> = mutableListOf()
        val images: MutableList<String> = mutableListOf()
        val idList: MutableList<Int> = mutableListOf()
        val units: MutableList<List<String>> = mutableListOf()
        for (i in 0 until data.size) {
            if (data[i].type == type || (type == "all")) {
                names.add(data[i].name)
                images.add(data[i].image)
                units.add(data[i].units)
                idList.add(i)
            }
        }
        return MenuData(names, images, idList, units)
    }

    fun getImage(id: Int): String = data[id].image

    fun getExprVars(ind: Int): List<String> {
        val res: MutableSet<String> = mutableSetOf()
        val expr = data[ind].expression[0]

        var i = 0

        while (i < expr.length) {
            if (expr[i] == '@') {
                i++
                var variable = ""
                while (expr[i] != '@') {
                    variable += expr[i]
                    i++
                }
                res.add(variable)
            }
            i++
        }
        return res.toList()
    }

    fun getExpr(ind: Int): List<String> = data[ind].expression

    fun getConstValue(constName: String): Double? {
        for (obj in const)
            if (obj.name == constName) return obj.value * Math.pow(10.0, obj.mantissa.toDouble())
        return null
    }

    fun getUnits(ind: Int): List<String> = data[ind].units

    fun getExprConst(ind: Int): List<String> {
        val res: MutableSet<String> = mutableSetOf()
        val expr = data[ind].expression[0]
        var i = 0
        while (i < expr.length) {
            if (expr[i] == '&') {
                i++
                var constant = ""
                while (expr[i].isLetter()) {
                    constant += expr[i]
                    i++
                }
                res.add(constant)
            }
            i++
        }
        return res.toList()
    }
}

class JsonTypes {

    companion object {
        var subtypes = mutableMapOf<String, MutableList<Pair<String, String>>>()
        var typeCodeList: MutableList<String> = mutableListOf()
        var typeNameList: MutableList<String> = mutableListOf()
    }

}

val signN = 5