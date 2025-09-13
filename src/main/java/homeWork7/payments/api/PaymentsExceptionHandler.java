package homeWork7.payments.api;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice(basePackages = {
        "homeWork6.products",
        "homeWork7.payments"
})
public class PaymentsExceptionHandler {

    /**
     * 4xx от продуктового сервиса — прокидываем как есть (сохраняем ProblemDetail от upstream).
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ProblemDetail onClientError(HttpClientErrorException ex) {
        var body = ex.getResponseBodyAs(ProblemDetail.class);
        if (body != null) {
            return body; // уже готовый ProblemDetail из products
        }
        var pd = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getMessage());
        pd.setTitle("Upstream client error");
        return pd;
    }

    /**
     * Таймауты/сетевые проблемы к products — 504.
     */
    @ExceptionHandler(ResourceAccessException.class)
    public ProblemDetail onNetwork(ResourceAccessException ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.GATEWAY_TIMEOUT, "Upstream timeout or network error");
        pd.setTitle("Payments upstream error");
        return pd;
    }

    /**
     * СЕМИНАР 16: Circuit Breaker открыт — 503 Service Unavailable.
     */
    @ExceptionHandler(CallNotPermittedException.class)
    public ProblemDetail onCircuitBreakerOpen(CallNotPermittedException ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, "Upstream service temporarily unavailable");
        pd.setTitle("Circuit breaker open");
        return pd;
    }

    /**
     * Локальная валидация тела (@RequestBody) 400 — Например, @Valid на DTO.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail onValidation(MethodArgumentNotValidException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation failed");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    /**
     * Локальная валидация параметров (@RequestParam/@PathVariable) 400 при @Validated на контроллере.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail onConstraintViolation(ConstraintViolationException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation failed");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    /**
     * Наши осознанные бизнес-ответы через ResponseStatusException (например, insufficient funds / payment not found).
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail onResponseStatus(ResponseStatusException ex) {
        var pd = ProblemDetail.forStatus(ex.getStatusCode());
        // Делаем заголовки более читабельными и единообразными
        var status = ex.getStatusCode();
        if (status.is4xxClientError()) {
            pd.setTitle(status == HttpStatus.NOT_FOUND ? "Resource not found" : "Request failed");
        } else if (status.is5xxServerError()) {
            pd.setTitle("Payments internal error");
        } else {
            pd.setTitle("Request failed");
        }
        pd.setDetail(ex.getReason());
        return pd;
    }

    /**
     * Прочие неожиданные ошибки — 500.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail onAny(Exception ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        pd.setTitle("Payments internal error");
        return pd;
    }
}