[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=codekopf_pwc-router&metric=bugs)](https://sonarcloud.io/summary/new_code?id=codekopf_pwc-router) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=codekopf_pwc-router&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=codekopf_pwc-router) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=codekopf_pwc-router&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=codekopf_pwc-router) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=codekopf_pwc-router&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=codekopf_pwc-router) [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=codekopf_pwc-router&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=codekopf_pwc-router) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=codekopf_pwc-router&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=codekopf_pwc-router) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=codekopf_pwc-router&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=codekopf_pwc-router)

# Route Calculator

A Spring Boot REST service that calculates land routes between countries using their shared borders.

## Table of Contents

- [Developer Notes](#developer-notes)
    - [Design Decisions](#design-decisions)
    - [Technical Notes](#technical-notes)
    - [Testing](#testing)
- [Prerequisites](#prerequisites)
- [Build](#build)
- [Run](#run)
- [API Usage](#api-usage)
- [Docker](#docker)
- [Run Tests](#run-tests)
- [Technical Details](#technical-details)

### Developer Notes

### Design Decisions

**Country Data Loading**

Countries are loaded from a file rather than a database. This is based on the assumption that countries are finite and relatively stable—borders rarely change, and new countries are not created overnight. Given this stability, a file-based approach is simpler and sufficient for this use case.

**Pathfinding Algorithm**

Bidirectional BFS is used instead of standard BFS or DFS, as it is generally faster for finding the shortest path between two nodes.

**Input Validation**

Lowercase CCA3 codes are considered invalid. The task description does not explicitly state that only uppercase 3-letter codes are valid, so any input that does not exactly match a CCA3 code is treated as invalid.

**Same Origin and Destination**

When the origin and destination are the same country, the service treats this as a valid route and returns a single-element array. This edge case was not explicitly defined in the task description, so I opted for the more permissive behavior rather than throwing an error.

### Technical Notes

**Java Version**

Java 25 LTS is used, as it is already 2026.

**Lombok**

I typically use Lombok to reduce Java boilerplate. However, the current version of Lombok does not support Java 25, so it has been removed entirely from this project.

### Testing

**Postman Collection**

A Postman collection is included in the project for manual API testing.

## Prerequisites

- **Java 25** (JDK 25 or later)
- **Maven 3.9+** (included via Maven Wrapper)
- **Docker** (optional, for containerized deployment)

## Build

```bash
./mvnw clean package
```

To skip tests during build:

```bash
./mvnw clean package -DskipTests
```

## Run

After building, start the application with:

```bash
./mvnw spring-boot:run
```

Or run the packaged JAR directly:

```bash
java -jar target/router-0.0.1-SNAPSHOT.jar
```

The application starts on port **8080** by default.

## API Usage

### Calculate a land route

```
GET /routing/{origin}/{destination}
```

Countries are identified by their **cca3** code (ISO 3166-1 alpha-3).

#### Example: Czech Republic to Italy

**Request:**

```
GET /routing/CZE/ITA
```

**Response (200 OK):**

```json
{
  "route": ["CZE", "AUT", "ITA"]
}
```

#### Error: No land crossing possible

**Request:**

```
GET /routing/CZE/JPN
```

**Response (400 Bad Request):**

```json
{
  "error": "No land route found from CZE to JPN"
}
```

#### Error: Invalid country code

**Request:**

```
GET /routing/XXX/ITA
```

**Response (400 Bad Request):**

```json
{
  "error": "Country code not found: XXX"
}
```

## Docker

Build the image:

```bash
docker build -t route-calculator .
```

Run the container:

```bash
docker run -p 8080:8080 route-calculator
```

## Run Tests

```bash
./mvnw test
```

## Technical Details

- **Framework:** Spring Boot 4.0.2
- **Language:** Java 25
- **Algorithm:** Bidirectional BFS for efficient shortest-path calculation between countries
- **Data source:** Country border data bundled from [mledoze/countries](https://raw.githubusercontent.com/mledoze/countries/master/countries.json) as a classpath resource

### How it works

1. On application startup, the service loads the bundled country dataset from the classpath.
2. An adjacency graph is built from each country's `borders` field (list of neighboring country cca3 codes).
3. When a routing request arrives, **bidirectional BFS** searches simultaneously from origin and destination, meeting in the middle. This reduces the search space compared to standard BFS.
4. If no path exists (e.g., island nations with no land borders), the service returns HTTP 400.
