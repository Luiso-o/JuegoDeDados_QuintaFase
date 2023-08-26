package Luis.JuegoDeDados.exceptions;

public class PlayerNotFoundException extends RuntimeException{
    public PlayerNotFoundException(Object id){
        super("No se encontró el jugador con el ID: " + id);
    }
}
