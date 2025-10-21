# Shrink.me - Minimal URL Shortener

A tiny demo URL shortener built with Spring Boot, JPA, Flyway and a small static frontend.

This repository contains a small, self-contained demo app that shortens URLs, redirects short keys to the original URL, and exposes a lightweight stats API. It's intended as an example app you can run locally, inspect, and extend.

## Key features

- Shorten URLs via a JSON API or the built-in static GUI.
- Redirect short keys to target URLs.
- Track simple click counts (stored in DB row).
- Short keys are generated with a small base62 generator and collisions are checked.
- Automatic expiry: new mappings expire at expire at predifend Time and a scheduled task cleans expired rows.
- Persistence with JPA + MySQL (Flyway manages the schema migration).

## Tech stack

- Java 17
- Spring Boot 3.5.x (starter parent)
- Spring Web, Spring Data JPA, Spring Validation
- Flyway for migrations
- MySQL (or compatible) as the runtime DB

## API

Base prefix for API endpoints: `/api`

1) Shorten a URL (POST)

POST /api/shorten

Request JSON body:

```json
{ "url": "https://example.com/some/path" }
```

Response (200 OK):

```json
{
  "Url": "https://example.com/some/path",
  "shortUrl": "http://localhost:8080/Ab12XyZ",
  "CreatedAt": "2025-10-21T10:00:00"
}
```

Notes:
- The request is validated to start with `http://` or `https://` by `RequestDto`.
- The short key generator produces 7-character base62 strings; collisions are checked and regenerated if necessary.

2) Redirect (GET)

GET /{shortKey}

Example: GET /Ab12XyZ

Behavior: returns a 302 redirect to the original long URL. The service increments the click counter when resolving.

3) Get stats for a key (GET)

GET /api/stats/{shortKey}

Response (200 OK):

```json
{
  "Url": "https://example.com/some/path",
  "shortUrl": "http://localhost:8080/Ab12XyZ",
  "clicks": 3
}
```

4) UI

The repository includes a minimal frontend at `/index.html` which talks to the API endpoints above. Use it to shorten URLs and query stats.


## Expiry and cleanup

For demo purposes each new `UrlMapping` is given a short expiry in the constructor. The `ExpiryCleanupService` runs every 7 minutes and deletes Expried rows.

This behavior is deliberately short for demo/testing. In a production system you'd choose a more sensible expiry policy, or make expiry optional/configurable.