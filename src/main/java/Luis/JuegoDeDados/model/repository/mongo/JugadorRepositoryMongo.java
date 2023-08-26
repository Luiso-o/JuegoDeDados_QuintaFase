package Luis.JuegoDeDados.model.repository.mongo;

import Luis.JuegoDeDados.model.entity.mongo.JugadorEntityMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JugadorRepositoryMongo extends MongoRepository<JugadorEntityMongo,String>{
}