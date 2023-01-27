package kimsy.groceryapi.product.domain;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class VegetableWebClient {
    private final WebClient vegetableWebClient;

    public VegetableWebClient(@Value("${api.url.vegetable}") String vegetableUrl) {
        vegetableWebClient = WebClient.builder().baseUrl(vegetableUrl).build();
    }

    public List<String> getProductNames() {
        final AccessToken accessToken = getToken();
        final String[] result = vegetableWebClient.get()
                .uri(ProductType.VEGETABLE.productUri())
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
}
