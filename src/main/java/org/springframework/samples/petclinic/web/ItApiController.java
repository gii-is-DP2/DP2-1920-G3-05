package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.ItApiSearch;
import org.springframework.samples.petclinic.model.ItBook;
import org.springframework.samples.petclinic.model.ItBookDetails;
import org.springframework.samples.petclinic.service.ItApiService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItApiController {

    private ItApiService service;

    @Autowired
    public ItApiController(ItApiService itApiService) {
        this.service = itApiService;
    }

    @GetMapping("/itBooks")
    public String processFindForm(final Map<String,Object> model, ItBook itBook) {
        String searchParam = itBook.getTitle();
        ItApiSearch search = this.service.searchItBooks(searchParam);
        List<ItBook> itBooks = search.getItBooks();
        model.put("itBooks", itBooks);
        return "itBooks/list";
    }

    @GetMapping("/itBooks/find")
    public String searchForm(final Map<String, Object> model) {
        model.put("itBook", new ItBook());
        return "itBooks/find";
    }

    @GetMapping("/itBooks/details/{isbn}")
    public String getDetails(final Map<String, Object> model, @PathVariable final String isbn) {
        ItBookDetails itBookDetails = this.service.getDetailsItBook(isbn);
        model.put("itBook", itBookDetails);
        return "itBooks/details";
    }

}