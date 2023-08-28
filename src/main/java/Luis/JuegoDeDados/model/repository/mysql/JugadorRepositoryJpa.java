package Luis.JuegoDeDados.model.repository.mysql;

import Luis.JuegoDeDados.model.entity.mysql.JugadorEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JugadorRepositoryJpa extends JpaRepository<JugadorEntityJpa,Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param email El nombre de usuario del usuario a buscar.
     * @return Un Optional que puede contener al usuario si se encuentra, o estar vacío si no se encuentra.
     */
    Optional<JugadorEntityJpa> findByEmail(String email);

}
