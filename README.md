# Route Calculator

A Spring Boot REST service that calculates land routes between countries using their shared borders.

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
- **Data source:** Country border data bundled from [mledoze/countries](https://raw.githubusercontent.com/mledoze/countries/master/countries.json ) as a classpath resource

### How it works

1. On application startup, the service loads the bundled country dataset from the classpath.
2. An adjacency graph is built from each country's `borders` field (list of neighboring country cca3 codes).
3. When a routing request arrives, **bidirectional BFS** searches simultaneously from origin and destination, meeting in the middle. This reduces the search space compared to standard BFS.
4. If no path exists (e.g., island nations with no land borders), the service returns HTTP 400.
