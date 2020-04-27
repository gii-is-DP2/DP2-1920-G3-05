package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.Quote;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QuoteService {

    public Quote getRandomQuote() {
        final String uri = "https://api.quotable.io/random";
		RestTemplate restTemplate = new RestTemplate();
		Quote quote = restTemplate.getForObject(uri, Quote.class);
        return quote;
    }
    
} 