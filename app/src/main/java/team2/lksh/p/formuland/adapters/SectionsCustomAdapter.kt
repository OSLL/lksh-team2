package team2.lksh.p.formuland.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.formula_list_fragment.view.*
import kotlinx.android.synthetic.main.section_row.view.*
import team2.lksh.p.formuland.JsonTypes
import team2.lksh.p.formuland.R

class SectionsCustomAdapter(val subject : String, val v: View, val context : Context) : RecyclerView.Adapter<SectionViewHolder>() {

    var current = 0

    lateinit var currentView : View

    override fun getItemCount(): Int {
        return JsonTypes.subtypes[subject]?.size ?: 0
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.itemView.subject.text = JsonTypes.subtypes[subject]!![position].second
        holder.index = position

        if (current == position) {

            holder.itemView.card_view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent4))

            v.list.adapter = FormulasAdapter(context, "$subject.${JsonTypes.subtypes[subject]!![position].first}")

            currentView = holder.itemView
            current = -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.section_row, parent, false)
        return SectionViewHolder(cellForRow, this)
    }

    fun onClick(view : View, index : Int) {
        if (view == currentView)
            return
        v.list.adapter = FormulasAdapter(context, "$subject.${JsonTypes.subtypes[subject]!![index].first}")
        try {
            currentView.card_view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent2))
        } catch (e : kotlin.UninitializedPropertyAccessException) { }

        view.card_view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent4))

        currentView = view
    }
}
class SectionViewHolder(val v: View, ada : SectionsCustomAdapter) : RecyclerView.ViewHolder(v) {
    var index = -1
    init {
        v.setOnClickListener {
            ada.onClick(v, index)
        }
    }
}