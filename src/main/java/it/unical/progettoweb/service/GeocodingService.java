package it.unical.progettoweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GeocodingService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public double[] geocodifica(String street, String streetNumber, String city, String cap, String province) {
        String address = street + " " + streetNumber + ", " + cap + " " + city + " " + province + ", Italy";
        String encoded = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encoded + "&key=" + apiKey;

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode body = response.getBody();

        if (body == null || !body.path("status").asText().equals("OK")) {
            throw new RuntimeException("Geocoding fallito per: " + address);
        }

        JsonNode location = body.path("results").get(0).path("geometry").path("location");
        return new double[]{ location.path("lat").asDouble(), location.path("lng").asDouble() };
    }
}