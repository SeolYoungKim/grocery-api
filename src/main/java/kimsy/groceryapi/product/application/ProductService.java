package kimsy.groceryapi.product.application;

import kimsy.groceryapi.product.application.dto.ProductPriceResponse;
import kimsy.groceryapi.product.application.dto.ProductsResponse;
import kimsy.groceryapi.product.domain.Product;
import kimsy.groceryapi.product.domain.ProductWebClient;
import kimsy.groceryapi.product.domain.Products;
import kimsy.groceryapi.product.presentation.dto.ProductPriceRequest;
import kimsy.groceryapi.product.presentation.dto.ProductsSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductWebClient productWebClient;

    public ProductsResponse getProducts(final ProductsSearchRequest productsSearchRequest) {
        final Products products = productWebClient.getProducts(productsSearchRequest.productType());
        return new ProductsResponse(products);
    }

    public ProductPriceResponse getProduct(final ProductPriceRequest productPriceRequest) {
        final Product product = productWebClient.getProduct(productPriceRequest.productType(),
                productPriceRequest.productName());
        return new ProductPriceResponse(product);
    }
}
