package kimsy.groceryapi.product.application.dto;

import java.util.List;
import kimsy.groceryapi.product.domain.Products;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductsResponse {
    private List<String> values;

    public ProductsResponse(final Products products) {
        this.values = products.values();
    }

    public List<String> values() {
        return values;
    }
}
