package com.qaprosoft.carina.demo.api;

import java.io.IOException;
import java.net.*;

public class GetResponseCode {

    public int getResponseCode(String request) throws IOException {
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection.getResponseCode();
    }
}
