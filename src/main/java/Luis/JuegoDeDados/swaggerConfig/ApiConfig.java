package Luis.JuegoDeDados.swaggerConfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Luis",
                        email = "cheportillo@gmail.com",
                        url = "https://github.com/Luiso-o"
                ),
                description = "API para gestionar jugadores y partidas en el juego de dados usando MongoDb y Mysql Workbench como base de datos persistentes",
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
public class ApiConfig {
}
