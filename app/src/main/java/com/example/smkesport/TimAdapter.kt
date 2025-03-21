package com.example.smkesport

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.net.URL

class TimAdapter(val listTim: MutableList<TimModel>, val context: Context): RecyclerView.Adapter<TimAdapter.TimAdapterView>() {

    class TimAdapterView(val view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.logoTim)
        val nama: TextView = view.findViewById(R.id.namaTim)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimAdapterView {
        val layout = LayoutInflater.from(context).inflate(R.layout.tim_listitem, parent, false)
        return TimAdapterView(layout)
    }

    override fun getItemCount(): Int {
        return listTim.size
    }

    override fun onBindViewHolder(holder: TimAdapterView, position: Int) {
        val tim = listTim[position]

        UpdateUI(holder, tim)

    }

    private fun UpdateUI(holder: TimAdapterView, tim: TimModel) = runBlocking {
        launch(Dispatchers.IO){

            val kon = URL(Variabel.url + "/logos/" + tim.logo256).openConnection() as HttpURLConnection
            val inputStream = kon.inputStream
            val gambar = BitmapFactory.decodeStream(inputStream)
            (context as Activity).runOnUiThread{
                holder.image.setImageBitmap(gambar)
                holder.nama.text = tim.name
            }

        }


    }


}