package Luis.JuegoDeDados.model.repository.mongo;

import Luis.JuegoDeDados.model.entity.mongo.JugadorEntityMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JugadorRepositoryMongo extends MongoRepository<JugadorEntityMongo,String>{

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param email El nombre de usuario del usuario a buscar.
     * @return Un Optional que puede contener al usuario si se encuentra, o estar vacío si no se encuentra.
     */
    Optional<JugadorEntityMongo> findByEmail(String email);

}