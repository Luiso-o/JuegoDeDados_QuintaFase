package Luis.JuegoDeDados.model.services.mongo;

import Luis.JuegoDeDados.model.dto.mongo.PartidaDtoMongo;
import Luis.JuegoDeDados.model.entity.mongo.JugadorEntityMongo;
import Luis.JuegoDeDados.model.entity.mongo.PartidaEntityMongo;
import Luis.JuegoDeDados.model.repository.mongo.JugadorRepositoryMongo;
import Luis.JuegoDeDados.model.repository.mongo.PartidaRepositoryMongo;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Service
public class PartidaServiceMongo {

    @Autowired
    private PartidaRepositoryMongo partidaRepositoryMongo;
    @Autowired
    private JugadorRepositoryMongo jugadorRepositoryMongo;

    /**
     * Crea una nueva partida para un jugador.
     *
     * @param jugador Jugador para el cual se creará la partida.
     * @return Objeto PartidaDtoMongo que representa la partida creada.
     */
    public PartidaDtoMongo crearPartida(JugadorEntityMongo jugador){
        int lanzada = tirarDados();
        PartidaEntityMongo nuevaPartida = PartidaEntityMongo.builder()
                .fecha(LocalDate.now())
                .victorias(lanzada <= 7 ? 1 : 0)
                .derrotas(lanzada > 7 ? 1 : 0)
                .jugador(jugador)
                .build();
        partidaRepositoryMongo.save(nuevaPartida);
        return pasarEntityADto(nuevaPartida);
    }

    /**
     * Elimina todas las partidas asociadas a un jugador.
     *
     * @param jugador El jugador del cual se desean eliminar las partidas.
     * @throws RuntimeException Si el jugador no tiene partidas que eliminar.
     */
    public void eliminarPartidasDeJugador(JugadorEntityMongo jugador){
        List<PartidaEntityMongo> misPartidas = partidaRepositoryMongo.findByJugador(jugador);
        if (misPartidas.isEmpty()) {
            throw new RuntimeException("El jugador no tiene partidas que eliminar");
        }
        misPartidas.forEach(partida -> partidaRepositoryMongo.delete(partida));
        jugadorRepositoryMongo.save(jugador);
    }

    /**
     * Encuentra y devuelve las partidas de un jugador en forma de DTO.
     *
     * @param jugador El jugador del cual se desean obtener las partidas.
     * @return Una lista de objetos PartidaDtoMongo que representan las partidas del jugador.
     * @throws NotFoundException Si no se encuentran partidas para el jugador.
     */
    public List<PartidaDtoMongo> encuentraPartidasJugador(JugadorEntityMongo jugador){
        List<PartidaEntityMongo> misPartidas = partidaRepositoryMongo.findByJugador(jugador);
        if(misPartidas.isEmpty()){
            throw new NotFoundException("No se le encontraron partidas a este jugador");
        }

        return misPartidas.stream()
                .map(this::pasarEntityADto)
                .collect(Collectors.toList());

    }

    //Metodos privados ---------------------------------------------------------------->>
    /**
     * Convierte una entidad PartidaEntityMongo en un DTO PartidaDtoMongo.
     *
     * @param partidaEntity Entidad PartidaEntityMongo a convertir.
     * @return Objeto PartidaDtoMongo resultante.
     */
    private PartidaDtoMongo pasarEntityADto(PartidaEntityMongo partidaEntity) {
        return PartidaDtoMongo.builder()
                .id(partidaEntity.getId())
                .fecha(partidaEntity.getFecha())
                .mensaje(partidaEntity.getVictorias() == 1 ? "Ganaste :D" : "Perdiste :V")
                .build();
    }

    /**
     * Genera un número aleatorio entre 1 y 12, simulando el resultado de tirar dos dados.
     *
     * @return El número aleatorio generado.
     */
    private int tirarDados(){
        return (int)Math.floor(Math.random() * 12) + 1;
    }
}
