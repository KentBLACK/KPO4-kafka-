package ru.valinkin.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valinkin.exceptions.ConvertToDTOException;
import ru.valinkin.models.Account;
import ru.valinkin.models.InboxMessage;
import ru.valinkin.models.OutboxMessage;
import ru.valinkin.repositories.InboxMessageRepository;

import java.util.List;

@Service
public class InboxMessageService {
    private final InboxMessageRepository inboxMessageRepository;
    private final AccountService accountService;
    private final OutboxMessageService outboxMessageService;

    @Autowired
    public InboxMessageService(InboxMessageRepository inboxMessageRepository, AccountService accountService, OutboxMessageService outboxMessageService) {
        this.inboxMessageRepository = inboxMessageRepository;
        this.accountService = accountService;
        this.outboxMessageService = outboxMessageService;
    }

    @Transactional
    public void save(InboxMessage inboxMessage){
        inboxMessageRepository.save(inboxMessage);
    }
    @Scheduled(
            fixedDelay = 1000,
            initialDelay = 5000
    )
    public void checkInboxMessages(){
        List<InboxMessage> messages = inboxMessageRepository.findByProcessed(false);
            for (InboxMessage message : messages) {
            Account account = accountService.getAccountByUserId(message.getPayload());
            if (account == null) {
                accountService.save(new Account(message.getPayload(), 5000));
                System.out.println("Аккаунт " + message.getPayload() + " создан");
                OutboxMessage outboxMessage = new OutboxMessage("SUCCESSFUL", message.getPayload());
                outboxMessageService.save(outboxMessage);
            } else {
                if (account.getBalance() >= 500) {
                    account.setBalance(account.getBalance() - 500);
                    OutboxMessage outboxMessage = new OutboxMessage("SUCCESSFUL", message.getPayload());
                    outboxMessageService.save(outboxMessage);
                } else {
                    OutboxMessage outboxMessage = new OutboxMessage("UNSUCCESSFUL", message.getPayload());
                    outboxMessageService.save(outboxMessage);
                }
            }
            message.markAsProcessed();
            inboxMessageRepository.save(message);
        }
    }
}
