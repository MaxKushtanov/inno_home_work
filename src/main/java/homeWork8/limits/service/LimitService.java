package homeWork8.limits.service;

import homeWork8.limits.api.LimitExceededException;
import homeWork8.limits.config.LimitsProperties;
import homeWork8.limits.domain.Limit;
import homeWork8.limits.repo.LimitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LimitService {

    private final LimitRepository repo;
    private final LimitsProperties props;

    public LimitService(LimitRepository repo, LimitsProperties props) {
        this.repo = repo;
        this.props = props;
    }

    /**
     * Найти или создать запись лимита на конкретный день.
     */
    @Transactional
    public Limit ensure(Long clientId, LocalDate day) {
        return repo.findByClientIdAndDay(clientId, day)
                .orElseGet(() -> {
                    Limit l = new Limit();
                    l.setClientId(clientId);
                    l.setDay(day);
                    l.setRemaining(props.getDefaultAmount());
                    return repo.save(l);
                });
    }

    /**
     * Зарезервировать сумму из дневного лимита клиента (сегодня).
     */
    @Transactional
    public Limit tryReserve(Long clientId, BigDecimal amount) {
        LocalDate today = LocalDate.now();
        Limit l = ensure(clientId, today);

        if (l.getRemaining().compareTo(amount) < 0) {
            throw new LimitExceededException("Daily limit exceeded for clientId=" + clientId);
        }
        l.setRemaining(l.getRemaining().subtract(amount));
        return repo.save(l); // @Version защитит от гонок
    }

    /**
     * Вернуть сумму в лимит (например, при неуспешном платеже).
     */
    @Transactional
    public Limit restore(Long clientId, BigDecimal amount) {
        LocalDate today = LocalDate.now();
        Limit l = ensure(clientId, today);
        l.setRemaining(l.getRemaining().add(amount));
        return repo.save(l);
    }

    /**
     * Подготовить лимиты на сегодня всем известным клиентам.
     */
    @Transactional
    public void prepareTodayForAllKnownClients() {
        List<Long> clients = repo.findDistinctClientIds();
        LocalDate today = LocalDate.now();
        for (Long clientId : clients) {
            Limit l = repo.findByClientIdAndDay(clientId, today).orElse(null);
            if (l == null) {
                ensure(clientId, today);
            } else {
                // если запись уже есть — просто обновим остаток до дефолтного (опционально)
                l.setRemaining(props.getDefaultAmount());
                repo.save(l);
            }
        }
    }
}