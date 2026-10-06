# Eventual Consistency Lab

A practical mini-project demonstrating **Eventual Consistency** between two independent services using **Spring Boot, Oracle, Debezium, and Apache Kafka**.

The project simulates a distributed system where the **User Service** owns user data, while another service maintains its own local representation of that data.

Instead of sharing the same database or performing synchronous API calls between services, changes are propagated asynchronously through **Change Data Capture (CDC)** and Kafka.

---

## Architecture

```text
                    ┌─────────────────────┐
                    │    User Service     │
                    │     Spring Boot     │
                    │                     │
                    │       Oracle       │
                    │                     │
                    │  USERS              │
                    │  USERS_ADDRESS      │
                    └──────────┬──────────┘
                               │
                               │ Database Changes
                               ▼
                    ┌─────────────────────┐
                    │      Debezium       │
                    │    Oracle CDC       │
                    └──────────┬──────────┘
                               │
                               │ Change Events
                               ▼
                    ┌─────────────────────┐
                    │       Kafka         │
                    │                     │
                    │ users-db.USERS...  │
                    └──────────┬──────────┘
                               │
                               │ Async Events
                               ▼
                    ┌─────────────────────┐
                    │     Post Service    │
                    │     Spring Boot     │
                    │                     │
                    │       Oracle       │
                    │                     │
                    │  Local Users       │
                    │  Local Addresses   │
                    └─────────────────────┘
```

---

## What This Project Demonstrates

The main goal of this project is to demonstrate how two services can maintain **eventual consistency** without directly sharing a database.

The User Service is the source of truth for user information.

The Post Service maintains its own local copy of the required user data.

When data changes in the User Service database, Debezium captures those changes and publishes them to Kafka. The Post Service then consumes the events asynchronously.

---

## Example Scenario

A user can be created through the User Service using:

```http
POST /users
Content-Type: application/json
```

```json
{
  "username": "john",
  "mobile": "09120000000",
  "address": "Tehran, Iran",
  "postalCode": "1234567890"
}
```

This operation results in changes to multiple database tables.

```text
USERS
--------------------------------
ID | USERNAME | MOBILE
1  | john     | 09120000000
```

```text
USERS_ADDRESS
-----------------------------------------------
ID | USER_ID | ADDRESS       | POSTAL_CODE
1  | 1       | Tehran, Iran  | 1234567890
```

The database changes are then captured by Debezium.

```text
User Service
     │
     ▼
  Oracle
     │
     │ CDC
     ▼
 Debezium
     │
     ▼
  Kafka
     │
     ▼
Post Service
```

---

## Eventual Consistency

The important characteristic of this architecture is that the services are **not immediately consistent**.

For example:

```text
T0

User Service DB
    User: ✓
    Address: ✓

Post Service DB
    User: ✗
    Address: ✗
```

At this point, the User Service has successfully created the data, but the Post Service has not received or processed the events yet.

Then:

```text
T1

Oracle
   ↓
Debezium
   ↓
Kafka
```

The database changes are captured and published as events.

Next:

```text
T2

Kafka
   ↓
Post Service

User Event
   ↓
User is created locally
```

The Post Service may now have:

```text
Post Service DB
    User: ✓
    Address: ✗
```

Then the address event is processed:

```text
T3

Kafka
   ↓
Post Service

User Address Event
   ↓
Address is created locally
```

Eventually:

```text
T4

User Service DB
    User: ✓
    Address: ✓

Post Service DB
    User: ✓
    Address: ✓
```

The two services have now reached **eventual consistency**.

---

## Event Flow

The complete flow looks like this:

```text
                    CREATE USER
                         │
                         ▼
                ┌─────────────────┐
                │   User Service  │
                └────────┬────────┘
                         │
                         ▼
                     ┌───────┐
                     │Oracle │
                     └───┬───┘
                         │
               ┌─────────┴─────────┐
               │                   │
               ▼                   ▼
          USERS change       ADDRESS change
               │                   │
               └─────────┬─────────┘
                         │
                         ▼
                    Debezium
                         │
                         ▼
                       Kafka
                    /          \
                   /            \
                  ▼              ▼
             User Event     Address Event
                  │              │
                  └──────┬───────┘
                         ▼
                  Post Service
                         │
                         ▼
                      Oracle
```

---

## Kafka Topics

Debezium creates separate Kafka topics for the captured tables.

For example:

```text
users-db.USERS_APP.USERS
users-db.USERS_APP.USERS_ADDRESS
```

The User Service database changes are therefore transformed into Kafka events without requiring the application to explicitly publish those events.

---

## Why Debezium?

Instead of modifying the application code to publish an event every time a user is created or updated, Debezium monitors the Oracle database and captures changes directly from the database transaction log.

This allows the database to remain the source of truth while changes are propagated asynchronously.

