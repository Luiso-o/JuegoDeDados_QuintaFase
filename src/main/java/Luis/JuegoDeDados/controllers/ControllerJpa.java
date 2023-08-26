package Luis.JuegoDeDados.controllers;

import Luis.JuegoDeDados.exceptions.GamesNotFoundInThisPlayerException;
import Luis.JuegoDeDados.exceptions.ItemsNotFoundException;
import Luis.JuegoDeDados.exceptions.PlayerNotFoundException;
import Luis.JuegoDeDados.model.dto.mysql.JugadorDtoJpa;
import Luis.JuegoDeDados.model.dto.mysql.PartidaDtoJpa;
import Luis.JuegoDeDados.model.entity.mysql.JugadorEntityJpa;
import Luis.JuegoDeDados.model.services.mysql.JugadorServiceJpa;
import Luis.JuegoDeDados.model.services.mysql.PartidaServiceJpa;
import com.mongodb.lang.Nullable;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@AllArgsConstructor
@Builder
@RequestMapping("/jpa")
public class ControllerJpa {

    @Autowired
    private final JugadorServiceJpa jugadorServiceJpa;

    @Autowired
    private final PartidaServiceJpa partidaServiceJpa;

    @Operation(summary = "Crea un nuevo jugador", description = "devuelve un objeto jugador,recibirá un parametro de tipo String, si no recibe nada devolverá un Anónimo")
    @ApiResponse(responseCode = "200", description = "Nuevo Jugador Guardado con éxito")
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    @PostMapping
    public ResponseEntity<JugadorDtoJpa> crearNuevoUsuario
            (@Nullable
             @RequestParam
             @Pattern(regexp = "^[a-zA-Z]*$",message = "El nombre debe contener solo letras") String nombre)
    {
        JugadorDtoJpa jugadorNuevo = jugadorServiceJpa.crearJugador(nombre);
        return ResponseEntity.ok(jugadorNuevo);
    }

    @Operation(summary = "Actualiza el nombre de un Jugador", description = "Actualizará el nombre del jugador correspondiente al id introducido")
    @ApiResponse(responseCode = "200", description = "Nombre de jugador actualizado con éxito")
    @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    @PutMapping("/{id}")
    public ResponseEntity<JugadorDtoJpa> actualizarJugador
            (@PathVariable Long id,
             @Nullable
             @RequestParam
             @Pattern(regexp = "^[a-zA-Z]*$",message = "El nombre debe contener solo letras") String nombre)
    {
        try {
            JugadorEntityJpa jugadorEntidad = jugadorServiceJpa.buscarJugadorPorId(id);
            JugadorDtoJpa jugador = jugadorServiceJpa.actualizarNombreJugador(jugadorEntidad, nombre);
            return ResponseEntity.ok(jugador);
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Juega una partida", description = "Lanza los dados y devuelve los resultados de la partida")
    @ApiResponse(responseCode = "200", description = "Partida realizada y guardada con éxito")
    @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    @PostMapping("/{id}/juego")
    public ResponseEntity<PartidaDtoJpa> tirarDados(@PathVariable long id){
        try{
            JugadorEntityJpa jugador = jugadorServiceJpa.buscarJugadorPorId(id);
            PartidaDtoJpa nuevaPartida = partidaServiceJpa.crearPartida(jugador);
            jugadorServiceJpa.actualizarPorcentajeExitoJugador(jugador);
            return ResponseEntity.ok(nuevaPartida);
        }catch (PlayerNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina las partidas de un Jugador", description = "recibe el id de un jugador y elimina sus partidas")
    @ApiResponse(responseCode = "204", description = "No Hay Partidas")
    @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    @DeleteMapping("/{id}/partidas")
    public ResponseEntity<String> eliminarPartidasDeUnJugador(@PathVariable long id) {
        try{
            JugadorEntityJpa jugador = jugadorServiceJpa.buscarJugadorPorId(id);
            partidaServiceJpa.eliminarPartidasDeJugador(jugador);
            jugadorServiceJpa.actualizarPorcentajeExitoJugador(jugador);
            return ResponseEntity.ok("Partidas eliminadas con éxito");
        }catch (PlayerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error " + e.getMessage());
        }catch (ItemsNotFoundException i) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Error: " + i.getMessage());
        }
    }

    @Operation(summary = "Ver lista de jugadores",description = "Devuelve la lista de los jugadores con su porcentaje éxito")
    @ApiResponse(responseCode = "200", description = "Lista procesada con éxito")
    @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    @GetMapping()
    public List<JugadorDtoJpa> obtenerListaJugadoresConPorcentajeMedioExito() {
        List<JugadorDtoJpa> jugadores = jugadorServiceJpa.listaJugadores();
        return ResponseEntity.ok(jugadores).getBody();
    }

    @Operation(summary = "Busca Partidas de un jugador", description = "Encontrará las partidas de un jugador por su id")
    @ApiResponse(responseCode = "200", description = "Partidas encontradas con éxito")
    @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    @GetMapping("/{id}/partidas")
    public ResponseEntity<Map<String, Object>> muestraPartidasDeUnJugador(@PathVariable long id) {
        try {
            JugadorEntityJpa jugador = jugadorServiceJpa.buscarJugadorPorId(id);
            List<PartidaDtoJpa> partidas = partidaServiceJpa.encuentraPartidasJugador(jugador);

            Map<String, Object> respuesta = new HashMap<>();
            if (partidas.isEmpty()) {
                respuesta.put("mensaje", "No se encontraron partidas para el jugador " + jugador.getNombre());
                return ResponseEntity.noContent().build();
            } else {
                respuesta.put("mensaje", "Lista de partidas del jugador " + jugador.getNombre());
                respuesta.put("partidas", partidas);
                return ResponseEntity.ok(respuesta);
            }
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (GamesNotFoundInThisPlayerException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "Ranking de victorias",description = "Muestra el porcentaje total de victorias de todos los jugadores")
    @ApiResponse(responseCode = "200", description = "Porcentaje realizado con éxito")
    @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    @GetMapping("/ranking")
    public ResponseEntity<Map<String, Object>> muestraPorcentajeVictorias(){
        int porcentaje = jugadorServiceJpa.calculaPorcentajeVictoriasGlobales();
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("porcentaje de victorias globales", porcentaje + "%");
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Revisa peores jugadores",description = "Muestra los jugadores con el porcentaje más bajo de victorias")
    @ApiResponse(responseCode = "200", description = "Lista encontrada con éxito")
    @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    @GetMapping("/ranking/peores")
    public ResponseEntity<List<JugadorDtoJpa>> peoresPorcentajes() {
        List<JugadorDtoJpa> peoresJugadores = jugadorServiceJpa.peoresJugadores();
        return ResponseEntity.ok(peoresJugadores);
    }

    @Operation(summary = "Revisa mejores jugadores",description = "Muestra los jugadores con el porcentaje más alto de victorias")
    @ApiResponse(responseCode = "200", description = "Lista encontrada con éxito")
    @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    @GetMapping("/ranking/mejores")
    public ResponseEntity<List<JugadorDtoJpa>> mejoresPorcentajes() {
        List<JugadorDtoJpa> peoresJugadores = jugadorServiceJpa.mejoresJugadores();
        return ResponseEntity.ok(peoresJugadores);
    }
}