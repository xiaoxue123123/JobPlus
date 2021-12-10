package com.laioffer.job.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.job.entity.Item;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
/**
 * search job based on keyword, lat, lon
 *
 *
 * **/
public class GitHubClient {
    private static final String URL_TEMPLATE = "https://jobs.github.com/positions.json?description=%s&lat=%s&long=%s";
    private static final String DEFAULT_KEYWORD = "developer";

    public List<Item> search(double lat, double lon, String keyword) {
        if (keyword == null) {
            keyword = DEFAULT_KEYWORD;
        }


        // “hello world” => “hello%20world” or "hello+world"
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 把 %s replace 成 keyword
        String url = String.format(URL_TEMPLATE, keyword, lat, lon);

        // closeable:
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // Create a custom response handler
        // lambda
        ResponseHandler<List<Item>> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() != 200) {
                return Collections.emptyList();
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return Collections.emptyList();

            }
            ObjectMapper mapper = new ObjectMapper();
            //return Arrays.asList(mapper.readValue(entity.getContent(), Item[].class));
            List<Item> items = Arrays.asList(mapper.readValue(entity.getContent(), Item[].class));
            // scan all items and its job description, extract keywords from job description
            extractKeywords(items);
            return items;

        };

        try {
            return httpclient.execute(new HttpGet(url), responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();

    }



    private static void extractKeywords(List<Item> items) {
        MonkeyLearnClient monkeyLearnClient = new MonkeyLearnClient();
        List<String> descriptions = new ArrayList<>();
        // 修复一个bug, "·" 不能被显示
        for (Item item : items) {
            String description = item.getDescription();
            description = description.replace("·"," ");
            descriptions.add(description);
        }

        List<Set<String>> keywordList = monkeyLearnClient.extract(descriptions);
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setKeywords(keywordList.get(i));
        }
    }

}
