package com.example.parsetagram

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject

//import com.parse.Parse

class ParsetagramApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Register the Post Parse model so that we can use that class and link to the table
        ParseObject.registerSubclass(Post::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}
