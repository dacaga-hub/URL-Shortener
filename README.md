# URL Shortener

Acorta URLs largas a un código corto y redirige de vuelta al visitar ese código.
Proyecto de aprendizaje de Spring Boot con foco en TDD y decisiones de diseño explícitas.

## Stack
- Java 21
- Spring Boot 4.1 (Web, Data JPA, Validation)
- H2 (base de datos en memoria)
- Maven, JUnit 5

## Ejecutar
```bash
./mvnw spring-boot:run    # arranca en http://localhost:8080
./mvnw test               # ejecuta los tests
```

## Decisiones de diseño
- **Código corto vía Base62 desde el id**: Se usa Base62 sobre la ID secuencial por ser determinista, libre de colisiones y rápida de calcular. Una estrategia aleatoria requeriría comprobar colisiones en la base de datos a cada intento. Migraría a un código aleatorio si la secuencialidad permitiera adivinar URLs ajenas recorriendo los códigos (problema de enumeración / privacidad). Escenarios de generación de ids en sistemas distribuidos quedan fuera del alcance de este proyecto.
- **GenerationType.IDENTITY**: Permite delegar la autogeneración de claves a la BD de forma sencilla, ideal para prototipos sin inserciones masivas, aunque deshabilita el batch insert en JPA.

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
Suite de 12 tests en tres niveles:
- **Unitarios** — lógica de codificación Base62 (sin Spring).
- **Servicio** — con repositorio y encoder simulados (Mockito).
- **Capa web** — endpoints con MockMvc (creación, redirección, validación).