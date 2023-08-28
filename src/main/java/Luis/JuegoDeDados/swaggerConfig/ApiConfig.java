package Luis.JuegoDeDados.swaggerConfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Luis",
                        email = "cheportillo@gmail.com",
                        url = "https://github.com/Luiso-o"
                ),
                description = """
                        API para gestionar jugadores y partidas en el juego de dados
                        Base de datos : Mysql WorkBench && MongoDB
                        Seguridad : Spring Security && Json Web Token.""",

                title = "Juego de dados con Jwt && Security",
                version = "1.0",
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(
                        description = "local ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://github.com/Luiso-o"
                )
        }
)
@SecurityScheme(
        name = "bearerauth",
        description = "Introduce tu Jwt Token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class ApiConfig {
}
