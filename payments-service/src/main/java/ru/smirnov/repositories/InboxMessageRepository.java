package ru.smirnov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.models.InboxMessage;

import java.util.List;

@Repository
public interface InboxMessageRepository extends JpaRepository<InboxMessage, String> {
    List<InboxMessage> findByProcessed(boolean processed);
}
