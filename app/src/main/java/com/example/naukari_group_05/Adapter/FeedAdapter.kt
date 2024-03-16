package com.example.naukari_group_05.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.naukari_group_05.Model.FeedItem
import com.example.naukari_group_05.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FeedAdapter(private val dataList: List<FeedItem>, val activity: Activity) :
    RecyclerView.Adapter<FeedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.user_name.text = dataList[position].author
        holder.headline.text = dataList[position].headline
        holder.content.text = dataList[position].content


        val isoTimestamp = dataList[position].timestamp
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val parsedDateTime = LocalDateTime.parse(isoTimestamp, dateTimeFormatter)

        val socialMediaFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a")
        val formattedTimestamp = parsedDateTime.format(socialMediaFormatter)


        holder.date.text =  "Post On : " +formattedTimestamp
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val user_name: TextView = itemView.findViewById(R.id.user_name)
        val headline: TextView = itemView.findViewById(R.id.headline)
        val content: TextView = itemView.findViewById(R.id.content)
        val date: TextView = itemView.findViewById(R.id.date)

    }

}


