package kubeio;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

// Handler value: example.HandlerInteger
public class HTTPHandler implements RequestHandler<RequestRecord, ResponseRecord> {

  @Override
  /*
   * Takes in an RequestRecord, which contains a URL.
   * Logs the URL, then returns a message.
   */
  public ResponseRecord handleRequest(RequestRecord event, Context context) {
    LambdaLogger logger = context.getLogger();
    logger.log("URL found: " + event.url());

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(event.url()))
        .GET()
        .timeout(Duration.ofMillis(1))
        .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      logger.log("Response found: " + response.statusCode());
    } catch(Exception e) {
      logger.log(String.format("Exception %s", e));
    }

    return new ResponseRecord("Done");
  }
}

record RequestRecord(String url) {
}

record ResponseRecord(String message) {
}
