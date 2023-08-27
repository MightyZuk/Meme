package com.example.meme

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request.Method.GET
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.meme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentMemeUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        window.statusBarColor = this.resources.getColor(R.color.red)

        //action bar color below top
        supportActionBar?.setBackgroundDrawable((ColorDrawable(Color.parseColor("#ED2C2C"))))

        //hide title
        supportActionBar?.title = null

        //show custom view in action bar
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        setContentView(binding.root)

        loadMeme()

        binding.button.setOnClickListener{loadMeme()}
        binding.shareCurrentMeme.setOnClickListener {
            val sendIntent : Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,"Check out this meme $currentMemeUrl")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent,"Share via")
            startActivity(shareIntent)
        }

    }

    private fun loadMeme(){
        val url = "https://meme-api.com/gimme"
        binding.loading.visibility = View.VISIBLE
        val jsonObjectRequest = JsonObjectRequest(GET,url,null, {
            currentMemeUrl = it.getString("url")
            Glide.with(this).load(currentMemeUrl).listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.loading.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.loading.visibility = View.GONE
                    return false
                }
            }).into(binding.meme)
        },
            {
                Toast.makeText(this,"Error Occurred",Toast.LENGTH_SHORT).show()
            })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

}