package kimsy.groceryapi.product.domain;

import java.util.Map;
import kimsy.groceryapi.product.domain.web_client.GroceryWebClient;
import org.springframework.util.StringUtils;

public class GroceryWebClientMapper {
    private final Map<String, GroceryWebClient> webClients;

    public GroceryWebClientMapper(final Map<String, GroceryWebClient> webClients) {
        this.webClients = webClients;
    }

    public Products getProducts(final String productType) {
        return findHandlerBy(productType)
                .getProducts();
    }

    private GroceryWebClient findHandlerBy(final String productType) {
        return webClients.keySet().stream()
                .filter(key -> key.equals(productType))
                .map(webClients::get)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("서비스를 지원하지 않는 품목입니다."));
    }

    public Product getProduct(final String productType, final String productName) {
        validate(productName);
        return findHandlerBy(productType)
                .getProduct(productName);
    }

    private void validate(final String productName) {
        if (!StringUtils.hasText(productName)) {
            throw new IllegalArgumentException("품목명을 입력해 주세요.");
        }
    }
}
