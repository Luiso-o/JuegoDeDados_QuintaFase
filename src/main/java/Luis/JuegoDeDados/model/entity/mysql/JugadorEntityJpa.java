package Luis.JuegoDeDados.model.entity.mysql;

import Luis.JuegoDeDados.model.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información de la entidad Jugador")
@Table(name = "jugador", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class JugadorEntityJpa implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;
    @Column(nullable = false)
    private String email;
    @Column(name = "NombreJugador")
    private String nombre;
    @Column(name = "porcentajeExito")
    private int porcentajeExito;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;


    /**
     * Obtiene las autoridades (roles) asociadas al usuario.
     *
     * @return Una colección de objetos que representan las autoridades del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((role.name())));
    }

    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indica si la cuenta del usuario no ha expirado.
     *
     * @return true si la cuenta no ha expirado, false si ha expirado.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta del usuario no está bloqueada.
     *
     * @return true si la cuenta no está bloqueada, false si está bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales del usuario (contraseña) no han expirado.
     *
     * @return true si las credenciales no han expirado, false si han expirado.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta de usuario está habilitada.
     *
     * @return true si la cuenta está habilitada, false si está deshabilitada.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
