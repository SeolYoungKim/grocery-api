package kimsy.groceryapi.product.domain;

public class Product {
    private String name;
    private Integer price;

    public String name() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer price() {
        return price;
    }

    public void setPrice(final Integer price) {
        this.price = price;
    }
}
