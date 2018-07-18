package team2.lksh.p.formuland.adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.formul_row.view.*
import team2.lksh.p.formuland.R
import team2.lksh.p.formuland.JsonDataProcessor
import team2.lksh.p.formuland.activities.MainFormulaActivity

fun generateFormulaActivity(activity: Context, id: Int, subject: String) {
    val a = Intent(activity, MainFormulaActivity::class.java)
    a.putExtra("id", id)
    a.putExtra("subject", subject)
    activity.startActivity(a)
}

fun getImgDrawable(activity: Context, name : String) : Drawable {
    val res = activity.resources
    val resId = res.getIdentifier(name, "drawable", activity.packageName)

//    ContextCompat.getDrawable(activity, R.drawable.z_a)
    val draw = ResourcesCompat.getDrawable(res, resId, null)!!

//    val draw = res.getDrawable(resId)
    return draw
}

class FormulasAdapter(val activity: Context, val subject: String) : RecyclerView.Adapter<FormulaViewHolder>() {

    val jsonDataProcessor = JsonDataProcessor(activity)
    val data = jsonDataProcessor.getMenuData(subject)

    override fun getItemCount(): Int {
        return data.idList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormulaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val cellForRow = layoutInflater.inflate(R.layout.formul_row, parent, false)
        return FormulaViewHolder(cellForRow, activity, subject)
    }

    override fun onBindViewHolder(holder: FormulaViewHolder, position: Int) {
        val v = holder.itemView

        val title = data.names[position]
        val imgPath = data.images[position]
        holder.index = data.idList[position]

        v.title.text = title

        val drawable = getImgDrawable(activity, imgPath)

        v.img.setImageDrawable(drawable)
        v.pos_text.text = data.idList[position].toString()
    }
}
class FormulaViewHolder(v: View, activity: Context, subject: String) : RecyclerView.ViewHolder(v) {
    var index = -1
    init {
        v.setOnClickListener {
            generateFormulaActivity(activity, index, subject)
        }
    }
}
