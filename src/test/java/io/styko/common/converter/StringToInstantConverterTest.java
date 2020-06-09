package io.styko.common.converter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StringToInstantConverterTest {

  @InjectMocks
  private StringToInstantConverter stringToInstantConverter;

  @Mock
  private Clock clock;

  @Test
  void convertTest(){
    when(clock.instant()).thenReturn(Instant.parse("2020-07-24T13:00:00Z"));

    assertThat(stringToInstantConverter.convert("25.5.2020 20:36:31")).isEqualTo(Instant.parse("2020-05-25T18:36:31Z"));
    assertThat(stringToInstantConverter.convert("1.5.2020 20:36:31")).isEqualTo(Instant.parse("2020-05-01T18:36:31Z"));
  }
}
