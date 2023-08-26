package Luis.JuegoDeDados.exceptions;

public class ItemsNotFoundException extends RuntimeException{
    public ItemsNotFoundException(){
        super("El jugador no tiene partidas que eliminar");
    }
}