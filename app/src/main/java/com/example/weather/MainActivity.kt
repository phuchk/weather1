package com.example.weather

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weather.model2.Current
import com.example.weather.model2.Daily
import com.example.weather.model2.Weather
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter
    lateinit var edt_lat: EditText
    lateinit var edt_lon: EditText
    lateinit var btn: Button

    private lateinit var text1: TextView
    private lateinit var text2: TextView
    private lateinit var text7: TextView

    private lateinit var text3: TextView
    private lateinit var text4: TextView
    private lateinit var text5: TextView
    private lateinit var text6: TextView
    private lateinit var text8: TextView
    private lateinit var text9: TextView
    private lateinit var text10: TextView
    private lateinit var text11: TextView
    private lateinit var text12: TextView
    private lateinit var text13: TextView
    private lateinit var text14: TextView
    private lateinit var text15: TextView
    private lateinit var text16: TextView

    private lateinit var image: ImageView
    private lateinit var sharedPreferences: SharedPreferences


    companion object {
        const val URL: String = "https://openweathermap.org/img/wn/"

        //convert date from format Unix,utc to format dd-MM-yyyy HH:mm:ss
        fun calculateDate(int: Int): String {
            val unixSeconds: Int = int
            val date: Date = Date(unixSeconds * 1000L)
            val sdf: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            sdf.timeZone = TimeZone.getTimeZone("GMT+7")
            return sdf.format(date)
        }

        //get hour and minute with type HH:mm
        fun getHourMinute(int: Int): String {
            val time: String = calculateDate(int)
            val arr = time.split(" ")[1].split(":")
            return arr[0] + ":" + arr[1]
        }

        //get date and mon with type dd-MM
        fun getDateMonth(int: Int): String {
            val time: String = calculateDate(int)
            val arr = time.split(" ")[0].split("-")
            return arr[0] + "-" + arr[1]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences("position", Context.MODE_PRIVATE)
        init()
        val lat: String = getValueSharePre()[0]
        val lon: String = getValueSharePre()[1]
        if (lat != null && lon != null) {
            edt_lat.setText(lat)
            edt_lon.setText(lon)
            callAPI(lat, lon)
        }
        myAdapter.onItemClick = {
            handleItemClick(it)
        }
        btn.setOnClickListener {

            val lat: String = edt_lat.text.toString()
            val lon: String = edt_lon.text.toString()
            try {
                lat.toFloat()
                lon.toFloat()
                callAPI(lat, lon)
                putValueSharePre(lat, lon)
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Hãy nhập tọa độ là số", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun putValueSharePre(lat: String, lon: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("lat", lat)
        editor.putString("long", lon)
        editor.apply()
        editor.commit()
    }

    private fun getValueSharePre(): List<String> {
        val lat = sharedPreferences.getString("lat", "")
        val lon = sharedPreferences.getString("long", "")
        return listOf(lat, lon) as List<String>
    }

    private fun callAPI(lat: String, lon: String) {
        val api = RetrofitHelper.getInstance().create(myAPI::class.java)
        GlobalScope.launch(Dispatchers.Main) {
            //?lat={lat}&lon={lon}&lang=vi&units=metric&appid=${RetrofitHelper.API_KEY}
            val result = api.getWeather(lat, lon, "vi", "metric", RetrofitHelper.API_KEY)
            if (result != null) {
                val body = result.body()
                if (body != null) {
                    handleWeatherToday(body.current);
                    handleWeatherDaily(body.daily)
                    handleItemClick(body.daily[0])
                }
            }
        }
    }

    private fun handleWeatherDaily(daily: List<Daily>) {
        myAdapter.setList(daily)
    }

    private fun handleWeatherToday(current: Current) {
        val weather: Weather = current.weather[0]
        image.load(URL.plus(weather.icon + "@2x.png"))
        text1.text = weather.main
        text2.text = ceil(current.temp).toInt().toString() + " °C"
        text7.text = getHourMinute(current.dt)
    }

    fun init() {
        image = findViewById(R.id.image_weather_today)
        edt_lat = findViewById(R.id.edt_latitude)
        edt_lon = findViewById(R.id.edt_longitude)
        btn = findViewById(R.id.button_get_weather)
        text1 = findViewById(R.id.text_weather_today)
        text2 = findViewById(R.id.text_degree_weather_today)
        text7 = findViewById(R.id.text_time_update)
        text8 = findViewById(R.id.text_weather_detail)

        text3 = findViewById(R.id.text_sunrise)
        text4 = findViewById(R.id.text_feels_like)
        text5 = findViewById(R.id.text_moonrise)
        text6 = findViewById(R.id.text_humidity)
        text9 = findViewById(R.id.text_pressure)
        text10 = findViewById(R.id.text_dew_point)
        text11 = findViewById(R.id.text_wind_speed)
        text12 = findViewById(R.id.text_wind_deg)
        text13 = findViewById(R.id.text_wind_gust)
        text14 = findViewById(R.id.text_clouds)
        text15 = findViewById(R.id.text_rain)
        text16 = findViewById(R.id.text_uvi)
        recyclerView = findViewById(R.id.rev)
        myAdapter = MyAdapter()
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = myAdapter
    }

    private fun handleItemClick(daily: Daily) {
        text8.text = daily.weather[0].description
        text3.text = getHourMinute(daily.sunrise)
        text4.text = daily.feels_like.day.toString() + "°C"
        text5.text = getHourMinute(daily.moonrise)
        text6.text = daily.humidity.toString() + "%"
        text9.text = daily.pressure.toString() + "hPa"
        text10.text = daily.dew_point.toString() + "°C"
        text11.text = daily.wind_speed.toString() + "m/s"
        text12.text = daily.wind_deg.toString()
        text13.text = daily.wind_gust.toString() + "m/s"
        text14.text = daily.clouds.toString() + "%"
        text15.text = daily.rain.toString() + "mm"
        text16.text = daily.uvi.toString()
    }

}