package Luis.JuegoDeDados.controllers.mysql;


import Luis.JuegoDeDados.model.dto.mysql.AuthResponseMysql;
import Luis.JuegoDeDados.model.services.mysql.AuthServiceMysql;
import Luis.JuegoDeDados.model.services.mysql.JugadorServiceJpa;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllerMysql {

    private final AuthServiceMysql authService;
    private final JugadorServiceJpa jugadorServiceJpa;

    @Operation(summary = "Registro de un usuario",description = "Registra tus datos para darte de alta como jugador")
    @ApiResponse(responseCode = "200", description = "Registro exitoso")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @PostMapping(value = "register")
    public ResponseEntity<AuthResponseMysql> register(@RequestParam String nombre, @RequestParam String email, @RequestParam String password){
        return ResponseEntity.ok(jugadorServiceJpa.register(nombre, email,password));
    }

    @PostMapping(value = "login")
    @Operation(summary = "Inicio de sesión", description = "Introduce tus datos de usuario para generar un token que podrás usar para acceder a los demás endpoints")
    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    public ResponseEntity<AuthResponseMysql> login(@RequestParam String email, String password) {
        return ResponseEntity.ok(authService.login(email, password));
    }


}
