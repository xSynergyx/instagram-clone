package com.example.parsetagram.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.parsetagram.Post
import com.example.parsetagram.PostsAdapter
import com.example.parsetagram.PostsGridAdapter
import com.example.parsetagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment : Fragment() {

    lateinit var postsGridView: GridView
    private lateinit var adapter: PostsGridAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    val postsArrayList = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsGridView = view.findViewById(R.id.gv_profile_posts)
        adapter = PostsGridAdapter(requireContext(), postsArrayList)
        postsGridView.adapter = adapter

        // Set user details for profile page
        val user = ParseUser.getCurrentUser()
        view.findViewById<TextView>(R.id.tv_username).text = user.username
        view.findViewById<TextView>(R.id.user_bio).text = "Set your bio"
        val profileImage = view.findViewById<ImageView>(R.id.iv_profile_image)
        Glide.with(requireContext()).load(user.getParseFile("profileImage")?.url).circleCrop().into(profileImage)

        postsArrayList.clear()
        queryPersonalPosts()

        /*
        swipeContainer = view.findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refreshing timeline")
            queryPersonalPosts()
        } */
    }

    //TODO: Crazy that I can do this without a layout, but it's best to create one since the views will be very different
    fun queryPersonalPosts() {
        val query: ParseQuery<Post> = ParseQuery(Post::class.java)

        // Asking parse to also include the user that posted the Post (Since User is a pointer in the Post table)
        query.include(Post.KEY_USER)
        // Returns only current users posts
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        // Get only the first 20 posts from the server
        query.limit = 20
        query.addDescendingOrder("createdAt")
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error fetching posts")
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser()?.username)
                        }
                        postsArrayList.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "ProfileFragment"
    }
}