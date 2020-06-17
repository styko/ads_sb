package io.styko.common.persistance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.styko.common.config.GsonConfig;
import io.styko.common.converter.PriceConverter;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({PriceConverter.class, GsonConfig.class})
class AdRepositoryTest {

  @Autowired
  private AdRepository adRepository;

  private Ad ad;

  @BeforeEach
  void setUp() {
    ad = createAd();
    adRepository.save(ad);
  }

  private Ad createAd() {
    return Ad.builder()
        .link("link")
        .address("address")
        .category("category")
        .contact("contact")
        .countOfUpdates(1)
        .created(Instant.now())
        .description("description")
        .lastPrice(BigDecimal.ONE)
        .currency("EUR")
        .locality("locality")
        .address("address")
        .size(85)
        .title("title")
        .updated(Instant.now())
        .deactivated(Instant.now())
        .prices(Arrays.asList(
            Price.builder().updated(Instant.now()).value(BigDecimal.ONE).build(),
            Price.builder().updated(Instant.now()).value(BigDecimal.ONE).build()
        ))
        .galleryUrl("galleryUrl")
        .build();
  }

  @Test
  void base() {
    List<Ad> allAdds = adRepository.findAll();
    assertThat(allAdds.size()).isEqualTo(1);
    assertThat(adRepository.findById(allAdds.get(0).getId()).get()).isEqualTo(ad);
  }

  @Test
  void findByLink() {
    assertThat(adRepository.findByLink("link").get()).isEqualTo(ad);
  }

  @Test
  void findAllByDeactivatedIsNull() {
    Ad ad2 = createAd();
    ad2.setLink("link2");
    ad2.setDeactivated(null);
    adRepository.save(ad2);

    List<String> linksOfActiveAds = adRepository.findAllLinksByDeactivatedIsNull();
    assertThat(linksOfActiveAds.size()).isEqualTo(1);
    assertThat(linksOfActiveAds.get(0)).isEqualTo("link2");
  }

  @Test
  void findAllByDeactivatedIsNotNull() {
    Ad ad2 = createAd();
    ad2.setLink("link2");
    ad2.setDeactivated(null);
    adRepository.save(ad2);

    List<Ad> linksOfActiveAds = adRepository.findAllByDeactivatedIsNotNull();
    assertThat(linksOfActiveAds.size()).isEqualTo(1);
    assertThat(linksOfActiveAds.get(0)).isEqualTo(ad);
  }
}
