package com.example.punctualityalarm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


//import com.mapquest.android.samples.JSONParser.MyCallbackInterface;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

//public class JSONParser {
public class JSONParser extends AsyncTask<String, Void, JSONObject> {
  static InputStream is = null;
  static JSONObject jObj = null;
  static String json = "";
  // constructor
  public JSONParser() {
  }
  
  public interface MyCallbackInterface {
      public void onRequestCompleted(JSONObject result);
  }

  private MyCallbackInterface mCallback;

  public JSONParser(MyCallbackInterface callback) {
      mCallback = callback;
  }
  
  public JSONObject getJSONFromUrl(String url) {
    // Making HTTP request
    try {
      // defaultHttpClient
      DefaultHttpClient httpClient = new DefaultHttpClient();
      HttpPost httpPost = new HttpPost(url);
      HttpResponse httpResponse = httpClient.execute(httpPost);
      HttpEntity httpEntity = httpResponse.getEntity();
      is = httpEntity.getContent();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          is, "iso-8859-1"), 80000);
      StringBuilder sb = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
      is.close();
      json = sb.toString();
    } catch (Exception e) {
      Log.e("Buffer Error", "Error converting result " + e.toString());
    }
    // try parse the string to a JSON object
    try {
      //jObj = new JSONObject(json);
      jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
    } catch (JSONException e) {
      Log.e("JSON Parser", "Error parsing data " + e.toString());
    }
    // return JSON String
    return jObj;
  }
  
  @Override
  protected JSONObject doInBackground(String... params) {
	String url = params[0];            
    return getJSONFromUrl(url);
  }
  
  @Override
  protected void onPostExecute(JSONObject result){
	//In here, call back to Activity or other listener that things are done
      mCallback.onRequestCompleted(result);
  }
  
}

