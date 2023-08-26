package Luis.JuegoDeDados.exceptions;

public class EmptyPlayersListException extends RuntimeException{
    public EmptyPlayersListException(){
        super("Lista de jugadores vacía");
    }
}
