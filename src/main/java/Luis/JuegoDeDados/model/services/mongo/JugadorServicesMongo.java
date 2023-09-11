package Luis.JuegoDeDados.model.services.mongo;

import Luis.JuegoDeDados.model.dto.mongo.AuthResponseMongo;
import Luis.JuegoDeDados.model.dto.mongo.JugadorDtoMongo;
import Luis.JuegoDeDados.model.entity.Role;
import Luis.JuegoDeDados.model.entity.mongo.JugadorEntityMongo;
import Luis.JuegoDeDados.model.entity.mongo.PartidaEntityMongo;
import Luis.JuegoDeDados.model.repository.mongo.JugadorRepositoryMongo;
import Luis.JuegoDeDados.model.repository.mongo.PartidaRepositoryMongo;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Service
public class JugadorServicesMongo {

    @Autowired
    private JugadorRepositoryMongo jugadorRepositoryMongo;
    @Autowired
    private PartidaRepositoryMongo partidaRepositoryMongo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtServiceMongo jwtService;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param nombre El nombre del usuario a registrar.
     * @param email El correo electrónico del usuario a registrar.
     * @param password La contraseña del usuario a registrar.
     * @return Una instancia de AuthResponse que contiene el token generado para el usuario registrado.
     */
    public AuthResponseMongo register(String nombre, String email, String password){
        JugadorEntityMongo usuario = JugadorEntityMongo.builder()
                .id(asignarId())
                .email(email)
                .password(passwordEncoder.encode(password))
                .nombre(filtraNombre(nombre))
                .porcentajeExito(0)
                .role(Role.USER)
                .build();

        jugadorRepositoryMongo.save(usuario);

        return AuthResponseMongo.builder()
                .token(jwtService.getToken(usuario))
                .build();
    }

