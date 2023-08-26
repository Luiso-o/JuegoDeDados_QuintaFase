package Luis.JuegoDeDados.model.entity.mongo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información de la entidad Jugador")
@Document(collection = "Jugadores")
public class JugadorEntityMongo {

    @MongoId
    private String id;
    private String nombre;
    private int porcentajeExito;

}
