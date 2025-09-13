package homeWork8.limits.service;

import homeWork6.products.api.dto.ProductDto;
import homeWork7.payments.api.dto.PaymentRequest;
import homeWork7.payments.client.ProductsClient;
import homeWork7.payments.domain.Payment;
import homeWork7.payments.domain.PaymentStatus;
import homeWork8.limits.repo.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final PaymentRepository repo;
    private final ProductsClient productsClient;
    private final LimitService limits;

    public PaymentService(PaymentRepository repo,
                          ProductsClient productsClient,
                          LimitService limits) {
        this.repo = repo;
        this.productsClient = productsClient;
        this.limits = limits;
    }

    /**
     * Исполнить платёж:
     * 0) идемпотентность: если передан externalId и платёж уже есть — возвращаем его как есть;
     * 1) базовая валидация входа;
     * 2) резерв дневного лимита пользователя (tryReserve);
     * 3) тянем продукт из products-сервиса;
     * 4) проверяем достаточность средств на продукте;
     * 5) создаём Payment в статусе PENDING и сохраняем;
     * 6) проводим "бизнес-операцию" (в этой версии — сразу успешная);
     * 7) переводим платёж в SUCCESS и сохраняем.
     *
     * Важные замечания:
     * - Если на любом шаге после резерва лимита произошла ошибка — лимит возвращаем (restore),
     *   а платёж (если успели создать) помечаем FAILED.
     * - Баланс продукта мы не изменяем (это ответственность product-сервиса).
     */
    @Transactional
    public Payment execute(PaymentRequest req) {
        // --- 0) идемпотентность по externalId ---
        if (!StringUtils.hasText(req.externalId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "externalId is required");
        }
        var existing = repo.findByExternalId(req.externalId());
        if (existing.isPresent()) {
            return existing.get(); // уже выполняли этот запрос — возвращаем прежний результат
        }

        // --- 1) базовые проверки входа (userId/productId/amount) ---
        if (req.userId() == null || req.userId() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId must be positive");
        }
        if (req.productId() == null || req.productId() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "productId must be positive");
        }
        if (req.amount() == null || req.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount must be > 0");
        }

        boolean reserved = false; // факт, что лимит зарезервирован — чтобы понять, нужно ли его возвращать
        Payment p = null;         // ссылка на платёж, если мы его создадим

        try {
            // --- 2) резерв дневного лимита пользователя ---
            // бросит LimitExceededException, если не хватает — вверх улетит 400 (поймает наш ExceptionHandler)
            limits.tryReserve(req.userId(), req.amount());
            reserved = true;

            // --- 3) тянем продукт (если 404 прилетит сверху — перехватит наш @ControllerAdvice) ---
            ProductDto product = productsClient.findById(req.productId());

            // --- 4) проверка достаточности средств на продукте ---
            if (product.balance().compareTo(req.amount()) < 0) {
                // важно: вернуть лимит перед тем как бросить 400
                limits.restore(req.userId(), req.amount());
                reserved = false;
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
            }

            // --- 5) создаём платёж в статусе PENDING ---
            p = new Payment();
            p.setUserId(req.userId());
            p.setProductId(req.productId());
            p.setAmount(req.amount());
            p.setExternalId(req.externalId());
            p.setStatus(PaymentStatus.PENDING);
            p = repo.save(p);

            // --- 6) бизнес-проведение (в этой версии считаем, что операция успешна) ---
            // Если здесь будет интеграция с внешней платёжкой, то ошибки должны перевести платёж в FAILED.

            // --- 7) финализация: SUCCESS ---
            p.setStatus(PaymentStatus.SUCCESS);
            return repo.save(p);
        } catch (RuntimeException ex) {
            // Любая непредвиденная ошибка после резерва — возвращаем лимит
            if (reserved) {
                limits.restore(req.userId(), req.amount());
            }
            // Если платёж уже успели создать — помечаем FAILED
            if (p != null && p.getId() != null && p.getStatus() != PaymentStatus.FAILED) {
                p.setStatus(PaymentStatus.FAILED);
                repo.save(p);
            }
            throw ex;
        }
    }
}