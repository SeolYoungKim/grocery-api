package kimsy.groceryapi.product.application.dto;

import kimsy.groceryapi.product.domain.Product;

public class ProductPriceResponse {
    private final String productName;
    private final Integer price;

    public ProductPriceResponse(final Product product) {
        this.productName = product.name();
        this.price = product.price();
    }

    public String productName() {
        return productName;
    }

    public Integer price() {
        return price;
    }
}
