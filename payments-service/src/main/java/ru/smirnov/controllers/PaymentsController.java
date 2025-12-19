package ru.smirnov.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.models.Account;
import ru.smirnov.services.AccountService;

@RestController
@RequestMapping("/api/accounts/")
@Tag(name = "Заказы", description = "Сервис заказов")
public class PaymentsController {
    private final AccountService accountService;

    @Autowired
    public PaymentsController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/balance/")
    @Operation(summary = "Получить баланс пользователя")
    public BalanceDTO getAccounts(@RequestParam("userId") String userId) {
        System.out.println("Получен запрос на получение баланса");
        System.out.println("Найден счет: " + accountService.getAccountByUserIdForController(userId).getUserId());
        return new BalanceDTO(accountService.getAccountByUserIdForController(userId).getBalance());
    }

    @PostMapping()
    @Operation(summary = "Создать пользователя")
    public void createAccount(@RequestBody UserDTO userDTO) {
        accountService.save(new Account(userDTO.getUserId(), 5000));
    }

    @PostMapping("/upbalance")
    @Operation(summary = "Пополнить баланс")
    public void upBalance(@RequestBody BalanceUpDTO balanceUpDTO) {
        Account account = accountService.getAccountByUserIdForController(balanceUpDTO.getUserId());
        account.setBalance(account.getBalance() + balanceUpDTO.getBalance());
        accountService.save(account);
    }

    public static class BalanceDTO {
        private int balance;

        public BalanceDTO() {}

        public BalanceDTO(int balance) {
            this.balance = balance;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }
    }

    public static class BalanceUpDTO {
        private int balance;

        private String userId;

        public BalanceUpDTO(int balance, String userId) {
            this.balance = balance;
            this.userId = userId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }
    }

    public static class UserDTO {
        private String userId;

        public UserDTO() {}

        public UserDTO(String userId) {
            this.userId = userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserId() {
            return userId;
        }
    }
}
