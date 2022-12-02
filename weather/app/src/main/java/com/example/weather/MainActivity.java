package com.example.weather;
import java.net.HttpURLConnection;
import java.net.URL;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;


public class MainActivity extends AppCompatActivity {

    private TextView temperature;
    private TextView FeeksLike;
    private TextView pressureMm;
    private TextView humidityPercent;
    private TextView WindWay;
    private TextView weatherDescription;
    //private TextView City;
    private ImageView weatherIcon;

    private EditText GetCity;

    private TextView id_forecastDay1;
    private TextView id_forecastDay2;
    private TextView id_forecastDay3;
    private TextView id_forecastDay4;
    private TextView id_forecastDay5;

    private TextView id_forecastTemp1;
    private TextView id_forecastTemp2;
    private TextView id_forecastTemp3;
    private TextView id_forecastTemp4;
    private TextView id_forecastTemp5;

    private ImageView id_forecastIcon1;
    private ImageView id_forecastIcon2;
    private ImageView id_forecastIcon3;
    private ImageView id_forecastIcon4;
    private ImageView id_forecastIcon5;

    private TextView id_forecastDesc1;
    private TextView id_forecastDesc2;
    private TextView id_forecastDesc3;
    private TextView id_forecastDesc4;
    private TextView id_forecastDesc5;

    private Button search;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherDescription = findViewById(R.id.weatherDescription);
        temperature = findViewById(R.id.temperature);
        FeeksLike = findViewById(R.id.FeeksLike);
        pressureMm = findViewById(R.id.pressureMm);
        humidityPercent = findViewById(R.id.humidityPercent);
        WindWay = findViewById(R.id.WindWay);

        GetCity=findViewById(R.id.GetCity);

        id_forecastDay1=findViewById(R.id.id_forecastDay1);
        id_forecastDay2=findViewById(R.id.id_forecastDay2);
        id_forecastDay3=findViewById(R.id.id_forecastDay3);
        id_forecastDay4=findViewById(R.id.id_forecastDay4);
        id_forecastDay5=findViewById(R.id.id_forecastDay5);

        id_forecastTemp1=findViewById(R.id.id_forecastTemp1);
        id_forecastTemp2=findViewById(R.id.id_forecastTemp2);
        id_forecastTemp3=findViewById(R.id.id_forecastTemp3);
        id_forecastTemp4=findViewById(R.id.id_forecastTemp4);
        id_forecastTemp5=findViewById(R.id.id_forecastTemp5);

        id_forecastIcon1=findViewById(R.id.id_forecastIcon1);
        id_forecastIcon2=findViewById(R.id.id_forecastIcon2);
        id_forecastIcon3=findViewById(R.id.id_forecastIcon3);
        id_forecastIcon4=findViewById(R.id.id_forecastIcon4);
        id_forecastIcon5=findViewById(R.id.id_forecastIcon5);

        id_forecastDesc1=findViewById(R.id.id_forecastDesc1);
        id_forecastDesc2=findViewById(R.id.id_forecastDesc2);
        id_forecastDesc3=findViewById(R.id.id_forecastDesc3);
        id_forecastDesc4=findViewById(R.id.id_forecastDesc4);
        id_forecastDesc5=findViewById(R.id.id_forecastDesc5);

        Toast toast = Toast.makeText(getApplicationContext(),
                "Неверно введён город!", Toast.LENGTH_SHORT);


