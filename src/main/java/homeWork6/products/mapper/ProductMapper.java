package homeWork6.products.mapper;


import homeWork6.products.api.dto.ProductDto;
import homeWork6.products.domain.Product;

public final class ProductMapper {
    private ProductMapper() {
    }

    public static ProductDto toDto(Product p) {
        return new ProductDto(
                p.getId(),
                p.getAccountNumber(),
                p.getBalance(),
                p.getType()
        );
    }
}