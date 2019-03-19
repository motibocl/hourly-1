package com.elector.Utils;

import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class HttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    public static String get(String url, Map<String, Object> params) {
        AsyncHttpClient client = Dsl.asyncHttpClient();
        BoundRequestBuilder request = client.prepareGet(url);
        if(!params.isEmpty()) {
            request.setFormParams(mapParams(params));
        }
        Future<Response> responseFuture = request.execute();
        Response r = null;
        try {
            r =  responseFuture.get();
        }
        catch (Exception e) {
            LOGGER.error(String.format("Error while execute request, url: %s", url), e);
        }
        return processResponse(r);
    }

    public static String post(String url, Map<String, Object> params) {
        AsyncHttpClient client = Dsl.asyncHttpClient();
        BoundRequestBuilder request = client.preparePost(url);
        // Set parameters there (form params, headers if needed, body etc,)
        if(!params.isEmpty()) {
            request.setFormParams(mapParams(params));
        }
        Future<Response> responseFuture = request.execute();
        Response r = null;
        try {
            r =  responseFuture.get();
        }
        catch (Exception e) {
            LOGGER.error("Error while execute request", e);
        }
        return processResponse(r);
    }

    public static void getAsync(String url, Map<String, Object> params) {
        AsyncHttpClient client = Dsl.asyncHttpClient();
        BoundRequestBuilder request = client.prepareGet(url);
        if(!params.isEmpty()) {
            request.setFormParams(mapParams(params));
        }
        request.execute(new AsyncCompletionHandler<Object>() {
            @Override
            public Object onCompleted(Response response) {
                // Process response there
                processResponse(response);
                return response;
            }
        });
    }

    public static void postAsync(String url, Map<String, Object> params) {
        AsyncHttpClient client = Dsl.asyncHttpClient();
        BoundRequestBuilder request = client.preparePost(url);
        if(!params.isEmpty()) {
            request.setFormParams(mapParams(params));
        }
        request.execute(new AsyncCompletionHandler<Object>() {
            @Override
            public Object onCompleted(Response response) {
                // Process response there
                processResponse(response);
                return response;
            }
        });
    }

    private static String processResponse(Response r) {
        // Method for processing response.
        // You can get there any info from response: status, body, headers etc
        String response = null;
        try {
            LOGGER.info("Received response with status = {}", r.getStatusCode());
            response = r.getResponseBody();
        }
        catch (Exception e) {
            LOGGER.error("Error while execute request", e);
        }
        return response;
    }

    private static List<Param> mapParams(Map<String, Object> params) {
        List<Param> convertedParams = new ArrayList<>();
        for(Map.Entry<String, Object> paramEntry : params.entrySet()) {
            Param p = new Param(paramEntry.getKey(), paramEntry.getValue().toString());
            convertedParams.add(p);
        }
        return convertedParams;
    }

}
