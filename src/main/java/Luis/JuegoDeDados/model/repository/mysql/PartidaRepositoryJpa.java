package Luis.JuegoDeDados.model.repository.mysql;

import Luis.JuegoDeDados.model.entity.mysql.JugadorEntityJpa;
import Luis.JuegoDeDados.model.entity.mysql.PartidaEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidaRepositoryJpa extends JpaRepository<PartidaEntityJpa,Long> {
    /**
     * Busca y devuelve una lista de partidas asociadas a un jugador específico.
     *
     * @param jugador El jugador para el que se desea buscar las partidas.
     * @return Una lista de objetos PartidaEntityJpa relacionados con el jugador.
     */
    List<PartidaEntityJpa> findByJugador(JugadorEntityJpa jugador);
}
