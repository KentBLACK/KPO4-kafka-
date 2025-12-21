package ru.smirnov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.smirnov.DTO.*;
import ru.smirnov.service.ApiService;

import java.util.List;

@Controller
@RequestMapping("/")
public class FrontendController {

    private final ApiService apiService;

    @Autowired
    public FrontendController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    // Orders Service endpoints
    @GetMapping("/orders")
    public String ordersPage() {
        return "orders";
    }

    @GetMapping("/orders/list")
    public String getOrdersPage(@RequestParam("userId") String userId, Model model) {
        try {
            List<OrderDTO> orders = apiService.getOrdersByUserId(userId).block();
            model.addAttribute("orders", orders);
            model.addAttribute("userId", userId);
            return "orders-list";
        } catch (Exception e) {
            String errorMessage = "Ошибка при получении заказов: " + e.getMessage();
            if (e.getCause() != null) {
                errorMessage += " (Причина: " + e.getCause().getMessage() + ")";
            }
            e.printStackTrace(); // Логируем для отладки
            model.addAttribute("error", errorMessage);
            return "error";
        }
    }

    @GetMapping("/orders/status/result")
    public String getOrderStatus(@RequestParam("orderId") Long orderId, Model model) {
        try {
            StatusDTO status = apiService.getOrderStatus(orderId).block();
            model.addAttribute("status", status);
            model.addAttribute("orderId", orderId);
            return "order-status-result";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при получении статуса заказа: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/orders/create")
    public String createOrderPage(Model model) {
        model.addAttribute("createOrderDTO", new CreateOrderDTO());
        return "create-order";
    }

    @PostMapping("/orders/create")
    public String createOrder(@ModelAttribute CreateOrderDTO createOrderDTO, Model model) {
        try {
            apiService.createOrder(createOrderDTO).block();
            model.addAttribute("message", "Заказ успешно создан!");
            return "success";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при создании заказа: " + e.getMessage());
            return "error";
        }
    }

    // Payments Service endpoints
    @GetMapping("/accounts")
    public String accountsPage() {
        return "accounts";
    }

    @GetMapping("/accounts/balance/result")
    public String getBalance(@RequestParam("userId") String userId, Model model) {
        try {
            BalanceDTO balance = apiService.getBalance(userId).block();
            model.addAttribute("balance", balance);
            model.addAttribute("userId", userId);
            return "balance-result";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при получении баланса: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/accounts/create")
    public String createAccountPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "create-account";
    }

    @PostMapping("/accounts/create")
    public String createAccount(@ModelAttribute UserDTO userDTO, Model model) {
        try {
            apiService.createAccount(userDTO).block();
            model.addAttribute("message", "Пользователь успешно создан!");
            return "success";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при создании пользователя: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/accounts/upbalance")
    public String upBalancePage(Model model) {
        model.addAttribute("balanceUpDTO", new BalanceUpDTO());
        return "upbalance";
    }

    @PostMapping("/accounts/upbalance")
    public String upBalance(@ModelAttribute BalanceUpDTO balanceUpDTO, Model model) {
        try {
            apiService.upBalance(balanceUpDTO).block();
            model.addAttribute("message", "Баланс успешно пополнен!");
            return "success";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при пополнении баланса: " + e.getMessage());
            return "error";
        }
    }
}

