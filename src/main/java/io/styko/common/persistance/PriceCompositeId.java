package io.styko.common.persistance;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Embeddable
public class PriceCompositeId implements Serializable {
  private static final long serialVersionUID = -5205633124060868167L;

  @Column(nullable=false)
  private Instant parsed;

  @Column(nullable=false)
  private Long adId;
}
