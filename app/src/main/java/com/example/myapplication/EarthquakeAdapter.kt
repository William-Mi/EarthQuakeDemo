package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class EarthquakeAdapter(private val earthquakeList: List<EarthquakeData>) : RecyclerView.Adapter<EarthquakeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootView:View = itemView.findViewById(R.id.rootLayout);
        val magnitudeTextView: TextView = itemView.findViewById(R.id.magnitudeTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val updatedTextView: TextView = itemView.findViewById(R.id.updatedTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_earthquake, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val earthquakeData = earthquakeList[position]
        holder.magnitudeTextView.text = "Magnitude: ${earthquakeData.magnitude}"
        if (earthquakeData.magnitude >= 7.5) {
            holder.rootView.setBackgroundResource(R.color.red)
        } else {
            holder.rootView.setBackgroundResource(R.color.blue)
        }
        holder.locationTextView.text = "Place: ${earthquakeData.location}"
        holder.timeTextView.text = "Time: ${earthquakeData.formattedDateTime}"
        holder.updatedTextView.text = "Updated: ${earthquakeData.updatedDateTime}"
        holder.setIsRecyclable(false)

        holder.itemView.setOnClickListener {
            // Open secondary activity with location on map
//            val intent = Intent(holder.itemView.context, MapActivity::class.java).apply {
//                putExtra("latitude", earthquakeData.latitude)
//                putExtra("longitude", earthquakeData.longitude)
//            }
//            holder.itemView.context.startActivity(intent)

            openGoogleMaps(earthquakeData.latitude, earthquakeData.longitude,  earthquakeData.location, 5f, holder.itemView.context)
        }
    }


    fun openGoogleMaps(latitude: Double, longitude: Double, address: String, zoomLevel: Float, context: Context) {
        val uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f(%s)&z=%f", latitude, longitude, latitude, longitude, address, zoomLevel)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            val mapsUrl = String.format(Locale.ENGLISH, "https://www.google.com/maps/search/?api=1&query=%f,%f", latitude, longitude)
            val mapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl))
            if (mapsIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapsIntent)
            } else {
                Toast.makeText(context, "can not open map", Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun getItemCount(): Int {
        return earthquakeList.size
    }
}