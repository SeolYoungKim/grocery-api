package kimsy.groceryapi.product.application;

import kimsy.groceryapi.product.application.dto.ProductPriceResponse;
import kimsy.groceryapi.product.application.dto.ProductsResponse;
import kimsy.groceryapi.product.domain.Product;
import kimsy.groceryapi.product.domain.Products;
import kimsy.groceryapi.product.domain.GroceryWebClientMapper;
import kimsy.groceryapi.product.presentation.dto.ProductPriceRequest;
import kimsy.groceryapi.product.presentation.dto.ProductsSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final GroceryWebClientMapper groceryWebClientMapper;

    public ProductsResponse getProducts(final ProductsSearchRequest requestDto) {
        final Products products = groceryWebClientMapper.getProducts(requestDto.productType());
        return new ProductsResponse(products);
    }

    public ProductPriceResponse getProduct(final ProductPriceRequest requestDto) {
        final Product product = groceryWebClientMapper.getProduct(requestDto.productType(),
                requestDto.productName());
        return new ProductPriceResponse(product);
    }
}
