package com.example.newsfresh

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsfresh.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var binding: ActivityMainBinding
   private lateinit var mAdapter:NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        fetchData()
        mAdapter= NewsListAdapter( this)
        binding.recyclerView.adapter = mAdapter


    }
    private  fun fetchData(){

        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=0e3cc03a35044a518dbb12d5c7a1d4fe"

// Request a string response from the provided URL.
        val jsonObjectRequest = object: JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener {
                // Display the first 500 characters of the response string.
              val newsJsonArray = it.getJSONArray("articles")
                val newsArray =ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {

            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["User-Agent"]="Mozilla/5.0"
                return headers
            }
        }

// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {

        val builder=CustomTabsIntent.Builder();
        val customTabsIntent =builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }
}