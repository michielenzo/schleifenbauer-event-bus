# Schleifenbauer Event Bus

This project collects measurements from different sources, publishes them through an in-memory event bus, and persists them to a database.

## Running Locally

To start the application and its required services locally, run:

```bash
docker compose up --build
```

This command builds the application image and starts the full local environment defined in `docker-compose.yml`.

## Calling the API

After the application has started, the following `curl` commands can be used to call the API:

```bash
curl http://localhost:7000/api/measurements
curl http://localhost:7000/api/measurements?channel=memory_free_bytes
curl http://localhost:7000/api/measurements?channel=humidity
curl http://localhost:7000/api/measurements?channel=temperature
curl http://localhost:7000/api/status
```

## Architecture Decisions

- The application follows a layered structure to keep responsibilities separated across controllers, services, persistence, and infrastructure code.
- Database schema migrations are executed through a Flyway container. This keeps database setup reproducible and avoids manual migration steps.
- The collector tasks are singletons, which ensures there is only one scheduled instance of each collector running in the application.
- Scheduled collection uses `ScheduledExecutorService` and `ScheduledFuture` instead of manual loops. This keeps the scheduling logic explicit and makes task state easier to inspect.
- The `InMemoryEventBus` uses thread-safe collections such as `ConcurrentHashMap` so publishing and subscribing remain safe when multiple threads interact with the bus.
- Database configuration is provided through environment variables and injected via the application configuration. This keeps credentials out of the code and makes local and deployment configuration easier to change.
- A single measurements endpoint is used for both unfiltered and channel-filtered queries. This keeps the API surface small while still supporting the main retrieval use cases. Both variants apply the same result limit to prevent unbounded responses when the application has been running for a longer period of time.

## Further Improvements

If I had more time, I would explore the following improvements:

- Add persistence queueing for measurement events, combined with retry behavior for failed persistence, so measurements are not lost when the database is temporarily unavailable and can be stored once the connection is restored.
- Add more possible states to the CollectorTask class. e.g PAUSED, NOT_YET_STARTED.
- Consider splitting the combined measurements endpoint into separate endpoints if filtered and unfiltered queries ever need different limits or different behavior.
- Add more integration tests to cover more scenario's. Maybe tests which also spins up a test database in a docker container to fully end-to-end test the application.
- Log selected handled exceptions, such as failed requests to Open-Meteo, to a file so diagnosing production issues is easier.
