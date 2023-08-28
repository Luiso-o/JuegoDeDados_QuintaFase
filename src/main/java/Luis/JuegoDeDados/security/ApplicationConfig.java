package Luis.JuegoDeDados.security;

import Luis.JuegoDeDados.model.repository.mysql.JugadorRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Autowired
    private final JugadorRepositoryJpa jugadorRepositoryJpa;

    /**
     * Configura y devuelve el administrador de autenticación.
     *
     * @param config La configuración de autenticación a utilizar.
     * @return El administrador de autenticación configurado.
     * @throws Exception Si ocurre algún error al configurar el administrador de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{
        return config.getAuthenticationManager();
    }

    /**
     * Configura y devuelve el proveedor de autenticación.
     *
     * @return El proveedor de autenticación configurado.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Configura y devuelve un codificador de contraseñas BCrypt.
     *
     * @return El codificador de contraseñas BCrypt configurado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura y devuelve un servicio de detalles de usuario personalizado.
     *
     * @return El servicio de detalles de usuario personalizado.
     */
    @Bean
    public UserDetailsService userDetailService() {
        return username -> jugadorRepositoryJpa.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
