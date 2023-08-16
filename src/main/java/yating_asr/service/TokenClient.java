package yating_asr.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import yating_asr.constants.Language;

public class TokenClient {
    static String asrApiUrl;
    static String asrApiKey;

    public TokenClient(String url, String key) {
        asrApiUrl = url;
        asrApiKey = key;
    }

    public String GetToken(String pipeline, String language) throws Exception {
        try {
            // parameter validation
            validation(language);

            // mapping http request body
            String bodyString = bodyGenerator(pipeline, language);

            // get auth token
            JSONObject tokenResponse = sendHttpRequest(bodyString);

            // validate response
            Boolean success = (Boolean) tokenResponse.get("success");
            if (!success) {
                throw new Exception("http request fallure: get token error");
            }

            return (String) tokenResponse.get("auth_token");
        } catch (Exception e) {
            throw e;
        }
    }

    private static void validation(String language) throws Exception {
        if (!Language.validate(language)) {
            throw new Exception("language: " + language + " is not allowed.");
        }
    }

    private static String bodyGenerator(String pipeline, String language) {
        JSONObject optionsObject = new JSONObject();
        optionsObject.put("lang", language);

        JSONObject bodyObject = new JSONObject();
        bodyObject.put("pipeline", pipeline);
        bodyObject.put("options", optionsObject);

        return bodyObject.toString();
    }

    private static JSONObject sendHttpRequest(String bodyString) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(asrApiUrl))
                .header("Content-Type", "application/json")
                .header("key", asrApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(bodyString))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("http request fallure: get token error");
        }

        JSONParser parser = new JSONParser();

        return (JSONObject) parser.parse(response.body());
    }
}
