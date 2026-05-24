# Backend Developer Challenge

## Introductie

Bouw een kleine backend service die data verzamelt van een externe bron, intern distribueert, opslaat in een database en beschikbaar stelt via een REST API.

Het gaat ons niet om een perfect afgerond product. We willen zien hoe jij een backend probleem aanpakt, hoe je code structureert en hoe je je keuzes motiveert.


**Inleveren via:** 
een GitHub of GitLab repository.

---

## Tech stack

Gebruik de volgende stack — dit zijn de tools waarmee we dagelijks werken:

| Tool | Versie |
|---|---|
| Java | 21+ |
| Google Guice | 6.x (dependency injection) |
| Javalin | 4.x (REST API) |
| MariaDB | via Docker |
| HikariCP | 5.x (connection pooling) |
| JUnit 5 + Mockito | testen |
| Maven | build tool |

Kotlin naast Java is toegestaan maar niet vereist.

---

## Wat je bouwt

### 1. Event Bus

Een eenvoudige in-memory pub-sub implementatie:

- Subscribers registreren op een kanaal (bijv. `"weather/temperature"`)
- Publishers sturen een bericht naar een kanaal
- Alle subscribers op dat kanaal worden gesynchroniseerd genotificeerd
- De implementatie moet thread-safe zijn

### 2. Twee collectors

Collectors verzamelen data en publiceren die naar de event bus.

**`WeatherCollector`**
- Pollt elke 30 seconden een HTTP endpoint
- Publiceert temperatuur en luchtvochtigheid naar de event bus
- Als de HTTP call mislukt: log de fout en probeer het bij de volgende poll opnieuw

Je mag zelf bepalen welk endpoint je gebruikt. Opties:
- Een gratis publieke weer-API (bijv. [open-meteo.com](https://open-meteo.com/) — geen API key nodig)
- Een lokale stub die je zelf opzet

**`SystemCollector`**
- Leest elke 10 seconden het beschikbaar geheugen via `Runtime.getRuntime().freeMemory()`
- Publiceert dit naar de event bus

Beide collectors worden via Guice geïnjecteerd en gestart vanuit een centrale `Application` klasse.

### 3. Persistentie

Een `StorageService` subscribet op de event bus en slaat ontvangen berichten op in MariaDB.

Tabel `measurements`:

```sql
CREATE TABLE measurements (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    channel   VARCHAR(255) NOT NULL,
    value     DOUBLE       NOT NULL,
    timestamp DATETIME     NOT NULL
);
```

- Gebruik directe JDBC (geen ORM/Hibernate)
- Gebruik HikariCP voor connection pooling

### 4. REST API

Drie endpoints via Javalin:

```
GET /api/measurements
    → Geeft de laatste 50 metingen terug (alle kanalen)

GET /api/measurements?channel=weather/temperature
    → Gefilterd op kanaal

GET /api/status
    → Geeft terug of de collectors actief zijn
```

Responses zijn JSON.

---

## Tests

Schrijf minimaal:

- **Een unit tests** voor de event bus (bijv. publiceren bereikt subscriber, meerdere subscribers werken correct)
- **Een unit test** voor een collector (mock de HTTP call of de clock, verifieer dat het juiste kanaal gepubliceerd wordt)
- **Een integratietest** voor de REST API (start Javalin op, doe een HTTP call, verifieer de response)

---

## Inleveren

- Een Git repository met een nette commit geschiedenis
- Een `docker-compose.yaml` zodat de database met één commando te starten is
- Een `README.md` met:
  - Hoe het project te starten
  - Een korte uitleg van je architectuurkeuzes
  - Wat je anders zou doen met meer tijd

---

## Waar we op letten

We kijken niet of alles perfect is. We kijken hoe je denkt en of je keuzes bewust maakt.

| Onderdeel | Wat we bekijken |
|---|---|
| **Structuur** | Is de code opgedeeld in logische lagen? Zijn verantwoordelijkheden duidelijk gescheiden? |
| **Dependency injection** | Wordt Guice op een zinvolle manier gebruikt? |
| **Foutafhandeling** | Wat gebeurt er als de HTTP call of database verbinding faalt? |
| **Tests** | Testen die iets zinvols bewijzen — niet alleen dat de code niet crasht |
| **README** | Kan een collega het project in 5 minuten draaien en begrijpt hij jouw keuzes? |

---

## Vragen

Als iets onduidelijk is in de opdracht, stel dan gewoon een vraag. Dat is geen minpunt, onduidelijkheden ophelderen is onderdeel van het werk.
