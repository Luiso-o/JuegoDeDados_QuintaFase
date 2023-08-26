package Luis.JuegoDeDados.model.entity.mysql;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información de la entidad Jugador")
@Table(name = "jugador")
public class JugadorEntityJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @Column(name = "NombreJugador")
    private String nombre;

    @Column(name = "porcentajeExito")
    private int porcentajeExito;

}
