package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date

class EarthquakeViewModel : ViewModel() {

    private val _earthquakeData = MutableLiveData<List<EarthquakeData>>()
    val earthquakeData: LiveData<List<EarthquakeData>> = _earthquakeData

    init {
        fetchData()
    }

    private fun fetchData() {
        GlobalScope.launch(Dispatchers.Main) {
            val data = fetchEarthquakeData()
            _earthquakeData.value = data
        }
    }

    suspend fun fetchEarthquakeData(): List<EarthquakeData>? {
        return withContext(Dispatchers.IO) {
            val url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2023-01-01&endtime=2024-01-01&minmagnitude=7"
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = StringBuilder()
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String? = reader.readLine()
                while (line != null) {
                    response.append(line)
                    line = reader.readLine()
                }

                val earthquakeList = mutableListOf<EarthquakeData>()
                val jsonObject = JSONObject(response.toString())
                val featuresArray = jsonObject.getJSONArray("features")
                for (i in 0 until featuresArray.length()) {
                    val featureObj = featuresArray.getJSONObject(i)
                    val propertiesObj = featureObj.getJSONObject("properties")
                    val geometryObj = featureObj.getJSONObject("geometry")
                    val coordinatesArray = geometryObj.getJSONArray("coordinates")

                    val magnitude = propertiesObj.getDouble("mag")
                    val timestamp = propertiesObj.getLong("time")
                    val updatedstamp = propertiesObj.getLong("updated")
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val date = Date(timestamp)
                    val formattedDateTime = sdf.format(date)

                    val updateDate = Date(updatedstamp)
                    val formattedUpdatedDateTime = sdf.format(updateDate)
                    val location = propertiesObj.getString("place")
                    val latitude = coordinatesArray.getDouble(1)
                    val longitude = coordinatesArray.getDouble(0)
                    val depth = coordinatesArray.getDouble(2)
                    val earthquakeData = EarthquakeData(magnitude, formattedDateTime,formattedUpdatedDateTime, location, latitude, longitude, depth)
                    earthquakeList.add(earthquakeData)
                }
                earthquakeList
            } else {
                null
            }
        }
    }
}
