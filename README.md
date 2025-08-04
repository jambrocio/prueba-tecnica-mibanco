# Prueba Tecnica Spring Boot Webflux

## Documentacion

http://localhost:8099/swagger-ui-custom.html

## Configuracion de variables de entorno (.env)

POSTGRES_DB=mibanco<br>
POSTGRES_USER=postgres<br>
POSTGRES_PASSWORD=xxxxxxxxxxxx<br>
POSTGRES_HOST=localhost<br>
POSTGRES_PORT=5441<br>

**Nota:** el cambio de dichos valores servira para la configuracion de bd y docker compose

## Ejecucion script bd(data.sql y schema.sql)

Se ejecuta de manera automatica al inicializar la aplicacion.

- **schema.sql**, creacion de tablas
- **data.sql**, registro de datos de prueba

## Sonarqube

http://localhost:9000/

**Comando ejecucion**

```
mvn clean verify sonar:sonar -Dsonar.projectKey=prueba-tecnica-mibanco -Dsonar.projectName='prueba-tecnica-mibanco' -Dsonar.host.url=http://localhost:9000 -Dsonar.token=##############>TOKEN
```
