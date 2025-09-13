package homeWork8.limits.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "limits")
public class LimitsProperties {
    /**
     * Дефолтный дневной лимит (по умолчанию 10000.00)
     */
    private BigDecimal defaultAmount = new BigDecimal("10000.00");

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(BigDecimal defaultAmount) {
        this.defaultAmount = defaultAmount;
    }
}