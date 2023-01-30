package kimsy.groceryapi.product.domain;

import java.util.Map;
import kimsy.groceryapi.product.domain.web_client.GroceryWebClient;
import org.springframework.util.StringUtils;

public class GroceryWebClientMapper {
    private final Map<ProductType, GroceryWebClient> webClients;

    public GroceryWebClientMapper(final Map<ProductType, GroceryWebClient> webClients) {
        this.webClients = webClients;
    }

    public Products getProducts(final String productType) {
        return findHandlerBy(productType)
                .getProducts();
    }

    public Product getProduct(final String productType, final String productName) {
        validate(productName);
        return findHandlerBy(productType)
                .getProduct(productName);
    }

    private GroceryWebClient findHandlerBy(final String productType) {
        return webClients.get(ProductType.of(productType));
    }

    private void validate(final String productName) {
        if (!StringUtils.hasText(productName)) {
            throw new IllegalArgumentException("품목명을 입력해 주세요.");
        }
    }
}
