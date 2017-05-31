package pl.lodz.trackingsystem.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.WindowManager;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class ServerRequests {

    private static final String basicUrl = "http://192.168.43.204:8080";

    public static void sendCoords(final String login, final String latitude, final String longitude, final String name) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = basicUrl + "/mobile/addCoordinates";
                try{
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                    params.add("login", login);
                    params.add("name", name);
                    params.add("latitude", latitude);
                    params.add("longitude", longitude);
                    restTemplate.put(url, params);
                } catch (Exception e) {
                    e.printStackTrace(); // TODO change for logger
                }
            }
        });

        thread.start();
    }

    public static class CheckMessageTask extends AsyncTask<Void, Void, String> {

        private String login = "";
        private Context context;

        public CheckMessageTask(String login, Context context){
            this.login = login;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
                param.add("login", login);
                return restTemplate.postForObject(ServerRequests.getMessageUrl(), param, String.class);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final String message) {

            if (message.length() > 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Alarm")
                        .setMessage(message)
                        .create();

                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alertDialog.show();
            }
        }

        @Override
        protected void onCancelled() {}
    }

    public static String getLoginUrl() {
        return basicUrl + "/mobile/login";
    }

    public static String getMessageUrl() {
        return basicUrl + "/mobile/checkMessages";
    }

}
