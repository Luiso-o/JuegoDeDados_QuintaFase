# JuegoDeDados_QuintaFase
ApiRest Crud, Juego de dados, persistiendo base de datos relacionales (Mysql) y no relacionales (mongoDB)
@Gradle @Workbench @MongoDB @Swuagger @Security @JWT

El juego de dados se juega con dos dados. En caso de que el resultado de la suma de ambos dados sea menor o igual a 7, la partida será ganada, si no es perdida. Un jugador/a puede ver un listado de todas las tiradas que ha realizado y el porcentaje de éxito.

Para poder jugar al juego y realizar una tirada, un usuario debe registrarse con un nombre no repetido. Al crearse, se le asigna un identificador numérico único y una fecha de registro. Si el usuario así lo desea, puedes no añadir ningún nombre y se llamará “ANÓNIMO”. Puede haber más de un jugador “ANÓNIMO”.
Cada jugador puede ver un listado de todas las tiradas que ha hecho, con el valor de cada dado y si se ha ganado o no la partida. Además, puede saber su porcentaje de éxito por todas las tiradas que ha realizado.

No se puede eliminar una partida en concreto, pero sí se puede eliminar todo el listado de tiradas por un jugador/a.

El software debe permitir listar a todos los jugadores/as que hay en el sistema, el porcentaje de éxito de cada jugador/a y el porcentaje de éxito medio de todos los jugadores/as en el sistema.

El software debe respetar los principales patrones de diseño.

NOTAS

Tienes que tener en cuenta los siguientes detalles de construcción:

URL's:
-POST: /players: crea un jugador/a.

-PUT /players: modifica el nombre del jugador/a.

-POST /players/{id}/games/ : un jugador/a específico realiza un tirón de los dados.

-DELETE /players/{id}/games: elimina las tiradas del jugador/a.

-GET /players/: devuelve el listado de todos los jugadores/as del sistema con su porcentaje medio de éxitos.

-GET /players/{id}/games: devuelve el listado de jugadas por un jugador/a.

-GET /players/ranking: devuelve el ranking medio de todos los jugadores/as del sistema. Es decir, el porcentaje medio de logros.

-GET /players/ranking/loser: devuelve al jugador/a con peor porcentaje de éxito.

-GET /players/ranking/winner: devuelve al jugador con peor porcentaje de éxito.

- Fase 5
Diseña y modifica el proyecto diversificando la persistencia para que utilice dos esquemas de base de datos a la vez, MySQL y Mongo.

JUEGO DE DADOS
API-![Swagger-Autenticacion](https://github.com/Luiso-o/JuegoDeDados_QuintaFase/assets/128043647/6f8ca2f9-2412-4a5f-9502-52a5b58789eb)


