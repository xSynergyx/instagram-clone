package com.example.parsetagram

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.core.content.getSystemService
import com.bumptech.glide.Glide

internal class PostsGridAdapter (
    private val context: Context,
    private val posts: ArrayList<Post>
        ) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var gridImage: ImageView

    override fun getCount(): Int {
        return posts.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var postGridView = view
        //val ivPost: ImageView = postGridView.findViewById(R.id.iv_grid_post)

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context)
        }
        if (postGridView == null) {
            postGridView = layoutInflater!!.inflate(R.layout.item_gridview_post, null)
        }

        gridImage = postGridView!!.findViewById<ImageView>(R.id.iv_grid_post)
        val post = posts[position]
        Glide.with(postGridView.context).load(post.getImage()?.url).into(gridImage)
        return postGridView
    }
}