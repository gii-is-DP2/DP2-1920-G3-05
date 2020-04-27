package org.springframework.samples.petclinic.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "error",
    "total",
    "page",
    "books"
})
public class ItApiSearch {

    @JsonProperty("error")
    private String error;
    @JsonProperty("total")
    private String total;
    @JsonProperty("page")
    private String page;
    @JsonProperty("books")
    private List<ItBook> itBooks = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("error")
    public String getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(String error) {
        this.error = error;
    }

    @JsonProperty("total")
    public String getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(String total) {
        this.total = total;
    }

    @JsonProperty("page")
    public String getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(String page) {
        this.page = page;
    }

    @JsonProperty("itBooks")
    public List<ItBook> getItBooks() {
        return itBooks;
    }

    @JsonProperty("itBooks")
    public void setBooks(List<ItBook> itBooks) {
        this.itBooks = itBooks;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}