# Expense Tracker

React (frontend) + Spring Boot (backend) + MySQL (database).

```
Expense_tracker_app/          (repository root)
├── backend/    Spring Boot REST API (Maven, Java 17+)
└── frontend/   React 18 app (static files, no build step)
```

## Features
- Add, view, update, delete expenses (amount, category, date, description)
- Categorisation, date-range / category / keyword filtering
- Total expenditure, count, average, top category
- Charts: spending by category (donut) and monthly spending (bar)

## 1. Backend (port 8080)
Requires Java 17+, Maven, MySQL 8 running locally.
```
cd backend
export DB_USER=root
export DB_PASSWORD=your_password     # or edit src/main/resources/application.properties
mvn spring-boot:run
```
The `expense_tracker` database and `expenses` table are created automatically.

## 2. Frontend (port 3000)
Requires Node.js 16+. React is bundled in `frontend/vendor/`, so there are no packages to install.
```
cd frontend
npm start
```
Open http://localhost:3000

- Backend URL is set in `frontend/config.js` (default `http://localhost:8080`).
- If you serve the frontend on another port/host, add it to `app.cors.allowed-origins` in `backend/src/main/resources/application.properties`.

## REST API
| Method | Path | Description |
|--------|------|-------------|
| GET    | `/api/expenses?category=&from=&to=&q=` | List (dates `yyyy-MM-dd`) |
| GET    | `/api/expenses/summary?...` | Total, count, average, by category, by month |
| GET    | `/api/expenses/categories` | Distinct categories |
| GET    | `/api/expenses/{id}` | One expense |
| POST   | `/api/expenses` | Create |
| PUT    | `/api/expenses/{id}` | Update |
| DELETE | `/api/expenses/{id}` | Delete |

Example body: `{"amount": 250.50, "category": "Food", "expenseDate": "2026-10-07", "description": "Lunch"}`

Currency is INR; change `CURRENCY` / `LOCALE` at the top of `frontend/app.js`.
