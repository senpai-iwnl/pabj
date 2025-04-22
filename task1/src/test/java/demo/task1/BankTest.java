package demo.task1;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

    @Test
    public void testCreateAccount() {
        Bank bank = new BankImpl();
        Long id = bank.createAccount("Kacper Pietrzykowski", "123 Main St");
        assertNotNull(id);
    }

    @Test
    public void testDeposit() {
        Bank bank = new BankImpl();
        Long id = bank.createAccount("Kacper Pietrzykowski", "123 Main St");
        bank.deposit(id, BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), bank.getBalance(id));
    }

    @Test
    public void testWithdraw() {
        Bank bank = new BankImpl();
        Long id = bank.createAccount("Kacper Pietrzykowski", "123 Main St");
        bank.deposit(id, BigDecimal.valueOf(100));
        bank.withdraw(id, BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), bank.getBalance(id));
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        Bank bank = new BankImpl();
        Long id = bank.createAccount("Kacper Pietrzykowski", "123 Main St");
        bank.deposit(id, BigDecimal.valueOf(50));
        assertThrows(Bank.InsufficientFundsException.class, () -> bank.withdraw(id, BigDecimal.valueOf(100)));
    }

    @Test
    public void testTransfer() {
        Bank bank = new BankImpl();
        Long id1 = bank.createAccount("Kacper Pietrzykowski", "123 Main St");
        Long id2 = bank.createAccount("Kacper Pietrzykowski", "456 Elm St");
        bank.deposit(id1, BigDecimal.valueOf(200));
        bank.transfer(id1, id2, BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), bank.getBalance(id1));
        assertEquals(BigDecimal.valueOf(100), bank.getBalance(id2));
    }

    @Test
    public void testInvalidAccount() {
        Bank bank = new BankImpl();
        assertThrows(Bank.AccountIdException.class, () -> bank.deposit(999L, BigDecimal.valueOf(100)));
    }
}
