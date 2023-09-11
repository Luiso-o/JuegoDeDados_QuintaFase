package Luis.JuegoDeDados.model.services.mongo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceMongo {
    /**
     * Clave secreta utilizada para firmar y verificar tokens JWT.
     * Asegúrese de que esta clave se mantenga segura y no se comparta públicamente.
     */
    private static final String SECRET_KEY = "MzI3MjQ1NDA2NTI2MTMzOTQzODc4MzQxMTk5MTYxMzE";

    /**
     * Genera un token JWT para el usuario proporcionado con reclamos adicionales personalizados.
     *
     * @param user El objeto UserDetails del usuario para el que se generará el token.
     * @return El token JWT generado.
     */
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(),user);
    }

    /**
     * Obtiene una clave a partir de la clave secreta en formato base64.
     *
     * @return La clave generada a partir de la clave secreta.
     */
    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Obtiene el nombre de usuario almacenado en el token JWT.
     *
     * @param token El token JWT del cual se extraerá el nombre de usuario.
     * @return El nombre de usuario contenido en el token JWT.
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Verifica si un token JWT es válido para un usuario específico.
     *
     * @param token El token JWT que se va a verificar.
     * @param userDetails Los detalles del usuario para comparar con el token.
     * @return true si el token es válido y corresponde al usuario; false en caso contrario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Obtiene una reclamación (claim) específica del token JWT utilizando un resolvedor de reclamaciones personalizado.
     *
     * @param token           El token JWT del que se extraerá la reclamación.
     * @param claimsResolver  El resolvedor de reclamaciones que se utilizará para obtener la reclamación deseada.
     * @param <T>             El tipo de la reclamación deseada.
     * @return La reclamación específica del token JWT.
     * @throws io.jsonwebtoken.MalformedJwtException Si el token JWT está mal formado o no puede ser analizado correctamente.
     * @throws io.jsonwebtoken.ExpiredJwtException Si el token JWT ha expirado.
     * @throws io.jsonwebtoken.UnsupportedJwtException Si el token JWT tiene un formato o contenido no soportado.
     */
    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Obtiene la fecha de expiración del token JWT.
     *
     * @param token El token JWT del que se obtendrá la fecha de expiración.
     * @return La fecha de expiración del token JWT.
     * @throws io.jsonwebtoken.MalformedJwtException Si el token JWT está mal formado o no puede ser analizado correctamente.
     * @throws io.jsonwebtoken.ExpiredJwtException Si el token JWT ha expirado.
     * @throws io.jsonwebtoken.UnsupportedJwtException Si el token JWT tiene un formato o contenido no soportado.
     */
    private Date getExpiration(String token){
        return getClaim(token,Claims::getExpiration);
    }

    /**
     * Verifica si el token JWT ha expirado.
     *
     * @param token El token JWT que se verificará.
     * @return {@code true} si el token JWT ha expirado, {@code false} si no ha expirado.
     * @throws io.jsonwebtoken.MalformedJwtException Si el token JWT está mal formado o no puede ser analizado correctamente.
     * @throws io.jsonwebtoken.ExpiredJwtException Si el token JWT ha expirado.
     * @throws io.jsonwebtoken.UnsupportedJwtException Si el token JWT tiene un formato o contenido no soportado.
     */
    public boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }

//metodos privados------------------------------------------------------------------->
    /**
     * Obtiene todas las reclamaciones (claims) contenidas en un token JWT.
     *
     * @param token El token JWT del que se extraerán las reclamaciones.
     * @return Las reclamaciones (claims) contenidas en el token JWT.
     * @throws io.jsonwebtoken.MalformedJwtException Si el token JWT está mal formado o no puede ser analizado correctamente.
     * @throws io.jsonwebtoken.ExpiredJwtException Si el token JWT ha expirado.
     * @throws io.jsonwebtoken.UnsupportedJwtException Si el token JWT tiene un formato o contenido no soportado.
     */
    private Claims getAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Genera un token JWT para el usuario proporcionado con reclamos adicionales personalizados.
     *
     * @param extractClaims Los reclamos adicionales que se incluirán en el token.
     * @param user El objeto UserDetails que representa al usuario para el cual se generará el token.
     * @return El token JWT generado.
     */
    private String getToken(Map<String, Object> extractClaims, UserDetails user) {
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}

