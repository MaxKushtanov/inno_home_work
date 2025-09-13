package homeWork8.limits.repo;

import homeWork7.payments.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByExternalId(String externalId);

    List<Payment> findAllByUserId(Long userId);
}