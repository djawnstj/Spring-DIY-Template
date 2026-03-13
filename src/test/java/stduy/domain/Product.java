package stduy.domain;

public class Product {
    private final long id;
    private final long price;
    private final String description;

    public long getId() {
        return id;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Product(long id, long price, String description) {
        this.id = id;
        this.price = price;
        this.description = description;
    }
}
