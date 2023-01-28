package kimsy.groceryapi.product.domain;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class VegetableWebClient {
    private final WebClient vegetableWebClient;
    private AccessToken accessToken;

    @Autowired
    public VegetableWebClient(@Value("${api.url.vegetable}") String vegetableUrl) {
        vegetableWebClient = WebClient.builder().baseUrl(vegetableUrl).build();
        accessToken = getToken();
    }

    public VegetableWebClient(final String vegetableUrl, final AccessToken accessToken) {
        vegetableWebClient = WebClient.builder().baseUrl(vegetableUrl).build();
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
        return vegetableWebClient.get()
                .uri(ProductType.VEGETABLE.productUri())
                .header(HttpHeaders.AUTHORIZATION, accessToken.accessToken())
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }

    private AccessToken getToken() {
        return vegetableWebClient.get()
                .uri(ProductType.VEGETABLE.tokenUri())
                .exchangeToMono(response -> {
                    final ResponseCookie responseCookie = response.cookies()
                            .getFirst(HttpHeaders.AUTHORIZATION);

                    if (responseCookie == null) {
                        throw new IllegalStateException("정보를 가져오는 데 실패했습니다.");
                    }

                    return Mono.fromSupplier(() -> new AccessToken(responseCookie.getValue()));
                }).block();
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
        return vegetableWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ProductType.VEGETABLE.productUri())
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
