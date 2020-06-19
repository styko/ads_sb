package io.styko.common.persistance;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "notices", path = "notices")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public interface AdRepository extends JpaRepository<Ad, Long> {
  Optional<Ad> findByLink(String link);

  @Query("select a.link from Ad a where a.deactivated is null")
  List<String> findAllLinksByDeactivatedIsNull();

  Page<Ad> findAllByDeactivatedIsNotNull(Pageable pageable);
}
