package kimsy.groceryapi.product.domain;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FruitWebClient {
    private final WebClient fruitWebClient;

    public FruitWebClient(@Value("${api.url.fruit}") String fruitUrl) {
        fruitWebClient = WebClient.builder().baseUrl(fruitUrl).build();
    }

    public Products getProducts() {
        final AccessToken accessToken = getToken();
        final String[] result = fruitWebClient.get()
                .uri(ProductType.FRUIT.productUri())
                .header(HttpHeaders.AUTHORIZATION, accessToken.accessToken())
                .retrieve()
                .bodyToMono(String[].class)
                .block();

        if (result == null) {
            throw new IllegalStateException("정보를 가져오는 데 실패했습니다.");
        }

        return new Products(Arrays.stream(result).toList());
    }

    private AccessToken getToken() {
        return fruitWebClient.get()
                .uri(ProductType.FRUIT.tokenUri())
                .retrieve()
                .bodyToMono(AccessToken.class)
                .block();
    }

    public Product getPrice(final String productName) {
        final AccessToken accessToken = getToken();
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
}
