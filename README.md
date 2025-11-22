# BL Time & Invoice Tracker

Minimal MVP for tracking contractor contracts, hours, burn and invoices.

## Structure
- `backend/`: Spring Boot + SQLite API
- `frontend/`: Angular client

## Backend
1. Install JDK 17 and Maven.
2. From `backend/` run:
   - `mvn clean package`
   - `mvn spring-boot:run`
3. Default credentials (HTTP Basic):
   - admin/password (role ADMIN)
   - viewer/password (role VIEWER)
4. Database stored in `backend/bltracker.db` (SQLite). File uploads saved under `backend/uploads/`.

## Frontend
1. Install Node 18+.
2. From `frontend/` run `npm install` then `npm start` (uses Angular dev server with proxy to `http://localhost:8080`).
3. Login with the backend credentials. Admin can create/update data; viewer has read-only access (enforced server-side).

## Features
- Contractor list with search, remaining hours/budget, burn-out date.
- Contractor detail with time entries, invoices (PDF upload/preview), notes.
- CSV import for time entries (columns: email,date,hours,description).
- Burn-out date based on average weekly hours (last 4 weeks fallback to overall).

## Tests
Run backend unit tests for business rules:
```
cd backend
mvn test
```

## Assumptions
- Minimal UI without heavy styling/material for simplicity.
- Authentication via HTTP Basic headers preserved in local storage token.
