package Luis.JuegoDeDados.model.dto.mysql;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JugadorDtoJpa {

    @Schema(description = "Identificador único de jugador",example = "1")
    private Long id;
    @Schema(defaultValue = "Anónimo",description = "Aquí se almacenará el nombre del jugador", example = "Carlos")
    private String nombre;
    @Schema(defaultValue = "0",description = "Aquí está el porcentaje de victorias de tus partidas, expresado en escala de 100%", example = "20")
    private int porcentajeExito;

}
