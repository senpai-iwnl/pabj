package demo.task1;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class BankImpl implements Bank {
    private final Map<Long, Account> accounts = new HashMap<>();
    private final Map<String, Long> accountOwners = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public synchronized Long createAccount(String name, String address) {
        String key = name + "|" + address;
        return accountOwners.computeIfAbsent(key, k -> {
            Long id = idGenerator.getAndIncrement();
            accounts.put(id, new Account(id));
            return id;
        });
    }

    @Override
    public synchronized Long findAccount(String name, String address) {
        return accountOwners.get(name + "|" + address);
    }

    @Override
    public synchronized void deposit(Long id, BigDecimal amount) {
        Account account = accounts.get(id);
        if (account == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AccountIdException();
        }
        account.balance = account.balance.add(amount);
    }

    @Override
    public synchronized BigDecimal getBalance(Long id) {
        Account account = accounts.get(id);
        if (account == null) {
            throw new AccountIdException();
        }
        return account.balance;
    }

    @Override
    public synchronized void withdraw(Long id, BigDecimal amount) {
        Account account = accounts.get(id);
        if (account == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AccountIdException();
        }
        if (account.balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        account.balance = account.balance.subtract(amount);
    }

    @Override
    public synchronized void transfer(Long idSource, Long idDestination, BigDecimal amount) {
        if (idSource.equals(idDestination) || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AccountIdException();
        }
        withdraw(idSource, amount);
        deposit(idDestination, amount);
    }

    private static class Account {
        final Long id;
        BigDecimal balance = BigDecimal.ZERO;

        Account(Long id) {
            this.id = id;
        }
    }
}
