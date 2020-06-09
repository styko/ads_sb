package io.styko.common.persistance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Price implements Serializable {
  private static final long serialVersionUID = -8895280459261409716L;

  private Instant updated;

  private BigDecimal value;
}
