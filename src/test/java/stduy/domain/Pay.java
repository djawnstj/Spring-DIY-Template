package stduy.domain;

public class Pay {
    private final long id;
    private long balance;

    Pay(long id, long balance) {
        this.id = id;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void charge(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("i must be positive");
        }
        this.balance += amount;
    }

    public void pay(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("i must be positive");
        }
        if (this.balance < amount) {
            throw new IllegalArgumentException("balance is not enough");
        }
        this.balance -= amount;

    }
}
