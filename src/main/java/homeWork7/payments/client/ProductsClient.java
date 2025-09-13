package homeWork7.payments.client;

import homeWork6.products.api.dto.ProductDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class ProductsClient {
    private final RestClient rc;

    public ProductsClient(RestClient productsRestClient) {
        this.rc = productsRestClient;
    }

    @Retry(name = "products")
    @CircuitBreaker(name = "products")
    public List<ProductDto> findByUser(Long userId) {
        try {
            return rc.get()
                    .uri(uri -> uri.queryParam("userId", userId).build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ProductDto>>() {
                    });
        } catch (HttpClientErrorException e) {
            // Пробрасываем 4xx с телом ProblemDetail дальше — разберём в @ControllerAdvice
            // 4xx НЕ ретраятся согласно конфигурации resilience4j.retry.instances.products.ignore-exceptions
            throw e;
        } catch (RestClientException e) {
            // таймауты/сетевые и пр. - будут ретраиться
            throw e;
        }
    }

    @Retry(name = "products")
    @CircuitBreaker(name = "products")
    public ProductDto findById(Long id) {
        try {
            return rc.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .body(ProductDto.class);
        } catch (HttpClientErrorException e) {
            // 4xx НЕ ретраятся согласно конфигурации
            throw e;
        } catch (RestClientException e) {
            // таймауты/сетевые и пр. - будут ретраиться
            throw e;
        }
    }
}