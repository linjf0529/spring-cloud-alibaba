package com.linjf.es;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static com.linjf.es.constants.HotelIndexConstants.MAPPING_TEMPLATE;

@Slf4j
@SpringBootTest
class HotelIndexTest {

    private RestHighLevelClient client;

    /**
     * 创建索引库
     * 等价于:PUT /hotel
     * MAPPING_TEMPLATE
     * @throws IOException
     */
    @Test
    void testCreateIndex() throws IOException {
        // 1.准备Request
        CreateIndexRequest request = new CreateIndexRequest("hotel");
        // 2.准备请求参数
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        // 3.发送请求
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * 判断索引库是否存在
     * 等价于:GET /hotel
     * @throws IOException
     */
    @Test
    void testExistsIndex() throws IOException {
        // 1.准备Request
        GetIndexRequest request = new GetIndexRequest("hotel");
        // 3.发送请求
        boolean isExists = client.indices().exists(request, RequestOptions.DEFAULT);
        log.info(isExists ? "存在" : "不存在");
        if(isExists){
            GetIndexResponse response=client.indices().get(request,RequestOptions.DEFAULT);
            String source=response.getMappings().get("hotel").source().toString();
            log.info("mappings.properties==>>{}",source);
        }
    }

    /**
     * 删除索引库
     * 等价于:DELETE /hotel
     * @throws IOException
     */
    @Test
    void testDeleteIndex() throws IOException {
        // 1.准备Request
        DeleteIndexRequest request = new DeleteIndexRequest("hotel");
        // 3.发送请求
        client.indices().delete(request, RequestOptions.DEFAULT);
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
