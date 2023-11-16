package my.demo.blockchain_demo.core.rpc;

import org.web3j.protocol.Service;
import org.web3j.protocol.exceptions.ClientConnectionException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

// based on org.web3j.protocol.http.HttpService, we use this implementation to avoid using okhttp3 library (dependency collision)
public class HttpService extends Service {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final String url;

    public HttpService(String url) {
        super(true);
        this.url = url;
    }

    @Override
    protected InputStream performIO(String request) throws IOException {
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<InputStream> response;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        } catch (InterruptedException e) {
            throw new IOException(e);
        }

        var responseBody = response.body();
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return new ByteArrayInputStream(responseBody.readAllBytes());
        } else {
            String text = responseBody == null ? "N/A" : new String(responseBody.readAllBytes(), StandardCharsets.UTF_8);
            throw new ClientConnectionException("Invalid response received: " + response.statusCode() + "; " + text);
        }
    }

    @Override
    public void close() throws IOException {
    }
}

