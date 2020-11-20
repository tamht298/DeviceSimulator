package vn.com.tma.connectserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ClientToServer {

    private final String URL_CONNECTION_DEVICE = "http://localhost:8080/ManageDevices/api/devices";

    public void registerDevice(String deviceInputString) {
        try {
            URL url = new URL(URL_CONNECTION_DEVICE);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");


            OutputStream os = conn.getOutputStream();
            os.write(deviceInputString.getBytes());
            os.flush();

//            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + conn.getResponseCode());
//            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();

        } catch (
                MalformedURLException e) {

            e.printStackTrace();

        } catch (
                IOException e) {

            e.printStackTrace();

        }
    }
}
