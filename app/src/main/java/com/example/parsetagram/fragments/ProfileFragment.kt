package com.example.parsetagram.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.parsetagram.Post
import com.example.parsetagram.PostsAdapter
import com.example.parsetagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment : Fragment() {

    lateinit var postsRecyclerView: RecyclerView
    lateinit var adapter: PostsAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    val postsArrayList = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsRecyclerView = view.findViewById(R.id.posts_recycler_view)
        adapter = PostsAdapter(requireContext(), postsArrayList)
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        postsRecyclerView.adapter = adapter

        queryPersonalPosts()

        swipeContainer = view.findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refreshing timeline")
            queryPersonalPosts()
        }
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