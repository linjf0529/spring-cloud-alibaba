package com.linjf.es;

import com.alibaba.fastjson.JSON;
import com.linjf.es.entity.Hotel;
import com.linjf.es.entity.HotelDoc;
import com.linjf.es.service.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@Slf4j
@SpringBootTest
class HotelDocumentTest {

    private RestHighLevelClient client;

    @Autowired
    private HotelService hotelService;

    /**
     * 添加数据
     * 等价于:PUT /hotel/_doc/{id}
     * 注意:id如果存在 es会删除旧数据在重新添加数据
     * @throws IOException
     */
    @Test
    void testAddDocument() throws IOException {
        // 1.查询数据库hotel数据
        Hotel hotel = hotelService.findById(61083L);
        // 2.转换为HotelDoc
        HotelDoc hotelDoc = new HotelDoc(hotel);
        // 3.转JSON
        String json = JSON.toJSONString(hotelDoc);

        // 1.准备Request
        IndexRequest request = new IndexRequest("hotel").id(hotelDoc.getId().toString());
        // 2.准备请求参数DSL，其实就是文档的JSON字符串
        request.source(json, XContentType.JSON);
        // 3.发送请求
        client.index(request, RequestOptions.DEFAULT);
    }

    /**
     * 根据id查询
     * 等价于:GET /hotel/_doc/{id}
     * @throws IOException
     */
    @Test
    void testGetDocumentById() throws IOException {
        // 1.准备Request
        GetRequest request = new GetRequest("hotel", "61083");
        // 2.发送请求
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        // 3.解析响应结果
        String json = response.getSourceAsString();

        HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
        log.info("hotelDoc==>>{}",hotelDoc);
    }

    /**
     * 根据id删除
     * 等价于:DELETE /hotel/_doc/{id}
     * @throws IOException
     */
    @Test
    void testDeleteDocumentById() throws IOException {
        // 1.准备Request      // DELETE /hotel/_doc/{id}
        DeleteRequest request = new DeleteRequest("hotel", "61083");
        // 2.发送请求
        client.delete(request, RequestOptions.DEFAULT);
    }

    /**
     * 部分字段更新
     * @throws IOException
     */
    @Test
    void testUpdateById() throws IOException {
        // 1.准备Request
        UpdateRequest request = new UpdateRequest("hotel", "61083");
        // 2.准备参数
        request.doc(
                "price", "870",
                "starName","四钻"
        );
        // 3.发送请求
        client.update(request, RequestOptions.DEFAULT);
    }

    /**
     * 批量添加数据
     * @throws IOException
     */
    @Test
    void testBulkRequest() throws IOException {
        // 查询所有的酒店数据
        List<Hotel> list = hotelService.findList();

        // 1.准备Request
        BulkRequest request = new BulkRequest();
        // 2.准备参数
        for (Hotel hotel : list) {
            // 2.1.转为HotelDoc
            HotelDoc hotelDoc = new HotelDoc(hotel);
            // 2.2.转json
            String json = JSON.toJSONString(hotelDoc);
            // 2.3.添加请求
            request.add(new IndexRequest("hotel").id(hotel.getId().toString()).source(json, XContentType.JSON));
        }

        // 3.发送请求
        client.bulk(request, RequestOptions.DEFAULT);
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
