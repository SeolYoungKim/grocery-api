package kimsy.groceryapi.product.domain;

import java.util.Arrays;
import java.util.List;
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

    public List<String> getProductNames() {
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

        return Arrays.stream(result).toList();
    }

    private AccessToken getToken() {
        return fruitWebClient.get()
                .uri(ProductType.FRUIT.tokenUri())
                .retrieve()
                .bodyToMono(AccessToken.class)
                .block();
    }
}