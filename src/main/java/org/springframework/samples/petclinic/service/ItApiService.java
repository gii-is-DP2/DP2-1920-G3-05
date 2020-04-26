package org.springframework.samples.petclinic.service;

import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.ItApiSearch;
import org.springframework.samples.petclinic.model.ItBookDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ItApiService {

    private final static String ENDPOINT = "https://api.itbook.store/1.0/";

    public ItApiSearch searchItBooks(final String serachParam) {
        String searchUri = ENDPOINT + "search/" + serachParam.replaceAll("\\s+","");
        HttpHeaders headers = new HttpHeaders();
        //We need to "fake" user agent so thtat api accepts the call, otherwise we get 403
        headers.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ItApiSearch> search = restTemplate.exchange(searchUri, HttpMethod.GET, entity, ItApiSearch.class);
        return search.getBody();
    }

    public ItBookDetails getDetailsItBook(final String isbn) {
        String searchUri = ENDPOINT + "books/" + isbn;
        HttpHeaders headers = new HttpHeaders();
        //We need to "fake" user agent so thtat api accepts the call, otherwise we get 403
        headers.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ItBookDetails> search = restTemplate.exchange(searchUri, HttpMethod.GET, entity, ItBookDetails.class);
        return search.getBody();
    }

}