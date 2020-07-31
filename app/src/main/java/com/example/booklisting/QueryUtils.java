package com.example.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class QueryUtils {

    private QueryUtils() {
    }

    public static boolean CheckInternetConnection(Context context){

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.isConnectedOrConnecting();
    }

    public static ArrayList<Book> extractBooks(String jsonResponse) {
        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookJSON = jsonArray.getJSONObject(i);
                JSONObject volumeInfo = bookJSON.getJSONObject("volumeInfo");


                String title = volumeInfo.getString("title");
                String author = "";

                if(volumeInfo.has("authors")){
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int j = 0; j < authorsArray.length(); j++){
                        stringBuilder.append(authorsArray.get(j));
                        if (j+1 < authorsArray.length()){
                            stringBuilder.append(", ");
                        }
                    }
                    author = stringBuilder.toString();
                } else {
                    author = volumeInfo.getString("publisher");
                }
                String description = "";
                if(volumeInfo.has("description")){
                    description = volumeInfo.getString("description");
                } else {
                    description = "This book has no description";
                }
                Book book = new Book(title,author,description);
                books.add(book);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return books;
    }

    public static String makeHttpRequest(String urlString) throws IOException {

        String jsonResponse = "";

        if (urlString == null){
            return  jsonResponse;
        }

        URL url = createUrl(urlString);
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            Log.e("", "makeHttpRequest: ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("", "Error with creating URL", exception);
            return null;
        }
        return url;
    }


}
