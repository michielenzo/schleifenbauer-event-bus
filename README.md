# Schleifenbauer Event Bus

## Lokaal Starten

Start de applicatie lokaal met:

```bash
docker compose up --build
```

## Architectuurkeuzes

Flyway container for sql migrations. This makes the workflow for

Layer pattern for single responsibility

The Collector classes are singletons ensuring only one instance running

usage of scheduledfuture instead of a loop

The InMemoryEventBus uses a ConcurrentHashmap to ensure that no concurrency errors will show up when multiple threads perform read/write operations on it.

The database credentials are injected through environment variables which in turn are bound to the DatabaseConfig class with Dependency Injection. This makes it easier to change database credentials because wwe dont have to edit code for this. We simply provide other credentials in the docker compose file.

## Verdere Verbeteringen

If i had more time i would have considered the following improvements / extra features

- Measurement event in memory buffering/queueing to ensure no measurement is lost even if the database connection would fail. In such case the event is not totally lost and it stays in the queue which on connection failure automatically would retry at a certain interval so that when the connection can be established again the queue can push al its stackedup data and it is not lost.
