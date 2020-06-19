package io.styko.common.persistance;

import io.styko.common.converter.PriceConverter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ad")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
public class Ad implements Serializable {
  private static final long serialVersionUID = -8664906054364226344L;

  @Id
  @GeneratedValue(generator = "seq_ad_id_generator" ,strategy= GenerationType.SEQUENCE)
  @SequenceGenerator(name="seq_ad_id_generator", sequenceName = "seq_ad_id", allocationSize = 1)
  private Long id;

  private String link;

  private String title;

  private BigDecimal lastPrice;

  private String currency;

  @Convert(converter = PriceConverter.class)
  private List<Price> prices = new ArrayList<>();

  private String locality;

  private String address;

  private Integer size;

  private String category; // house, flat,..

  private String description;

  private String contact; // email, mobile,..

  private Instant created;

  private Instant updated;

  private Instant deactivated;

  private Integer countOfUpdates; // how many times was ad updated

  private String galleryUrl;

  private Double latitude;

  private Double longitude;
}
