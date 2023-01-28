package kimsy.groceryapi.product.domain;

public class Product {
    private String name;
    private Integer price;

    public String name() {
        return name;
    }

    void setName(final String name) {
        this.name = name;
    }

    public Integer price() {
        return price;
    }

    void setPrice(final Integer price) {
        this.price = price;
    }
}
