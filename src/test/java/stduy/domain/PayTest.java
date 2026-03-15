package stduy.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PayTest {

    @Test
    @DisplayName("페이 계좌에 잔액을 충전할 수 있다")
    public void charge() {
        Pay pay = new Pay(1, 0);
        pay.charge(100);
        assertEquals(100, pay.getBalance());
    }

    @Test
    @DisplayName("상품을 구매한다.")
    public void buy() {
        Product product = new Product(1L, 800L, "고구마");
        Pay pay = new Pay(2L, 1000L);

        pay.pay(product.getPrice());

        assertEquals(pay.getBalance(), 0);
    }

    @Test
    @DisplayName("가지고 있는 돈보다 비싼 물건은 살 수 없다")
    public void cannotBuy() {
        Product product = new Product(1L, 800L, "고구마");
        Pay pay = new Pay(2L, 200L);

        assertThrows(IllegalArgumentException.class, () -> pay.pay(product.getPrice()));
    }
}
