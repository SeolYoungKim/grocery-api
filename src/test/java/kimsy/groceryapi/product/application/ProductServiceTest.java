package kimsy.groceryapi.product.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import kimsy.groceryapi.product.application.dto.ProductsResponse;
import kimsy.groceryapi.product.domain.FruitWebClient;
import kimsy.groceryapi.product.domain.ProductWebClient;
import kimsy.groceryapi.product.domain.VegetableWebClient;
import kimsy.groceryapi.product.presentation.dto.ProductsSearchRequest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private static MockWebServer mockWebServer;
    private static final String TOKEN = "{ \"accessToken\" : \"token\" }";
    private static final String[] EXPECT = {"배", "토마토", "사과", "바나나"};


    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    private final ObjectMapper objectMapper = new ObjectMapper();
    private ProductService productService;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        final String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());

        ProductWebClient productWebClient = new ProductWebClient(new FruitWebClient(baseUrl),
                new VegetableWebClient(baseUrl));

        productService = new ProductService(productWebClient);

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(TOKEN))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));


        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(EXPECT))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }

    @DisplayName("품목에 대한 전체 상품 목록이 들어있는 ProductsResponse를 반환한다.")
    @Test
    void getProducts() {
        final ProductsSearchRequest fruitRequest = new ProductsSearchRequest("fruit");
        final ProductsResponse result = productService.getProducts(fruitRequest);

        final List<String> actual = result.values();
        assertThat(actual).hasSize(EXPECT.length);
        assertThat(actual).contains(EXPECT);
    }
}