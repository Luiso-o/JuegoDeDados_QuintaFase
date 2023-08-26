package Luis.JuegoDeDados.exceptions;


import org.webjars.NotFoundException;

public class GamesNotFoundInThisPlayerException extends NotFoundException {

    public GamesNotFoundInThisPlayerException(){
        super("No se encontraron partidas a este jugador");
    }

}