```text
Application
    │
    ▼
Oracle
    │
    │ CDC
    ▼
Debezium
    │
    ▼
Kafka
```

This approach is especially useful when working with legacy systems where modifying every existing write operation may be difficult or risky.

---

## Database-per-Service

Each service owns its own database.

```text
┌──────────────────┐
│   User Service   │
│                  │
│     Oracle       │
│                  │
│  Users           │
│  Addresses       │
└──────────────────┘


┌──────────────────┐
│   Post Service   │
│                  │
│     Oracle       │
│                  │
│  Local Users     │
│  Addresses       │
└──────────────────┘
```

The services do not directly query each other's databases.

Instead, data is replicated asynchronously through events.

This provides better service isolation and reduces tight coupling between services.

---

## Parent / Child Event Dependency

One of the interesting problems demonstrated by this project is the relationship between the User and Address data.

Conceptually:

```text
User
 │
 └── Address
```

The address depends on the user already existing.

Therefore, if the following events are processed in the wrong order:

```text
UserAddressCreated
UserCreated
```

the Post Service may receive the address event before the user exists locally.

For example:

```text
Address Event
      │
      ▼
Post Service
      │
      ▼
Find User
      │
      ✗
User not found
```

This demonstrates an important problem in distributed systems:

> **Eventual consistency does not automatically guarantee that dependent events will always be processed in the required order.**

This introduces concepts such as:

* Event ordering
* Kafka partitions
* Retry
* Backoff
* Dead Letter Topics
* Idempotency
* Consumer recovery
* Parent/child event dependencies

---

## Failure Scenario

A consumer can fail while processing an event.

For example:

```text
Kafka
   │
   ▼
Post Service
   │
   ▼
User Address Event
   │
   ▼
User does not exist
   │
   ✗
Processing failed
```

Instead of losing the event, the consumer can use mechanisms such as:

```text
Retry
  ↓
Retry with Backoff
  ↓
Retry again
  ↓
DLT
```

This project provides a playground for experimenting with these failure-handling strategies.

---

## Idempotency

In an event-driven architecture, consumers should be designed with the possibility that an event can be delivered or processed more than once.

For example:

```text
UserCreated
     │
     ▼
Post Service
     │
     ▼
Database
```

If the same event is received again, the consumer should avoid creating duplicate data.

Possible approaches include:

* Unique constraints
* Event IDs
* Processed-event tables
* Upsert operations
* Idempotent business operations

---

## Technologies

* Java
* Spring Boot
* Spring Kafka
* Apache Kafka
* Debezium
* Oracle Database
* Docker
* Docker Compose
* Maven

---

## Local Environment

The project uses Docker Compose to run the infrastructure locally.

Typical components include:

```text
┌──────────────────────────┐
│       Infrastructure     │
│                          │
│  Oracle DB #1            │
│  Oracle DB #2            │
│  Kafka                    │
│  Debezium Kafka Connect  │
│  Kafka UI                 │
└──────────────────────────┘
```

The Spring Boot services run separately.

---

## Running the Project

Start the infrastructure:

```bash
docker compose up -d
```

Verify the containers:

```bash
docker ps
```

Start the User Service:

```bash
./mvnw spring-boot:run
```

Start the Post Service:

```bash
./mvnw spring-boot:run
```

Then create a user through the User Service:

```http
POST /users
```

```json
{
  "username": "john",
  "mobile": "09120000000",
  "address": "Tehran, Iran",
  "postalCode": "1234567890"
}
```

Observe the complete flow:

```text
User Service
     ↓
Oracle
     ↓
Debezium
     ↓
Kafka
     ↓
Post Service
     ↓
Oracle
```

---

## What Happens Behind the Scenes?

### Step 1 — User creation

The client sends:

```json
{
  "username": "john",
  "mobile": "09120000000",
  "address": "Tehran, Iran",
  "postalCode": "1234567890"
}
```

to the User Service.

---

### Step 2 — Database transaction

The User Service stores the data in Oracle.

```text
USERS
    ↓
USER_ADDRESS
```

---

### Step 3 — CDC

Debezium detects the database changes.

```text
Oracle
   ↓
Database Transaction Log
   ↓
Debezium
```

---

### Step 4 — Kafka

Debezium publishes change events:

```text
users-db.USERS_APP.USERS
users-db.USERS_APP.USERS_ADDRESS
```

---

### Step 5 — Consumer

The Post Service consumes the events:

```text
Kafka
   ↓
Kafka Consumer
   ↓
Deserialize Event
   ↓
Map Event
   ↓
Persist Entity
```

---

### Step 6 — Consistency

After all relevant events are successfully processed:

```text
User Service DB
       │
       │
       ▼
Post Service DB

     CONSISTENT
```

---

## Key Co

