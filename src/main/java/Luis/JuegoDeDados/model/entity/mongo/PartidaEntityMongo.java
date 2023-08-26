package Luis.JuegoDeDados.model.entity.mongo;

import Luis.JuegoDeDados.model.entity.mongo.JugadorEntityMongo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información de una partida")
@Document(collection = "partidas")
public class PartidaEntityMongo {

    @MongoId
    private String id;
    private LocalDate fecha;
    private int victorias;
    private int derrotas;
    @DBRef
    private JugadorEntityMongo jugador;
}
