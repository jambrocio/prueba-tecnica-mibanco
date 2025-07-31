# Prueba Tecnica Spring Boot Webflux

## Documentacion

http://localhost:8099/swagger-ui-custom.html

## Configuracion Bd (application.properties)

spring.r2dbc.url=r2dbc:postgresql://localhost:5441/mibanco<br>
spring.r2dbc.username=postgres<br>
spring.r2dbc.password=xxxxxxxx

## Ejecucion script bd(data.sql y schema.sql)

- **schema.sql**, creacion de tablas
- **data.sql**, registro de datos de prueba

## Sonarqube

http://localhost:9000/

**Comando ejecucion**

mvn clean verify sonar:sonar -Dsonar.projectKey=prueba-tecnica-mibanco -Dsonar.projectName='prueba-tecnica-mibanco' -Dsonar.host.url=http://localhost:9000 -Dsonar.token=##############>TOKEN
