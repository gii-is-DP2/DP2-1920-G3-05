package org.springframework.samples.petclinic.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.ItApiSearch;
import org.springframework.samples.petclinic.model.ItBookDetails;
import org.springframework.samples.petclinic.service.exceptions.BadIsbnException;
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

    public ItBookDetails getDetailsItBook(final String isbn) throws BadIsbnException{
        if(!validateISBN(isbn)){
            throw new BadIsbnException();
        }
        String searchUri = ENDPOINT + "books/" + isbn;
        HttpHeaders headers = new HttpHeaders();
        //We need to "fake" user agent so thtat api accepts the call, otherwise we get 403
        headers.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ItBookDetails> search = restTemplate.exchange(searchUri, HttpMethod.GET, entity, ItBookDetails.class);
        return search.getBody();
    }

    private boolean validateISBN(String isbn) {
		if (isbn == null) {
			return false;
		}
		//remove any hyphens
		isbn = isbn.replaceAll("-", "");

		//must be a 13 digit ISBN
		if (isbn.length() != 13) {
			return false;
		}
		try {
			int tot = 0;
			for (int i = 0; i < 12; i++) {
				int digit = Integer.parseInt(isbn.substring(i, i + 1));
				tot += i % 2 == 0 ? digit * 1 : digit * 3;
			}
			//checksum must be 0-9. If calculated as 10 then = 0
			int checksum = 10 - tot % 10;
			if (checksum == 10) {
				checksum = 0;
			}
			return checksum == Integer.parseInt(isbn.substring(12));
		} catch (NumberFormatException nfe) {
			//to catch invalid ISBNs that have non-numeric characters in them
			return false;
		}
	}

}