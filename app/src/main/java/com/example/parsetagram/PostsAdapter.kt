package com.example.parsetagram

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

class PostsAdapter(private val context: Context, private val posts: ArrayList<Post>) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsAdapter.ViewHolder, position: Int) {

        val post: Post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val ivPostImage: ImageView = itemView.findViewById(R.id.post_image)
        val tvUser: TextView = itemView.findViewById(R.id.post_user)
        val tvDescription: TextView = itemView.findViewById(R.id.post_description)
        val tvTimeSince: TextView = itemView.findViewById(R.id.post_time)

        fun bind(post: Post) {
            tvUser.text = post.getUser()?.username
            tvDescription.text = post.getDescription()

            val time = timeSincePost(post.createdAt.toString())
            tvTimeSince.text = time

            Glide.with(itemView.context).load(post.getImage()?.url).into(ivPostImage)
        }
    }

    fun timeSincePost(createdAt: String): String {

        // Get the created at value for the tweet and parse the string to a Date object
        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
        val parsedDate: Date = dateFormat.parse(createdAt)

        // Get the current time and parse that to a Date as well
        val currentDate = LocalDateTime.now(ZoneOffset.UTC)
        val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss +0000 yyyy")
        val formatted = currentDate.format(formatter)
        val parsedCurrentDate: Date = dateFormat.parse(formatted.toString())

        // Find the time difference between the two dates
        val diff: Long = parsedCurrentDate.time - parsedDate.time

        // Convert the returned long into time in seconds
        val seconds = diff.toDuration(DurationUnit.MILLISECONDS).toInt(DurationUnit.SECONDS)

        var postTime = ""
        postTime = if (seconds>=172800) {
            val time: Int = seconds / 86400
            "$time" + " days ago"
        } else if (seconds>=86400) {
            val time: Int = seconds / 86400
            "$time" + " day ago"
        } else if (seconds>=7200) {
            val time: Int = seconds / 3600
            "$time" + " hours ago"
        }else if (seconds>=3600) {
            val time: Int = seconds / 3600
            "$time" + " hour ago"
        } else if (seconds >= 120){
            val time: Int = seconds / 60
            "$time" + " minutes ago"
        } else if (seconds >= 60){
            val time: Int = seconds / 60
            "$time" + " minute ago"
        } else if (seconds == 1){
            val time: Int = seconds
            "$time" + " second ago"
        } else {
            val time: Int = seconds
            "$time" + " seconds ago"
        }

        return postTime
    }

    companion object {
        const val TAG = "PostsAdapter"
    }
}