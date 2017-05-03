package pl.lodz.trackingsystem.utils;

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

    public static String getLoginUrl() {
        return basicUrl + "/mobile/login";
    }

}
