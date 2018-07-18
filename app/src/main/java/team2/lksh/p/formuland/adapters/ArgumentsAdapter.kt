package team2.lksh.p.formuland.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.argument_row.view.*
import team2.lksh.p.formuland.R

class ArgumentsAdapter(private val variables: List<String>, private val units : List<String>, private val activity: Activity) : RecyclerView.Adapter<ArgumentsViewHolder>() {

    var cardList: MutableList<View> = mutableListOf()
    var currentChecked : Int = -1

    val views = mutableListOf<View>()

    fun setTarget(v: View, position: Int) {
        v.radio.isChecked = true
        v.arg_edit.isActivated = false
        v.arg_edit.isEnabled = false
        v.arg_edit.isFocusable = false
        v.arg_edit.isFocusableInTouchMode = false
        v.arg_edit.setText("Вы считаете это число")
        v.arg_edit.textSize = 17.0F
//        v.arg_edit.hint = ""

        currentChecked = position
    }

    fun setWritable(v: View, context: Context) {
        v.radio.isChecked = false
        v.arg_edit.isActivated = true
        v.arg_edit.isEnabled = true
        v.arg_edit.isFocusable = true
        v.arg_edit.isFocusableInTouchMode = true
        v.arg_edit.setText("")
        v.arg_edit.hint = context.getString(R.string.enter_number)
    }

    override fun getItemCount(): Int {
        return variables.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArgumentsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val cellForRow = layoutInflater.inflate(R.layout.argument_row, parent, false)
        return ArgumentsViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: ArgumentsViewHolder, position: Int) {
        val v = holder.itemView

        if (position == 0) {
            setTarget(v, 0)
        }

        views.add(v)

        val variable = variables[position]

        v.d_variable.text = variable
        v.d_pos.text = position.toString()


        try {
            if (variable.toLowerCase() == variable) {
                v.var_image.setImageDrawable(getImgDrawable(activity, "z_$variable"))
            } else {
//                if ()
                v.var_image.setImageDrawable(getImgDrawable(activity, "z_y${variable.toLowerCase()}"))
            }
        } catch (e : Exception) {
//            v.arg.text = variables[position].toUpperCase()
        }

        v.d_variable.text = variables[position]
        v.d_pos.text = position.toString()


        v.arg_unit.text = units[position]


        v.radio.setOnClickListener {

            if (currentChecked != -1)
                setWritable(cardList[currentChecked], activity)

            setTarget(cardList[position], position)
        }
        cardList.add(v)
    }

    fun getViewsList() : MutableList<View> = views
}

open class ArgumentsViewHolder(v: View) : RecyclerView.ViewHolder(v)