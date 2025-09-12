package homeWork6.api.mapper;


import homeWork6.api.dto.ProductDto;
import homeWork6.domain.Product;

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