package io.styko.security.persistance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.styko.common.config.GsonConfig;
import io.styko.common.converter.PriceConverter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({PriceConverter.class, GsonConfig.class})
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  private User user;

  @BeforeEach
  void setUp() {
    user = User.builder()
        .username("styko")
        .email("styko@styko.sk")
        .password("styko")
        .roles(Set.of(ERole.ROLE_USER, ERole.ROLE_ADMIN))
        .build();
    userRepository.save(user);
  }

  @Test
  void base() {
    List<User> allUsers = userRepository.findAll();
    assertThat(allUsers.size()).isEqualTo(1);
    assertThat(userRepository.findById("styko").get()).isEqualTo(user);
  }
}
