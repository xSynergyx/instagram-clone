package com.example.parsetagram.fragments

import android.util.Log
import com.example.parsetagram.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment: HomeFragment() {

    //TODO: Crazy that I can do this without a layout, but it's best to create one since the views will be very different
    override fun queryPosts() {
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
}