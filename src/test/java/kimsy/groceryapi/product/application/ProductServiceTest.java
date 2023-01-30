package kimsy.groceryapi.product.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import kimsy.groceryapi.product.MockWebServerTest;
import kimsy.groceryapi.product.application.dto.ProductPriceResponse;
import kimsy.groceryapi.product.application.dto.ProductsResponse;
import kimsy.groceryapi.product.domain.AccessToken;
import kimsy.groceryapi.product.domain.ProductType;
import kimsy.groceryapi.product.domain.web_client.FruitWebClient;
import kimsy.groceryapi.product.domain.GroceryWebClientMapper;
import kimsy.groceryapi.product.domain.web_client.VegetableWebClient;
import kimsy.groceryapi.product.presentation.dto.ProductPriceRequest;
import kimsy.groceryapi.product.presentation.dto.ProductsSearchRequest;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class ProductServiceTest extends MockWebServerTest {
    public static final String PRODUCT_TYPE = "fruit";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private ProductService productService;

    @BeforeEach
    void setUp() {
        final String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());

        final AccessToken token = new AccessToken("token");
        final FruitWebClient fruitWebClient = new FruitWebClient(baseUrl, token);
        final VegetableWebClient vegetableWebClient = new VegetableWebClient(baseUrl, token);

        GroceryWebClientMapper groceryWebClientMapper = new GroceryWebClientMapper(Map.of(
                        ProductType.FRUIT, fruitWebClient,
                        ProductType.VEGETABLE, vegetableWebClient));

        productService = new ProductService(groceryWebClientMapper);
    }

    @DisplayName("품목에 대한 전체 상품 목록이 들어있는 ProductsResponse를 반환한다.")
    @Test
    void getProducts() throws JsonProcessingException {
        final String[] expect = {"배", "토마토", "사과", "바나나"};
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(expect))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        final ProductsSearchRequest fruitRequest = new ProductsSearchRequest(PRODUCT_TYPE);
        final ProductsResponse result = productService.getProducts(fruitRequest);

        final List<String> actual = result.values();
        assertThat(actual).hasSize(expect.length);
        assertThat(actual).contains(expect);
    }

    @DisplayName("개별 품목에 대한 이름과 가격이 들어있는 ProductPriceResponse를 반환한다.")
    @Test
    void getProduct() throws JsonProcessingException {
        final String productName = "배";
        final int productPrice = 3000;
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(Map.of(
                        "name", productName,
                        "price", productPrice)))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        final ProductPriceRequest requestDto = new ProductPriceRequest(PRODUCT_TYPE, productName);
        final ProductPriceResponse actual = productService.getProduct(requestDto);
        assertThat(actual.productName()).isEqualTo(productName);
        assertThat(actual.price()).isEqualTo(productPrice);
    }
}