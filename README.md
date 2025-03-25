# E-Shop Project
Project contains of 5 modules:
## Application
Contains application configuration, runnable app and IT tests.
## Domain
Contains domain models with corresponding methods to manipulate with them.
## Service
Service module applies changes to domain objects and communicate with ports (database and rest in our case)
## Rest
Contains openapi definition, logic to generate models and controllers from openapi and implements them.
## H2
H2 database module, contains entity definitions, JPA repositories and liquibase migrations.

## Run with
```./gradlew bootRun```