        search=findViewById(R.id.search);
        weatherIcon = findViewById(R.id.weatherIcon);
        //City = findViewById(R.id.City);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String CityForApiReq = GetCity.getText().toString();
                    SharedPreferences CityOnLoad = getSharedPreferences("AppWeatherCity", MODE_PRIVATE);
                    SharedPreferences.Editor editor = CityOnLoad.edit();
                    editor.remove("City");
                    editor.putString("City",CityForApiReq);
                    editor.apply();
                    String urlforecast="https://api.openweathermap.org/data/2.5/forecast?q="+CityForApiReq+"&appid=3fdd34a76aeb9f0de16c5fd99d08eef5&units=metric&lang=ru";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="+CityForApiReq+"&APPID=3fdd34a76aeb9f0de16c5fd99d08eef5&units=metric&lang=ru";
                    new GetURLData().execute(url);
                    new GetURLDataForecast().execute(urlforecast);

                }
                catch (NullPointerException e){
                    toast.show();}
            }
        });

            SharedPreferences CityOnLoad = getSharedPreferences("AppWeatherCity", MODE_PRIVATE);
            GetCity.setText(CityOnLoad.getString("City",""));
            String urlforecast="https://api.openweathermap.org/data/2.5/forecast?q="+GetCity.getText().toString()+"&appid=3fdd34a76aeb9f0de16c5fd99d08eef5&units=metric&lang=ru";
            String url = "https://api.openweathermap.org/data/2.5/weather?q="+GetCity.getText().toString()+"&APPID=3fdd34a76aeb9f0de16c5fd99d08eef5&units=metric&lang=ru";
            new GetURLData().execute(url);
            new GetURLDataForecast().execute(urlforecast);
        }

    private class GetURLData extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            URL url = null;
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    return buffer.toString();

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast toast2 = Toast.makeText(getApplicationContext(),
                    "Неверно введён город!", Toast.LENGTH_SHORT);
            try {


                JSONObject jsonObjec = new JSONObject(result);

                temperature.setText(jsonObjec.getJSONObject("main").getInt("temp") + "C°");
                FeeksLike.setText("Ощущается: " + jsonObjec.getJSONObject("main").getInt("feels_like") + "C°");
                pressureMm.setText("Давление: " + jsonObjec.getJSONObject("main").getInt("pressure") + "мм");
                humidityPercent.setText("Влажность: " + jsonObjec.getJSONObject("main").getInt("humidity") + "%");


                if (jsonObjec.getJSONObject("wind").getInt("deg") < 175 & jsonObjec.getJSONObject("wind").getInt("deg") > 95) {
                    WindWay.setText("Ветер: " + jsonObjec.getJSONObject("wind").getInt("speed") + "м/с " + " Ю/В");
                }
                if (jsonObjec.getJSONObject("wind").getInt("deg") < 85 & jsonObjec.getJSONObject("wind").getInt("deg") > 5) {
                    WindWay.setText("Ветер: " + jsonObjec.getJSONObject("wind").getInt("speed") + "м/с " + " С/В");
                }
                if (jsonObjec.getJSONObject("wind").getInt("deg") < 265 & jsonObjec.getJSONObject("wind").getInt("deg") > 185) {
                    WindWay.setText("Ветер: " + jsonObjec.getJSONObject("wind").getInt("speed") + "м/с " + " Ю/З");
                }
                if (jsonObjec.getJSONObject("wind").getInt("deg") < 3559 & jsonObjec.getJSONObject("wind").getInt("deg") > 275) {
                    WindWay.setText("Ветер: " + jsonObjec.getJSONObject("wind").getInt("speed") + "м/с " + " С/З");
                }
                if (jsonObjec.getJSONObject("wind").getInt("deg") > 175 & jsonObjec.getJSONObject("wind").getInt("deg") < 185) {
                    WindWay.setText("Ветер: " + jsonObjec.getJSONObject("wind").getInt("speed") + "м/с " + " Ю");
                }
                if (jsonObjec.getJSONObject("wind").getInt("deg") > 355 & jsonObjec.getJSONObject("wind").getInt("deg") < 5) {
                    WindWay.setText("Ветер: " + jsonObjec.getJSONObject("wind").getInt("speed") + "м/с " + " С");
                }
                if (jsonObjec.getJSONObject("wind").getInt("deg") > 85 & jsonObjec.getJSONObject("wind").getInt("deg") < 95) {
                    WindWay.setText("Ветер: " + jsonObjec.getJSONObject("wind").getInt("speed") + "м/с " + " В");
                }
                if (jsonObjec.getJSONObject("wind").getInt("deg") < 265 & jsonObjec.getJSONObject("wind").getInt("deg") < 275) {
                    WindWay.setText("Ветер: " + jsonObjec.getJSONObject("wind").getInt("speed") + "м/с " + " З");
                }

                JSONArray jsonArray = jsonObjec.getJSONArray("weather");
                JSONObject pogoda = jsonArray.getJSONObject(0);
                String pog = pogoda.getString("description");
                weatherDescription.setText(pog);

                //String cityname = jsonObjec.getString("name");
                //City.setText(cityname);

                JSONArray jsonIcoArray = jsonObjec.getJSONArray("weather");
                JSONObject icon = jsonIcoArray.getJSONObject(0);
                String WiD = icon.getString("icon");
                weatherDescription.setText(pog);

                String ClearSkyUrl = "http://f0746619.xsph.ru/" + WiD + ".png";
                Picasso.get().load(ClearSkyUrl).into(weatherIcon);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException ex){
                toast2.show();

            }
        }
    }
    private class GetURLDataForecast extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            URL urlforecast = null;
            try {
                urlforecast = new URL(strings[0]);
                connection = (HttpURLConnection) urlforecast.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    return buffer.toString();

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast toast3 = Toast.makeText(getApplicationContext(),
                    "Неверно введён город!", Toast.LENGTH_SHORT);

            try {

                JSONObject jsonObjec = new JSONObject(result);

                id_forecastDay1.setText(jsonObjec.getJSONArray("list").getJSONObject(10).getString("dt_txt"));
                id_forecastDay2.setText(jsonObjec.getJSONArray("list").getJSONObject(18).getString("dt_txt"));
                id_forecastDay3.setText(jsonObjec.getJSONArray("list").getJSONObject(26).getString("dt_txt"));
                id_forecastDay4.setText(jsonObjec.getJSONArray("list").getJSONObject(34).getString("dt_txt"));
                id_forecastDay5.setText(jsonObjec.getJSONArray("list").getJSONObject(39).getString("dt_txt"));

                id_forecastTemp1.setText(jsonObjec.getJSONArray("list").getJSONObject(10).getJSONObject("main").getString("temp"));
                id_forecastTemp2.setText(jsonObjec.getJSONArray("list").getJSONObject(18).getJSONObject("main").getString("temp"));
                id_forecastTemp3.setText(jsonObjec.getJSONArray("list").getJSONObject(26).getJSONObject("main").getString("temp"));
                id_forecastTemp4.setText(jsonObjec.getJSONArray("list").getJSONObject(34).getJSONObject("main").getString("temp"));
                id_forecastTemp5.setText(jsonObjec.getJSONArray("list").getJSONObject(39).getJSONObject("main").getString("temp"));

                id_forecastDesc1.setText(jsonObjec.getJSONArray("list").getJSONObject(10).getJSONArray("weather").getJSONObject(0).getString("description"));
                id_forecastDesc2.setText(jsonObjec.getJSONArray("list").getJSONObject(18).getJSONArray("weather").getJSONObject(0).getString("description"));
                id_forecastDesc3.setText(jsonObjec.getJSONArray("list").getJSONObject(26).getJSONArray("weather").getJSONObject(0).getString("description"));
                id_forecastDesc4.setText(jsonObjec.getJSONArray("list").getJSONObject(34).getJSONArray("weather").getJSONObject(0).getString("description"));
                id_forecastDesc5.setText(jsonObjec.getJSONArray("list").getJSONObject(39).getJSONArray("weather").getJSONObject(0).getString("description"));

                String ico1 = jsonObjec.getJSONArray("list").getJSONObject(10).getJSONArray("weather").getJSONObject(0).getString("icon");
                String ico2 = jsonObjec.getJSONArray("list").getJSONObject(18).getJSONArray("weather").getJSONObject(0).getString("icon");
                String ico3 = jsonObjec.getJSONArray("list").getJSONObject(26).getJSONArray("weather").getJSONObject(0).getString("icon");
                String ico4 = jsonObjec.getJSONArray("list").getJSONObject(34).getJSONArray("weather").getJSONObject(0).getString("icon");
                String ico5 = jsonObjec.getJSONArray("list").getJSONObject(39).getJSONArray("weather").getJSONObject(0).getString("icon");

                String day1url = "http://f0746619.xsph.ru/" + ico1 + ".png";
                String day2url = "http://f0746619.xsph.ru/" + ico2 + ".png";
                String day3url = "http://f0746619.xsph.ru/" + ico3 + ".png";
                String day4url = "http://f0746619.xsph.ru/" + ico4 + ".png";
                String day5url = "http://f0746619.xsph.ru/" + ico5 + ".png";

                Picasso.get().load(day1url).into(id_forecastIcon1);
                Picasso.get().load(day2url).into(id_forecastIcon2);
                Picasso.get().load(day3url).into(id_forecastIcon3);
                Picasso.get().load(day4url).into(id_forecastIcon4);
                Picasso.get().load(day5url).into(id_forecastIcon5);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException exs){
                toast3.show();
            }
        }
    }
}

