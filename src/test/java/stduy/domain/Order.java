package stduy.domain;

public class Order {
    private final long id;

    private final long userId;
    private final long productId;
    private final long payId;
    private final long orderPrice;

    public Order(long id, long userId, long productId, long payId, long orderPrice) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.payId = payId;
        this.orderPrice = orderPrice;
    }

    public long getUserId() {
        return userId;
    }

    public long getOrderPrice() {
        return orderPrice;
    }

    public long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public long getPayId() {
        return payId;
    }

}
