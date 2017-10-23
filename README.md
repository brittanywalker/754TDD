# 754 Requirements Engineering
Assignment 3 - Test Driven Development

## Create the database user

```
CREATE ROLE "springbootuser" LOGIN
  PASSWORD 'springbootpass'
  SUPERUSER INHERIT CREATEDB NOCREATEROLE NOREPLICATION;
```

## Create the database

```
CREATE DATABASE "assignment754"
  WITH ENCODING='UTF8'
       OWNER="springbootuser"
       CONNECTION LIMIT=-1;
```

## Build the project

```
mvn clean install
```

## Run the migrations

```
mvn liquibase:update -P local
```

## Running the API

Start the service by running the following command:

```
java -jar target/spring-boot-rest-api-seed-1.0-SNAPSHOT.jar
```