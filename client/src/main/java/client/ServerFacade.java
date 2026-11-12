package client;

import com.google.gson.Gson;
import register.RegisterRequest;
import register.RegisterResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final String serverUrl;

    public ServerFacade(int port){
        serverUrl = "http://localhost:" + port;
    }

    public RegisterResult register(String username, String password, String email) throws ResponseException {
        HttpRequest httpRequest = buildRequest("POST", "/user", new RegisterRequest(username, password, email), null);
        HttpResponse<String> httpResponse = sendRequest(httpRequest);
        return handleResponse(httpResponse, RegisterResult.class);
    }

    public void clear() throws ResponseException {
        HttpRequest httpRequest = buildRequest("DELETE", "/db", null, null);
        HttpResponse<String> httpResponse = sendRequest(httpRequest);
        handleResponse(httpResponse, null);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String auth){
        var request = HttpRequest.newBuilder().uri(URI.create(serverUrl + path)).method(method, makeRequestBody(body));
        if(body != null){
            request.setHeader("Content-Type", "application/json");
        }
        if(auth != null){
            request.setHeader("authorization", auth);
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request){
        if(request != null){
            return HttpRequest.BodyPublishers.ofString(gson.toJson(request));
        }
        return HttpRequest.BodyPublishers.noBody();
    }

    private HttpResponse<String> sendRequest (HttpRequest request) throws ResponseException{
        try{
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch(Exception e){
            throw new ResponseException("Unable to connect to the server");
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if(!isSuccessful(status)){
            throw new ResponseException(readErrorMessage(response.body()));
        }
        if(responseClass == null){
            return null;
        }
        return gson.fromJson(response.body(), responseClass);
    }

    private boolean isSuccessful(int status){
        return status / 100 == 2;
    }

    private String readErrorMessage(String body) {
        Map<?,?> error = gson.fromJson(body, Map.class);
        if(error != null && error.get("message") != null){
            return error.get("message").toString();
        }
        return "Error";
    }
}
