package com.horirevens.antarankantorpos.libs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by horirevens on 11/26/16.
 */
public class RequestHandler {
    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams) {
        //creating URL
        URL url;

        //StringBuilder object to store the message retrieved from the server
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //initializing url
            url = new URL(requestURL);

            //creating htmlurl connection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            //configuring connection properties
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            //creating an output stream
            OutputStream outputStream = httpURLConnection.getOutputStream();

            //writting parameters to the request
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(getPostDataString(postDataParams));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int responeCode = httpURLConnection.getResponseCode();
            if (responeCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                stringBuilder = new StringBuilder();
                String response;

                //reading server response
                while ((response = bufferedReader.readLine()) != null) {
                    stringBuilder.append(response);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public String sendGetRequest(String requestURL) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String string;
            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public String sendGetRequestParam(String requestURL, String params) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(requestURL+params);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String string;
            while ((string = bufferedReader.readLine()) != null){
                stringBuilder.append(string);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /*public String sendGetRequestParam(String requestURL, HashMap<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(requestURL+params);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String string;
            while ((string = bufferedReader.readLine()) != null){
                stringBuilder.append(string);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }*/

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) first = false;
            else stringBuilder.append("&");

            stringBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return stringBuilder.toString();
    }
}
