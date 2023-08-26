package Luis.JuegoDeDados.model.repository.mysql;

import Luis.JuegoDeDados.model.entity.mysql.JugadorEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JugadorRepositoryJpa extends JpaRepository<JugadorEntityJpa,Long> {
}
