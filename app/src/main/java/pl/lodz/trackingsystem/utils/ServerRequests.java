package pl.lodz.trackingsystem.utils;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class ServerRequests {

    private static final String basicUrl = "http://192.168.2.6:8080";

    public static void sendCoords(final String latitude, final String longitude) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = basicUrl + "/user/addCoordinates";
                try{
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                    params.add("name", "Mateusz");
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

}
