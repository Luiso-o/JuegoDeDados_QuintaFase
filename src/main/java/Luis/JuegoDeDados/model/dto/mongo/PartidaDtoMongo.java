package Luis.JuegoDeDados.model.dto.mongo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartidaDtoMongo {

    @Schema(description = "Identificador único de la partida",example = "1")
    private String id;
    @Schema(description = "Fecha de la partida en formato dd-MM-yyyy", example = "2023-08-11")
    private LocalDate fecha;
    @Schema(description = "Mensaje que indica si el jugador ganó o perdió", example = "Ganaste :D")
    private String mensaje;
}