package com.codestracture.ui.swipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.codestracture.R

class ViewPagerAdapter(
    private val list: List<ImageModelWithText>
) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewSubTitle: TextView = itemView.findViewById(R.id.textViewSubTitle)
        val cardView: CardView = itemView.findViewById(R.id.cardView1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.viewpager_item, parent, false)
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.imageView.setImageResource(list[position].resId)
        holder.textViewTitle.text = list[position].title
        holder.textViewSubTitle.text = list[position].subTitle

//        Blurry.with(holder.cardView.context).capture(holder.imageView).getAsync {
//            holder.cardView.background = BitmapDrawable(holder.cardView.resources, it)
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
