package com.github.hackerwin7.libjava.test.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class SearchTests {

    private static final String URL = "http://localhost:9092";
    private static final String ADDR = "localhost";
    private static final int PORT = 9200;
    private static final RestHighLevelClient ES = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost(ADDR, PORT, "http")
            )
    );

    public static void searchBidPrice() throws Exception {
        SearchRequest req = new SearchRequest("adx_req_bid_fixed_hourly-2019.12.19");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        SumAggregationBuilder sumAgg = AggregationBuilders
                .sum("bid-sum")
                .field("bid_price");
        sourceBuilder.aggregation(sumAgg);
        req.source(sourceBuilder);
        SearchResponse resp = ES.search(req, RequestOptions.DEFAULT);
        Aggregations aggs = resp.getAggregations();
        Sum sum = aggs.get("bid-sum");
        System.out.println((long) sum.getValue());
    }

    public static void main(String[] args) throws Exception {
        searchBidPrice();
    }
}