    /**
     * Busca y devuelve un jugador por su ID.
     *
     * @param id El ID del jugador que se desea buscar.
     * @return El objeto JugadorEntityMongo que representa al jugador con el ID especificado.
     * @throws NotFoundException Si la lista de jugadores está vacía.
     * @throws RuntimeException Si no se encuentra un jugador con el ID proporcionado.
     */
    public JugadorEntityMongo buscarJugadorPorId(String id){

        try {
            List<JugadorEntityMongo> misJugadores = jugadorRepositoryMongo.findAll();

            if (misJugadores.isEmpty()) {
                throw new NotFoundException("Lista de jugadores vacía");
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Lista de jugadores vacía");
        }

        return jugadorRepositoryMongo.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el jugador con el ID proporcionado."));
    }

    /**
     * Actualiza el nombre de un jugador en la base de datos y devuelve la información actualizada en forma de DTO.
     *
     * @param jugador El objeto de entidad de jugador que se desea actualizar.
     * @param nombre El nuevo nombre que se desea asignar al jugador.
     * @return Un objeto DTO que representa al jugador actualizado.
     */
    public JugadorDtoMongo actualizarNombreJugador(JugadorEntityMongo jugador, String nombre){
        String nombreFinal = filtraNombre(nombre);
        jugador.setNombre(nombreFinal);
        jugadorRepositoryMongo.save(jugador);
        return pasarEntidadADto(jugador);
    }

    /**
     * Actualiza el porcentaje de éxito de un jugador y guarda los datos actualizados en la base de datos.
     *
     * @param jugador El objeto JugadorEntityMongo que representa al jugador cuyo porcentaje de éxito se actualizará.
     */
    @Transactional
    public void actualizarPorcentajeExitoJugador(JugadorEntityMongo jugador){
        int porcentajeExitoActualizado = calculaPorcentajeExitoDeUnJugador(jugador);
        jugador.setPorcentajeExito(porcentajeExitoActualizado);
        jugadorRepositoryMongo.save(jugador);
    }

    /**
     * Retorna una lista de todos los jugadores en forma de DTO.
     *
     * @return Una lista de objetos JugadorDtoMongo que representan a todos los jugadores.
     * @throws NotFoundException Si la lista de jugadores está vacía.
     */
    public List<JugadorDtoMongo> listaJugadores() {
        List<JugadorEntityMongo> jugadores = jugadorRepositoryMongo.findAll();
        if (jugadores.isEmpty()) {
            throw new NotFoundException("Lista de Jugadores vacía");
        }
        return jugadores.stream().map(this::pasarEntidadADto)
                .collect(Collectors.toList());
    }

    /**
     * Calcula y devuelve el porcentaje global de victorias entre todos los jugadores.
     *
     * @return El porcentaje global de victorias.
     */
    public int calculaPorcentajeVictoriasGlobales(){
        List<JugadorEntityMongo> jugadores = jugadorRepositoryMongo.findAll();
        if(jugadores.isEmpty()){return 0;}
        int porcentajeExitoGlobal = jugadores.stream()
                .mapToInt(JugadorEntityMongo::getPorcentajeExito)
                .sum();
        if(porcentajeExitoGlobal == 0){return 0;}
        return porcentajeExitoGlobal/jugadores.size();
    }

    /**
     * Obtiene una lista de los peores jugadores basados en su porcentaje de éxito.
     *
     * @return Una lista de objetos JugadorDtoMongo que representan a los peores jugadores.
     * @throws NotFoundException Si no se encuentran jugadores en la base de datos.
     */
    public List<JugadorDtoMongo> peoresJugadores(){
        List<JugadorEntityMongo> todosLosJugadores = jugadorRepositoryMongo.findAll();
        List<JugadorDtoMongo> peoresJugadores = new ArrayList<>();
        int porcentajeMasBajo = 100; //Partimos con el porcentaje más alto

        if (todosLosJugadores.isEmpty()) {
            throw  new NotFoundException("No hay jugadores en la base de datos");
        }

        for (JugadorEntityMongo jugador : todosLosJugadores) {
            int miPorcentajeDeExito = jugador.getPorcentajeExito();

            if(miPorcentajeDeExito < porcentajeMasBajo){
                // Si encontramos un jugador con un porcentaje más bajo, limpiamos la lista anterior
                peoresJugadores.clear();
                porcentajeMasBajo = miPorcentajeDeExito;
            }
            if (miPorcentajeDeExito == porcentajeMasBajo) {
                JugadorDtoMongo jugadorDto = pasarEntidadADto(jugador);
                peoresJugadores.add(jugadorDto);
            }
        }
        return peoresJugadores;
    }

    /**
     * Obtiene una lista de los jugadores con el mismo porcentaje más alto de éxito.
     *
     * @return Una lista de objetos JugadorDtoMongo que representan a los jugadores con el mismo porcentaje más alto.
     * @throws NotFoundException Si no se encuentran jugadores en la base de datos.
     */
    public List<JugadorDtoMongo> mejoresJugadores() throws NotFoundException {
        List<JugadorEntityMongo> todosLosJugadores = jugadorRepositoryMongo.findAll();
        List<JugadorDtoMongo> mejoresJugadores = new ArrayList<>();
        int porcentajeMasAlto = 0; // Partimos con el porcentaje más bajo

        if (todosLosJugadores.isEmpty()) {
            throw new NotFoundException("No hay jugadores en la base de datos");
        }

        for (JugadorEntityMongo jugador : todosLosJugadores) {
            int miPorcentajeDeExito = jugador.getPorcentajeExito();

            if (miPorcentajeDeExito > porcentajeMasAlto) {
                // Si encontramos un jugador con un porcentaje más alto, limpiamos la lista anterior
                mejoresJugadores.clear();
                porcentajeMasAlto = miPorcentajeDeExito;
            }
            if (miPorcentajeDeExito == porcentajeMasAlto) {
                JugadorDtoMongo jugadorDto = pasarEntidadADto(jugador);
                mejoresJugadores.add(jugadorDto);
            }
        }

        return mejoresJugadores;
    }

    //Metodos Privados----------------------------------------------------------------------->>

    /**
     * Calcula el porcentaje de éxito de un jugador basado en la cantidad de victorias en sus partidas.
     * <p>
     * Este método calcula el porcentaje de éxito de un jugador identificado por su ID. El porcentaje se calcula
     * dividiendo la suma de las victorias en las partidas del jugador por la cantidad total de partidas jugadas.
     * El resultado se multiplica por 100 para obtener el porcentaje.
     *
     * @param jugador La entidad del jugador para el que se va a calcular el porcentaje de éxito.
     * @return El porcentaje de éxito del jugador en sus partidas. Si no hay partidas registradas, devuelve 0.
     * @throws RuntimeException Si no se encuentra el jugador con el ID proporcionado.
     */
    private int calculaPorcentajeExitoDeUnJugador(JugadorEntityMongo jugador){
        int porcentajeExito = 0, victorias;
        List<PartidaEntityMongo> misPartidas = partidaRepositoryMongo.findByJugador(jugador);
        int cantidadPartidas = misPartidas.size();

        victorias = misPartidas.stream().mapToInt(PartidaEntityMongo::getVictorias).sum();

        if (cantidadPartidas > 0) {
            porcentajeExito = (victorias * 100) / cantidadPartidas;
        }
        return porcentajeExito;
    }

    /**

     * Convierte una entidad JugadorEntityMongo en un DTO JugadorDtoMongo.
     * <p>
     * Esta función realiza la conversión de una entidad JugadorEntityMongo a un DTO JugadorDtoMongo,
     * asignando las propiedades relevantes de la entidad al DTO resultante.
     *
     * @param jugador La entidad JugadorEntityMongo que se va a convertir.
     * @return Un objeto JugadorDtoMongo que representa la entidad convertida.
     */
    private JugadorDtoMongo pasarEntidadADto(JugadorEntityMongo jugador) {
        return JugadorDtoMongo.builder()
                .id(jugador.getId())
                .nombre(jugador.getNombre())

                .porcentajeExito(jugador.getPorcentajeExito())
                .build();
    }

    /**
     * Filtra y normaliza un nombre, retornando "Anónimo" si el nombre es nulo, vacío o contiene solo espacios en blanco.
     *
     * @param cadena La cadena de texto a filtrar y normalizar.
     * @return El nombre filtrado o "Anónimo" si la cadena es nula, vacía o contiene solo espacios en blanco.
     */
    private String filtraNombre(String cadena){
        if (cadena != null && !cadena.isBlank()) {
            // Eliminar espacios en blanco y números de la cadena
            String nombreFiltrado = cadena.replaceAll("\\s+", "").replaceAll("\\d+", "");

            return nombreFiltrado.isEmpty() ? "Anónimo" : nombreFiltrado;
        } else {
            return "Anónimo";
        }
    }

    /**
     * Obtiene el siguiente ID para un jugador en la base de datos.
     *
     * @return El siguiente ID disponible como cadena.
     */
    private String asignarId() {
        List<JugadorEntityMongo> jugadores = jugadorRepositoryMongo.findAll();

        if (jugadores.isEmpty()) {
            return "1";
        }

        String maxId = jugadores.stream()
                .map(JugadorEntityMongo::getId)
                .max(Comparator.naturalOrder())
                .orElse("0");

        int nextId = Integer.parseInt(maxId) + 1;
        return String.valueOf(nextId);
    }

}
