package homeWork7.payments.api;

import homeWork6.products.api.dto.ProductDto;
import homeWork7.payments.api.dto.PaymentRequest;
import homeWork7.payments.client.ProductsClient;
import homeWork7.payments.domain.Payment;
import homeWork7.payments.service.PaymentService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@Validated
public class PaymentsController {

    private final PaymentService service;
    private final ProductsClient productsClient;


    public PaymentsController(PaymentService service, ProductsClient productsClient) {
        this.productsClient = productsClient;
        this.service = service;
    }

    /**
     * Проксируем запрос к продуктам: /payments/products?userId=...
     */
    @GetMapping("/products")
    public List<ProductDto> productsByUser(@RequestParam @NotNull @Min(1) Long userId) {
        return productsClient.findByUser(userId);
    }

    /**
     * Создать новый платёж
     */
    @PostMapping("/pay")
    @ResponseStatus(HttpStatus.CREATED)
    public Payment pay(@RequestBody PaymentRequest req) {
        return service.execute(req);
    }
}