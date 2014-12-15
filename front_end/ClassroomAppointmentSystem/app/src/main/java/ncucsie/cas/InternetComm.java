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
    public interface ApiResponse {
        void postProcessing(JSONObject result);
    }


    static public class userStruct {
        public userStruct(boolean a, String b, String c){
            result = a;
            sessionid = b;
            failed_message = c;
        }
        public boolean result;
        public String sessionid;
        public String failed_message;
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

    public static class ApiRequest extends AsyncTask<urlWithJSON, Void, JSONObject> {
        private String request = null;
        private String delete_request = null;
        ApiRequest(String request, String delete_request){
            this.request = request;
            this.delete_request = delete_request;
        }
        ApiRequest(String request){
            this.request = request;
        }

        public ApiResponse delegate=null;

        @Override
        protected JSONObject doInBackground(urlWithJSON... input) {


            try {
                return postRequest(input[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if(result != null) {
                Log.d("Server Response", result.toString());
                if (delegate != null) {
                    try {
                        JSONObject data = result.put(Constant.USER_REQUEST, request);
                        if(delete_request != null){
                            data.put(Constant.DELETE_REQUEST, delete_request);
                        }
                        delegate.postProcessing(data);
                    }
                    catch (JSONException e){
                        Log.d("JSONException at onPostExecute", e.toString());
                    }
                }
            }
            else {
                Log.d("Received no response from server", " ");
            }
            //perform update action after successful update or when failed to update, notify the user immediately
        }


    }

    public urlWithJSON createURLRequest(int method, JSONObject data) {
        String url;
        url  = "http://";
        url += Constant.SERVER_DOMAIN_NAME;
        url += ":";
        url += Constant.SERVER_PORT;
        url += Constant.SERVER_ROOT;
        urlWithJSON result;
        if(method == Constant.LOGIN){
            url += Constant.LOGIN_PAGE;
            result = new urlWithJSON(url,data);
        }
        else if(method == Constant.LOGOUT){
            url += Constant.LOGOUT_PAGE;
            result = new urlWithJSON(url,data);
        }
        else if(method == Constant.VIEW_APPOINTMENT){
            url += Constant.VIEW_APPOINTMENT_PAGE;
            result = new urlWithJSON(url, data);
        }
        else if(method == Constant.ADD_APPOINTMENT){
            url += Constant.ADD_APPOINTMENT_PAGE;
            result = new urlWithJSON(url, data);
        }
        else if(method == Constant.MODIFY_APPOINTMENT){
            url += Constant.MODIFY_APPOINTMENT_PAGE;
            result = new urlWithJSON(url, data);
        }
        else if(method == Constant.DELETE_APPOINTMENT){
            url += Constant.DELETE_APPOINTMENT_PAGE;
            result = new urlWithJSON(url, data);
        }
        else if(method == Constant.MY_APPOINTMENT){
            url += Constant.MY_APPOINTMENT_PAGE;
            result = new urlWithJSON(url, data);
        }
        else{
            result = null;
        }
        return result;
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    public static JSONObject postRequest(urlWithJSON input) throws IOException {
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
            writer.write("data=" + input.data.toString());
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
            Log.d(Constant.DEBUG_TAG, "IO exception in bufferedReader.readLine()");
        }
        return new JSONObject(result);
    }
}
