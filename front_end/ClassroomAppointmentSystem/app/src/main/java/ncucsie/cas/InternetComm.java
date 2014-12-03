package ncucsie.cas;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetComm {
    Context mContext;
    public InternetComm(Context mContext){
        this.mContext = mContext;
    }


    public class urlWithJSON {
        public urlWithJSON(String i, JSONObject j){
            url = i;
            data = j;
        }
        public String url;
        public JSONObject data;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private class ApiRequest extends AsyncTask<urlWithJSON, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(urlWithJSON... input) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return postRequest(input[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            //TODO: remove placeholder
            //perform update action after successful update or when failed to update, notify the user immediately
        }


    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private JSONObject postRequest(urlWithJSON input) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(input.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(input.data.toString());
            writer.flush();
            writer.close();
            os.close();



            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(Constant.DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();
            // Convert the InputStream into a string
            try {
                return InputStreamToJSONObject(is);
            }
            catch (Exception JSONException) {
                return null;
            }
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private static JSONObject InputStreamToJSONObject(InputStream inputStream)
            throws JSONException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        try {
            while ((line = bufferedReader.readLine()) != null)
                result += line;
        } catch (Exception IOException){
            Log.i(Constant.DEBUG_TAG, "IO exception in bufferedReader.readLine()");
        }
        return new JSONObject(result);
    }
}
