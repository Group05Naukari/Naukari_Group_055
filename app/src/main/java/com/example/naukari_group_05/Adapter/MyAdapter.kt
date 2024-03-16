package com.example.naukari_group_05.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.naukari_group_05.DetailActivity
import com.example.naukari_group_05.Model.Cadidate
import com.example.naukari_group_05.R

class MyAdapter(private val dataList: List<Cadidate>, val activity: Activity) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.candidate_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = dataList[position]
        holder.name.text = item.name
        holder.jobtitle.text = item.jobtitle
        Glide.with(activity)
            .load(item.photo_url)
            .into(holder.photo)


        if(item.conneted == true){
            Glide.with(activity)
                .load(R.drawable.ic_connect)
                .into(holder.conneteddata)
        }else{
            Glide.with(activity)
                .load(R.drawable.ic_notconnect)
                .into(holder.conneteddata)
        }



        holder.viewfullprofile.setOnClickListener(View.OnClickListener {
            val intent222 = Intent(activity, DetailActivity::class.java)
            intent222.putExtra("Cadidatedata", item)
            activity.startActivity(intent222)

        })

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.user_name)
        val jobtitle: TextView = itemView.findViewById(R.id.jobtitle)
        val photo: ImageView = itemView.findViewById(R.id.photo)
        val conneteddata: ImageView = itemView.findViewById(R.id.conneteddata)
        val viewfullprofile: CardView = itemView.findViewById(R.id.viewfullprofile)
    }
}




