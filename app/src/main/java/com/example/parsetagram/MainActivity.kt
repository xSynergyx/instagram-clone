package com.example.parsetagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.parse.*
import java.io.File


class MainActivity : AppCompatActivity() {

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null

    var cameraResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.sign_out_button).setOnClickListener {
            signOut()
            goToLoginActivity()
        }

        findViewById<Button>(R.id.submit_post_button).setOnClickListener {
            val description = findViewById<EditText>(R.id.et_description).text.toString()
            if (photoFile != null) {
                submitPost(description, ParseUser.getCurrentUser(), photoFile!!)
            } else {
                // TODO: Print error log message
                //       Show toast to user to send picture
            }
        }

        findViewById<Button>(R.id.take_picture_button).setOnClickListener {
            onLaunchCamera()
        }

        val ivPostImage = findViewById<ImageView>(R.id.iv_post_image)

        queryPosts()
    }

    private fun signOut(){
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
        ParseUser.logOut()
    }

    private fun goToLoginActivity(){
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun submitPost(description: String, user: ParseUser, file: File) {

        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))

        post.saveInBackground { exception ->
            if (exception != null) {
                Log.e(TAG, "Error while saving post")
                exception.printStackTrace()
                Toast.makeText(this, "Couldn't save post", Toast.LENGTH_SHORT).show()
            } else {
                Log.i(TAG, "Successfully saved post")
                //TODO: reset edit text field
                //      Reset the ImageView to empty
            }
        }
    }

    // Query for all posts in our server
    private fun queryPosts() {

        val query: ParseQuery<Post> = ParseQuery(Post::class.java)

        // Asking parse to also include the user that posted the Post (Since User is a pointer in the Post table)
        query.include(Post.KEY_USER)
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error fetching posts")
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser()?.username)
                        }
                    }
                }
            }
        })
    }



    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                Log.i(TAG, "Made it to the part where I start the activity for result")
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Within the onCreate method
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Made it to the onActivityResult thing")
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                val ivPreview = findViewById<ImageView>(R.id.iv_post_image)
                ivPreview!!.setImageBitmap(takenImage)
                Toast.makeText(this, "Showing preview", Toast.LENGTH_SHORT).show()
            } else { // Result was a failure
                Log.i(TAG, "Made it to the part where it fails")
                Toast.makeText(this, "Error taking picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}