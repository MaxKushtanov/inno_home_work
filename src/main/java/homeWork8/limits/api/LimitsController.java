package homeWork8.limits.api;

import homeWork8.limits.domain.Limit;
import homeWork8.limits.service.LimitService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/limits")
@Validated
public class LimitsController {

    private final LimitService service;

    public LimitsController(LimitService service) {
        this.service = service;
    }

    /**
     * Остаток лимита на сегодня по клиенту
     */
    @GetMapping("/{clientId}/today")
    public Limit today(@PathVariable @NotNull @Min(1) Long clientId) {
        return service.ensure(clientId, LocalDate.now());
    }

    /**
     * Принудительно подготовить лимиты на сегодня всем известным клиентам
     */
    @PostMapping("/reset-now")
    public void resetNow() {
        service.prepareTodayForAllKnownClients();
    }
}