package Luis.JuegoDeDados.model.services.mongo;

import Luis.JuegoDeDados.model.dto.mongo.AuthResponseMongo;
import Luis.JuegoDeDados.model.entity.mongo.JugadorEntityMongo;
import Luis.JuegoDeDados.model.repository.mongo.JugadorRepositoryMongo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceMongo {
    private final JugadorRepositoryMongo jugadorRepositoryMongo;
    private final JwtServiceMongo jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Realiza el proceso de autenticación de un usuario y genera un token de acceso si las credenciales son válidas.
     *
     * @param email Objeto que contiene el nombre de usuario y la contraseña ingresados por el usuario.
     * @return Un objeto {@link AuthResponseMongo} que contiene el token de acceso generado.
     * @throws AuthenticationException Si las credenciales no son válidas o la autenticación falla.
     */
    public AuthResponseMongo login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        JugadorEntityMongo user = jugadorRepositoryMongo.findByEmail(email).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponseMongo.builder()
                .token(token)
                .build();
    }

}