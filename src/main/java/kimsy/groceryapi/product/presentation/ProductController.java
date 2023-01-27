package kimsy.groceryapi.product.presentation;

import kimsy.groceryapi.product.application.ProductService;
import kimsy.groceryapi.product.application.dto.ProductsResponse;
import kimsy.groceryapi.product.presentation.dto.ProductsSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/product")
@RequiredArgsConstructor
@Controller
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public String getFruits(ProductsSearchRequest productsSearchRequest, Model model) {
        final ProductsResponse products = productService.getProducts(productsSearchRequest);
        model.addAttribute("products", products);
        return "products";
    }
}
