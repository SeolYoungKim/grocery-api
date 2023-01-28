package kimsy.groceryapi.product.domain;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
public class FruitWebClient {
    private final WebClient fruitWebClient;
    private AccessToken accessToken;

    @Autowired
    public FruitWebClient(@Value("${api.url.fruit}") String fruitUrl) {
        fruitWebClient = WebClient.builder().baseUrl(fruitUrl).build();
        accessToken = getToken();
    }

    public FruitWebClient(final String fruitUrl, final AccessToken accessToken) {
        this.fruitWebClient = WebClient.builder().baseUrl(fruitUrl).build();
        this.accessToken = accessToken;
    }

    public Products getProducts() {
        String[] result;
        try {
            result = requestForProducts();
        } catch (WebClientResponseException e) {
            log.info("토큰이 만료되어 재발급 요청합니다. {}", e.getMessage());

            accessToken = getToken();
            result = requestForProducts();
        }

        validateNull(result);
        return new Products(Arrays.stream(result).toList());
    }

    private String[] requestForProducts() {
        return fruitWebClient.get()
                .uri(ProductType.FRUIT.productUri())
                .header(HttpHeaders.AUTHORIZATION, accessToken.accessToken())
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }

    private AccessToken getToken() {
        return fruitWebClient.get()
                .uri(ProductType.FRUIT.tokenUri())
                .retrieve()
                .bodyToMono(AccessToken.class)
                .block();
    }

    public Product getPrice(final String productName) {
        Product result;
        try {
            result = requestForProduct(productName);
        } catch (WebClientResponseException e) {
            log.info("토큰이 만료되어 재발급 요청합니다. {}", e.getMessage());

            accessToken = getToken();
            result = requestForProduct(productName);
        }

        validateNull(result);
        return result;
    }

    private Product requestForProduct(final String productName) {
        return fruitWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ProductType.FRUIT.productUri())
                        .queryParam("name", productName)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, accessToken.accessToken())
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }

    private <T> void validateNull(T t) {
        if (t == null) {
            throw new IllegalStateException("정보를 가져오는 데 실패했습니다.");
        }
    }
}
