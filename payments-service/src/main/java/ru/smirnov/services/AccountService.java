package ru.smirnov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.models.Account;
import ru.smirnov.repositories.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountByUserId(String userid) {
        return accountRepository.findByUserId(userid).orElse(null);
    }

    public Account getAccountByUserIdForController(String userid) {
        System.out.println("Получен запрос на получение счета по userId: " + userid);
        System.out.println("Найден счет: " + accountRepository.findByUserId(userid).orElse(new Account("no", 0)).getUserId());
        Account account = accountRepository.findByUserId(userid).orElse(null);
        if (account == null) { throw new RuntimeException("Аккаунт не найден"); }
        return account;
    }
    @Transactional
    public void save(Account account) {
        accountRepository.save(account);
    }
}
