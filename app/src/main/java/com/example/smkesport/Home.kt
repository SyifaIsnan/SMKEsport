package com.example.smkesport

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smkesport.databinding.ActivityHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listTim: MutableList<TimModel> = mutableListOf()

        lifecycleScope.launch(Dispatchers.IO){

            val kon = URL(Variabel.url + "/api/teams").openConnection() as HttpURLConnection //
            val inputString = kon.inputStream.bufferedReader().readText() //
            val jsonArray = JSONArray(inputString)
            for(i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                listTim.add(TimModel(
                    id = jsonObject.getString("id"),
                    name = jsonObject.getString("name"),
                    about = jsonObject.getString("about"),
                    kills = jsonObject.getString("kills"),
                    deaths = jsonObject.getString("deaths"),
                    assists = jsonObject.getString("assists"),
                    gold = jsonObject.getString("gold"),
                    damage = jsonObject.getString("damage"),
                    lordKills = jsonObject.getString("lordKills"),
                    tortoiseKills = jsonObject.getString("tortoiseKills"),
                    towerDestroy = jsonObject.getString("towerDestroy"),
                    logo256 = jsonObject.getString("logo256"),
                    logo500 = jsonObject.getString("logo500"),

                ))
            }
            runOnUiThread {
                binding.RecyclerView.layoutManager = GridLayoutManager(this@Home, 2)
                binding.RecyclerView.adapter = TimAdapter(listTim, this@Home)
            }


        }

        binding.username.text = "Halo, " + Variabel.username + " 👋"

        binding.searchView.addTextChangedListener(
            object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    filter(s.toString(), listTim)
                }

            }
        )



    }

    private fun filter(string: String, listTim: MutableList<TimModel>) {
        val newList = mutableListOf<TimModel>()
        for(tim in listTim){
            if(tim.name.contains(string, ignoreCase = true)){
                newList.add(tim)  
            }
        }
        runOnUiThread {
            binding.RecyclerView.layoutManager = GridLayoutManager(this@Home, 2)
            binding.RecyclerView.adapter = TimAdapter(newList, this@Home)
        }
    }
}