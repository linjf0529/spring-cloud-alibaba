package com.linjf.es;


import com.alibaba.fastjson.JSON;
import com.linjf.es.entity.HotelDoc;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

/**
 * Elasticsearch提供了基于JSON的DSL（Domain Specific Language）来定义查询。常见的查询类型包括：
 *  1.查询所有：查询出所有数据，一般测试用。例如：match_all
 *  2.全文检索（full text）查询：利用分词器对用户输入内容分词，然后去倒排索引库中匹配。例如 match_query multi_match_query
 *  3.精确查询：根据精确词条值查找数据，一般是查找keyword、数值、日期、boolean等类型字段。例如 ids range term
 *  4.地理（geo）查询：根据经纬度查询。例如 geo_distance geo_bounding_box
 *  5.复合（compound）查询：复合查询可以将上述各种查询条件组合起来，合并查询条件。例如bool function_score
 *基本语法:
 *  GET /indexName/_search
 *  {
 *      "query":{
 *          "查询类型":{
 *              "查询条件":"条件值"
 *          }
 *      }
 *  }
 */
@Slf4j
@SpringBootTest
class HotelSearchTest {

    private RestHighLevelClient client;

    /**
     * 查询全部(默认查询10条数据)
     * GET /hotel/_search
     * {
     *   "query": {
     *     "match_all": {
     *     }
     *   }
     * }
     * @throws IOException
     */
    @Test
    void testMatchAll() throws IOException {
        // 1.准备request
        SearchRequest request = new SearchRequest("hotel");
        // 2.准备请求参数
        request.source().query(QueryBuilders.matchAllQuery());
        // 3.发送请求，得到响应
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.结果解析
        handleResponse(response);
    }

    /**
     * match查询:全文检索查询的一种，会对用户输入内容分词，然后去倒排索引库检索
     * GET /hotel/_search
     * {
     *   "query": {
     *     "match": {
     *       "all": "如家"
     *     }
     *   }
     * }
     * multi_match查询:与match查询类似，只不过允许同时查询多个字段
     * GET /hotel/_search
     * {
     *   "query": {
     *     "multi_match": {
     *       "query": "外滩如家",
     *       "fields": ["name","brand","city"]
     *     }
     *   }
     * }
     * @throws IOException
     */
    @Test
    void testMatch() throws IOException {
        // 1.准备request
        SearchRequest request = new SearchRequest("hotel");
        // 2.准备请求参数
         request.source().query(QueryBuilders.matchQuery("all", "外滩如家"));//match查询
//        request.source().query(QueryBuilders.multiMatchQuery("外滩如家", "name", "brand", "city"));//multi_match查询
        // 3.发送请求，得到响应
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.结果解析
        handleResponse(response);
    }

    /**
     * bool复合查询
     *  1.must：必须匹配的条件，可以理解为“与”
     *  2.should：选择性匹配的条件，可以理解为“或”
     *  3.must_not：必须不匹配的条件，不参与打分
     *  4.filter：必须匹配的条件，不参与打分
     * @throws IOException
     */
    @Test
    void testBool() throws IOException {
        // 1.准备request
        SearchRequest request = new SearchRequest("hotel");
        // 2.准备请求参数
//         BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        boolQuery.must(QueryBuilders.termQuery("city", "杭州"));
//        boolQuery.filter(QueryBuilders.rangeQuery("price").lte(250));
        request.source().query(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("city", "杭州"))//term查询：根据词条精确匹配，一般搜索keyword类型、数值类型、布尔类型、日期类型字段
                        .filter(QueryBuilders.rangeQuery("price").lte(250))//range查询：根据数值范围查询，可以是数值、日期的范围
        );
        // 3.发送请求，得到响应
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.结果解析
        handleResponse(response);
    }

    /**
     * 排序:默认的排序是按相关度算分（_score）来排序,可以排序字段类型有：keyword类型、数值类型、地理坐标类型、日期类型等
     * 基本语法:
     * GET /hotel/_search
     * {
     *   "query": {
     *     "match_all": {
     *     }
     *   },
     *   "sort": [
     *     {
     *       "字段": {
     *         "order": "desc"
     *       }
     *     }
     *   ]
     * }
     * 根据距离排序
     * "sort": [
     *     {
     *       "_geo_distance": {
     *         "location":"31.156297, 121.419948",
     *         "order": "asc",
     *         "unit": "km"
     *       }
     *     }
     *   ]
     * 分页:from/size,与MySQL类似 from为起始位置,size为查询条数,逻辑分页,查询总条数不能超过十万条
     * @throws IOException
     */
    @Test
    void testSortAndPage() throws IOException {
        int page = 1,size = 10;

        // 1.准备request
        SearchRequest request = new SearchRequest("hotel");
        // 2.准备请求参数
        // 2.1.query
        request.source().query(QueryBuilders.matchAllQuery());
        // 2.2.排序sort
        //根据地理位置正序排序
        request.source().sort(SortBuilders
                .geoDistanceSort("location",new GeoPoint("31.156297, 121.419948"))  //比对字段,地理座标
                .order(SortOrder.ASC)      //正序
                .unit(DistanceUnit.METERS) //单位米
        );
        //根据价格正序排序
        request.source().sort("price", SortOrder.ASC);
        // 2.3.分页 from\size
        request.source().from((page - 1) * size).size(size);

        // 3.发送请求，得到响应
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.结果解析
        handleResponse(response);
    }

    /**
     * 高亮测试,打<em></em>标签
     * @throws IOException
     */
    @Test
    void testHighlight() throws IOException {
        // 1.准备request
        SearchRequest request = new SearchRequest("hotel");
        // 2.准备请求参数
        // 2.1.query
        request.source().query(QueryBuilders.matchQuery("all", "外滩如家"));
        // 2.2.高亮
        request.source().highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));
        // 3.发送请求，得到响应
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.结果解析
        handleResponse(response);
    }

    private void handleResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        // 4.1.总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("总条数：" + total);
        // 4.2.获取文档数组
        SearchHit[] hits = searchHits.getHits();
        // 4.3.遍历
        Integer i=0;
        for (SearchHit hit : hits) {
            // 4.4.获取source
            String json = hit.getSourceAsString();
            // 4.5.反序列化，非高亮的
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            // 4.6.处理高亮结果
            // 1)获取高亮map
            Map<String, HighlightField> map = hit.getHighlightFields();
            if(!map.isEmpty()){
                // 2）根据字段名，获取高亮结果
                HighlightField highlightField = map.get("name");
                // 3）获取高亮结果字符串数组中的第1个元素
                String hName = highlightField.getFragments()[0].toString();
                // 4）把高亮结果放到HotelDoc中
                hotelDoc.setName(hName);
            }
            // 4.7.打印
            log.info("数据{}::>{}",i+=1,hotelDoc);
        }
    }

    @BeforeEach
    void setUp() {
        client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://localhost:9200")
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        client.close();
    }

}
