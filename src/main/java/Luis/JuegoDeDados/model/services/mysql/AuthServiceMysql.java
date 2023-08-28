package Luis.JuegoDeDados.model.services.mysql;

import Luis.JuegoDeDados.model.dto.mysql.AuthResponseMysql;
import Luis.JuegoDeDados.model.entity.mysql.JugadorEntityJpa;
import Luis.JuegoDeDados.model.repository.mysql.JugadorRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceMysql {
    private final JugadorRepositoryJpa jugadorRepositoryJpa;
    private final JwtServiceMysql jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Realiza el proceso de autenticación de un usuario y genera un token de acceso si las credenciales son válidas.
     *
     * @param email Objeto que contiene el nombre de usuario y la contraseña ingresados por el usuario.
     * @return Un objeto {@link AuthResponseMysql} que contiene el token de acceso generado.
     * @throws AuthenticationException Si las credenciales no son válidas o la autenticación falla.
     */
    public AuthResponseMysql login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        JugadorEntityJpa user = jugadorRepositoryJpa.findByEmail(email).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponseMysql.builder()
                .token(token)
                .build();
    }

}

