package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.model.Poem;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PoemService {

	@Autowired
	private PoemService poemService;
	
    @Cacheable("poemList")
    public List<Poem> getPoemsList() {
        List<Poem> poems = new ArrayList<>();
        final String uri = "https://www.poemist.com/api/v1/randompoems";
        RestTemplate restTemplate = new RestTemplate();
        while(poems.size()<10){
            Poem[] poem = restTemplate.getForObject(uri, Poem[].class);
            List<Poem> aux = Arrays.asList(poem);
            poems.addAll(aux);
        }
        return poems;
    }

    public Poem getRandomPoem() {
        List<Poem> poems = poemService.getPoemsList();
		int random = ThreadLocalRandom.current().nextInt(0,10);
		Poem poem = poems.get(random);
		return poem;
    }
    
} 