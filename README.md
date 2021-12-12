# PSU DevBoards Back-End
[![Build](https://img.shields.io/github/workflow/status/PSU-DevBoards/devboards-back-end/Build)](https://github.com/PSU-DevBoards/devboards-back-end/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=PSU-DevBoards_devboards-back-end&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=PSU-DevBoards_devboards-back-end)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=PSU-DevBoards_devboards-back-end&metric=bugs)](https://sonarcloud.io/summary/new_code?id=PSU-DevBoards_devboards-back-end)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=PSU-DevBoards_devboards-back-end&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=PSU-DevBoards_devboards-back-end)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=PSU-DevBoards_devboards-back-end&metric=coverage)](https://sonarcloud.io/summary/new_code?id=PSU-DevBoards_devboards-back-end)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=PSU-DevBoards_devboards-back-end&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=PSU-DevBoards_devboards-back-end)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=PSU-DevBoards_devboards-back-end&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=PSU-DevBoards_devboards-back-end)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=PSU-DevBoards_devboards-back-end&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=PSU-DevBoards_devboards-back-end)

REST back-end API for the PSU Devboards system. 

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Gradle 7.1](https://gradle.org/)

## Environment Variables

The following environment variables are required. Additional optional variables can be found in the application properties files.

| Name                           | Value | Description                                          |
|--------------------------------|-------|------------------------------------------------------|
| SPRING_PROFILES_ACTIVE         | local | Active Spring profile. Use local during development. |
| AUTH0_MANAGEMENT_CLIENT_SECRET |       | Client secret for the Auth0 management API.          | 
| EMAIL_FROM                     |       | Sender email address for outgoing emails.            |

## Running Locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.psu.devboards.dbapi.DbapiApplication` class from your IDE.

Alternatively you can use [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) like so:

```shell
# Unix
./gradlew bootRun

# Windows
gradlew bootRun
```

## Deployment

This application is deployed to [Heroku](https://heroku.com) via continuous delivery. 

Base Url: https://psu-devboards-backend.herokuapp.com/

API Documentation: https://psu-devboards-backend.herokuapp.com/swagger-ui.html