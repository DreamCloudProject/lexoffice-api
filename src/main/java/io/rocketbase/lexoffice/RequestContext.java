package io.rocketbase.lexoffice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class RequestContext {

    private static long lastCall;
    private final LexofficeApiBuilder apiBuilder;
    private HttpClient httpClient;
    private ClientHttpRequestFactory requestFactory;
    private RestTemplate restTemplate;
    private long throttlePeriod = 510;

    RequestContext(LexofficeApiBuilder apiBuilder) {
        this.apiBuilder = apiBuilder;

        this.httpClient = HttpClientBuilder.create()
                .build();
        this.requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate = new RestTemplate(requestFactory);
        restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter(getObjectMapper())));
    }


    protected ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }


    public RestUriBuilder getUriBuilder() {
        return new RestUriBuilder()
                .protocol("https")
                .host(apiBuilder.getHost());
    }

    public synchronized <E> E execute(RestUriBuilder uriBuilder, HttpMethod method, ParameterizedTypeReference<E> entityClass) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + apiBuilder.getApiToken());

        checkThrottlePeriod();

        HttpEntity<E> httpEntity = new HttpEntity<E>(headers);
        ResponseEntity<E> response = restTemplate.exchange(uriBuilder.build(), method, httpEntity, entityClass);
        lastCall = System.currentTimeMillis();
        if (apiBuilder.throttleProviderPresent()) {
            apiBuilder.getThrottleProvider()
                    .apiCalled();
        }


        return response.getBody();
    }

    protected synchronized void checkThrottlePeriod() {
        if (apiBuilder.throttleProviderPresent()) {
            waitFuturePassed(apiBuilder.getThrottleProvider()
                    .getNextCallAllowed());
        } else {
            waitFuturePassed(lastCall + throttlePeriod);
        }
    }

    protected synchronized void waitFuturePassed(long future) {
        while (future > System.currentTimeMillis()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // ignore, except to propagate
                Thread.currentThread()
                        .interrupt();
            }
        }
    }

}
