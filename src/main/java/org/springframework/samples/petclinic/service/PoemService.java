package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.Poem;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PoemService {

    public Poem getRandomPoem() {
        final String uri = "https://www.poemist.com/api/v1/randompoems";
		RestTemplate restTemplate = new RestTemplate();
        Poem[] poem = restTemplate.getForObject(uri, Poem[].class);
        return poem[0];
    }
    
} 