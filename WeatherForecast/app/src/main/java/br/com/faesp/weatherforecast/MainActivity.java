package br.com.faesp.weatherforecast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerCity;
    private Spinner spinnerUF;
    private Button findButton;

    private ArrayList<FederalUnities> ufList = new ArrayList<FederalUnities>();
    private ArrayList<Cities> citiesList = new ArrayList<Cities>();
    private Cities selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Calendar calendar = Calendar.getInstance(new Locale("pt_BR"));

        spinnerUF = (Spinner) findViewById(R.id.spinnerUF);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);

        ufList.add(new FederalUnities(1, "Teste"));

        SearchUfs searchUfs = new SearchUfs();
        searchUfs.execute("https://servicodados.ibge.gov.br/api/v1/localidades/estados");

        ArrayAdapter ufAdapter = new ArrayAdapter<FederalUnities>(this,  R.layout.spinner_dropdown, R.id.textView, ufList);
        spinnerUF.setAdapter(ufAdapter);

        spinnerUF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                FederalUnities selectedUF = (FederalUnities) parentView.getItemAtPosition(position);

                SearchCities searchCities = new SearchCities();
                searchCities.execute("https://servicodados.ibge.gov.br/api/v1/localidades/estados/"+ selectedUF.id + "/municipios");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCity = (Cities) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        findButton = (Button) findViewById(R.id.buttonFind);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedCity != null) {
                    SearchWeather sw = new SearchWeather();
                    String cityNameFormatted = selectedCity.name.trim().toLowerCase();

                    sw.execute("https://api.weatherbit.io/v2.0/current?city=" + cityNameFormatted + "&country=BR&key=ebb2642870214a8d86f7a69cbafe10cd&lang=pt");
                }
            }
        });
    }

    public void updateListCities(ArrayList<Cities> newCitiesList) throws JSONException {
        citiesList = newCitiesList;

        ArrayAdapter cityAdapter = new ArrayAdapter<Cities>(this,  R.layout.spinner_dropdown, R.id.textView, citiesList);
        spinnerCity.setAdapter(cityAdapter);
    }

    public void updateListUfs(ArrayList<FederalUnities> newUfsList) throws JSONException {
        ufList = newUfsList;

        ArrayAdapter ufAdapter = new ArrayAdapter<FederalUnities>(this,  R.layout.spinner_dropdown, R.id.textView, ufList);
        spinnerUF.setAdapter(ufAdapter);
    }

    protected class SearchCities extends AsyncTask<String, Void, String> {
        private URL requestURL;

        @Override
        protected String doInBackground(String... strings) {
            try {
                this.requestURL = new URL(strings[0]);

                HttpURLConnection con = (HttpURLConnection) this.requestURL.openConnection();
                con.setRequestMethod("GET");

                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line;
                StringBuffer out = new StringBuffer();

                while ((line = br.readLine()) != null) {
                    out.append(line + "\n");
                }

                is.close();
                return out.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray json = parseJSONToArray(result);
                ArrayList<Cities> reloadCities = new ArrayList<Cities>();

                for(int i = 0; i < json.length(); i++){
                    JSONObject jsonObject = parseJSON(json.get(i).toString());
                    Cities city = new Cities(Long.valueOf(jsonObject.getString("id")), jsonObject.getString("nome"));

                    reloadCities.add(city);
                }

                updateListCities(reloadCities);
            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }
        }

        //O método abaixo serve para ler o JSON recebido e processar os itens contidos no mesmo
        private JSONArray parseJSONToArray(String data) {
            try {
                JSONArray jsonObject = new JSONArray(data);

                return jsonObject;
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
                return null;
            }
        }

        //O método abaixo serve para ler o JSON recebido e processar os itens contidos no mesmo
        private JSONObject parseJSON(String data) {
            try {
                JSONObject jsonObject = new JSONObject(data);

                return jsonObject;
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
                return null;
            }
        }
    }

    protected class SearchUfs extends AsyncTask<String, Void, String> {
        private URL requestURL;

        @Override
        protected String doInBackground(String... strings) {
            try {
                this.requestURL = new URL(strings[0]);

                HttpURLConnection con = (HttpURLConnection) this.requestURL.openConnection();
                con.setRequestMethod("GET");

                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line;
                StringBuffer out = new StringBuffer();

                while ((line = br.readLine()) != null) {
                    out.append(line + "\n");
                }

                is.close();
                return out.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray json = parseJSONToArray(result);
                ArrayList<FederalUnities> reloadUfs = new ArrayList<FederalUnities>();

                for (int i = 0; i < json.length(); i++) {
                    JSONObject jsonObject = parseJSON(json.get(i).toString());
                    FederalUnities uf = new FederalUnities(Long.valueOf(jsonObject.getString("id")), jsonObject.getString("nome"));

                    reloadUfs.add(uf);
                }

                updateListUfs(reloadUfs);
            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }
        }

        //O método abaixo serve para ler o JSON recebido e processar os itens contidos no mesmo
        private JSONArray parseJSONToArray(String data) {
            try {
                JSONArray jsonObject = new JSONArray(data);

                return jsonObject;
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
                return null;
            }
        }

        //O método abaixo serve para ler o JSON recebido e processar os itens contidos no mesmo
        private JSONObject parseJSON(String data) {
            try {
                JSONObject jsonObject = new JSONObject(data);

                return jsonObject;
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
                return null;
            }
        }
    }

    protected class SearchWeather extends AsyncTask<String, Void, String> {
        private URL requestURL;

        @Override
        protected String doInBackground(String... strings) {
            try {
                this.requestURL = new URL(strings[0]);

                HttpURLConnection con = (HttpURLConnection) this.requestURL.openConnection();
                con.setRequestMethod("GET");

                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line;
                StringBuffer out = new StringBuffer();

                while ((line = br.readLine()) != null) {
                    out.append(line + "\n");
                }

                is.close();
                return out.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                ArrayList<WeatherForecast> weatherList = new ArrayList<WeatherForecast>();

                JSONObject jsonObject = parseJSON(result);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject data = parseJSON(jsonArray.get(i).toString());
                    WeatherForecast wf = new WeatherForecast(data.getDouble("temp"),
                            data.getDouble("precip"),
                            data.getDouble("clouds"),
                            data.getJSONObject("weather").getString("description"));
                    weatherList.add(wf);
                }

                Intent intent = new Intent(MainActivity.this, ResultWeatherForecast.class);
                intent.putExtra("ListWeather", weatherList);
                startActivity(intent);
            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }
        }

        //O método abaixo serve para ler o JSON recebido e processar os itens contidos no mesmo
        private JSONObject parseJSON(String data) {
            try {
                JSONObject jsonObject = new JSONObject(data);

                return jsonObject;
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
                return null;
            }
        }
    }
}