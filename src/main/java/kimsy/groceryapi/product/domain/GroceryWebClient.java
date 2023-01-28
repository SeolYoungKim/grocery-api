package kimsy.groceryapi.product.domain;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public abstract class GroceryWebClient {
    protected final WebClient webClient;
    protected AccessToken accessToken;

    public GroceryWebClient(final String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
        this.accessToken = getToken();
    }

    public GroceryWebClient(final String url, final AccessToken accessToken) {
        this.webClient = WebClient.builder().baseUrl(url).build();
        this.accessToken = accessToken;
    }

    abstract AccessToken getToken();

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

    abstract String[] requestForProducts();

    private <T> void validateNull(T t) {
        if (t == null) {
            throw new IllegalStateException("정보를 가져오는 데 실패했습니다.");
        }
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

    abstract Product requestForProduct(String productName);
}
