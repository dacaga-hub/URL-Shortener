# URL Shortener

![CI](https://github.com/dacaga-hub/URL-Shortener/actions/workflows/ci.yml/badge.svg)

Acorta URLs largas a un código corto y redirige de vuelta al visitar ese código.
Proyecto de aprendizaje de Spring Boot con foco en TDD y decisiones de diseño explícitas.

## Stack
- Java 21
- Spring Boot 4.1 (Web, Data JPA, Validation)
- PostgreSQL 16, con migraciones de esquema en Flyway
- Maven, JUnit, Testcontainers
- Docker y Docker Compose
- GitHub Actions (test, build y publicación de la imagen en ghcr.io)

## Ejecutar

### Todo en contenedores
Solo requiere Docker:
```bash
docker compose up --build -d    # app en http://localhost:8080, PostgreSQL en el 5432
docker compose logs -f app      # espera a "Started ShortenerApplication"
docker compose down             # para el entorno (añade -v para borrar los datos)
```

### Desarrollo local
La base de datos en contenedor y la app desde Maven:
```bash
docker compose up -d db
./mvnw spring-boot:run          # arranca en http://localhost:8080
```

### Tests
```bash
./mvnw test
```
Los tests de integración levantan un PostgreSQL real con Testcontainers, así que Docker tiene que estar en marcha.

## Decisiones de diseño
- **Código corto vía Base62 desde el id**: Se usa Base62 sobre la ID secuencial por ser determinista, libre de colisiones y rápida de calcular. Una estrategia aleatoria requeriría comprobar colisiones en la base de datos a cada intento. Migraría a un código aleatorio si la secuencialidad permitiera adivinar URLs ajenas recorriendo los códigos (problema de enumeración / privacidad). Escenarios de generación de ids en sistemas distribuidos quedan fuera del alcance de este proyecto.
- **GenerationType.IDENTITY**: Permite delegar la autogeneración de claves a la BD de forma sencilla, ideal para prototipos sin inserciones masivas, aunque deshabilita el batch insert en JPA.
- **`url_short` admite NULL**: El código se calcula a partir del id, que solo existe tras el primer INSERT. La fila se guarda sin código y se actualiza a continuación, por lo que la columna no puede ser `NOT NULL`. La restricción `UNIQUE` sigue garantizando que no haya códigos repetidos. El INSERT y el UPDATE ocurren en una misma transacción: si la codificación falla, no queda ninguna fila sin código.
- **Flyway con `ddl-auto=validate`**: El esquema vive en migraciones SQL versionadas (`src/main/resources/db/migration`), no lo genera Hibernate. Con `validate`, la aplicación se niega a arrancar si las entidades y las tablas no coinciden, en lugar de fallar en la primera petición.
- **Testcontainers en lugar de H2**: Los tests de integración corren contra el mismo motor y la misma versión que producción. Con H2 los tests pasaban contra un dialecto SQL distinto y un esquema autogenerado, sin probar las migraciones reales. El coste es que los tests necesitan Docker y tardan unos segundos más.
- **Imagen multi-stage y usuario sin privilegios**: La imagen final solo contiene el JRE y el jar, no el JDK ni el código fuente, y el proceso no se ejecuta como root.
- **Configuración por entorno**: `application.properties` contiene los valores de desarrollo local y el contenedor los sobrescribe con variables de entorno (`SPRING_DATASOURCE_URL`, etc.). Las credenciales de `docker-compose.yml` son solo para desarrollo.

## Uso

### Crear una URL corta
```bash
curl -X POST http://localhost:8080/api/urls \
  -H "Content-Type: application/json" \
  -d '{"url": "https://www.google.com"}'
```
Respuesta `201 Created`:
```json
{"urlShort": "1"}
```

### Acceder a una URL corta
Visita `http://localhost:8080/{código}` en el navegador, o:
```bash
curl -v http://localhost:8080/1
```
Responde `302 Found` con la cabecera `Location` apuntando a la URL original.

### Validación
Una URL vacía o con formato inválido devuelve `400 Bad Request`:
```json
{"url": "debe ser un URL válido"}
```

## Testing
Suite de 15 tests en cuatro niveles:
- **Unitarios**: lógica de codificación Base62 (sin Spring).
- **Servicio**: con repositorio y encoder simulados (Mockito).
- **Capa web**: endpoints con MockMvc (creación, redirección, validación).
- **Integración**: flujo completo (crear y redirigir, código inexistente, rollback si falla la codificación) contra PostgreSQL real con Testcontainers y el esquema creado por Flyway.

## CI/CD
Cada push y cada pull request ejecutan la suite completa en GitHub Actions. Si los tests pasan en `main`, se construye la imagen Docker y se publica en GitHub Container Registry con dos etiquetas: `latest` y el hash del commit.

La imagen necesita un PostgreSQL. Para ejecutarla contra la base de datos del compose:

```bash
docker compose up -d db
docker run --rm -p 8080:8080 --network shortener_default \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/shortener \
  ghcr.io/dacaga-hub/url-shortener:latest
```