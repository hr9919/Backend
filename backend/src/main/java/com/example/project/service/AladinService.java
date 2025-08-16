package com.example.project.service;

import com.example.project.dto.BookDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class AladinService {

    private final RestTemplate restTemplate;

    @Value("${aladin.api.key}")
    private String apiKey;

    public AladinService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BookDto> searchBooks(String query) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl("https://www.aladin.co.kr/ttb/api/ItemSearch.aspx")
                    .queryParam("ttbkey", apiKey)
                    .queryParam("Query", URLEncoder.encode(query, StandardCharsets.UTF_8))
                    .queryParam("QueryType", "Title")
                    .queryParam("MaxResults", 10)
                    .queryParam("output", "js")
                    .build()
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode items = root.path("item");

            List<BookDto> books = new ArrayList<>();
            if (items.isArray()) {
                for (JsonNode item : items) {
                    books.add(new BookDto(
                            item.path("title").asText(""),
                            item.path("author").asText(""),
                            item.path("publisher").asText(""),
                            item.path("pubDate").asText(""),
                            item.path("isbn").asText(""),
                            item.path("isbn13").asText(""),
                            item.path("cover").asText(""),
                            item.path("link").asText(""),
                            item.path("categoryName").asText(""),
                            item.path("itemPage").asInt(0)
                    ));
                }
            }
            return books;

        } catch (Exception e) {
            throw new RuntimeException("알라딘 API 호출/파싱 중 오류 발생", e);
        }
    }
}
