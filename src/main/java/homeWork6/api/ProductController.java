package homeWork6.api;

import homeWork6.api.dto.ProductDto;
import homeWork6.api.mapper.ProductMapper;
import homeWork6.repo.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository products;

    public ProductController(ProductRepository products) {
        this.products = products;
    }

    @GetMapping
    public List<ProductDto> byUser(@RequestParam(required = false) Long userId) throws InterruptedException {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
        }
        //Thread.sleep(5000);
        return products.findAllByUserId(userId).stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductDto byId(@PathVariable Long id) {
        return products.findById(id)
                .map(ProductMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
    }
}