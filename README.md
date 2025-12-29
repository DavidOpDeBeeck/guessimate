# Guessimate

Guessimate is a real-time collaborative estimation application for agile teams.

Live version: [guessimate.app](https://guessimate.app)

## Run Instructions

### Backend (API)

The backend is a Spring Boot application.

```bash
# Run the application
./gradlew :guessimate-api:bootRun
```

The API will be available at `http://localhost:8080`.

### Frontend (UI)

The frontend is an Angular application.

```bash
cd guessimate-ui

# Install dependencies
npm install

# Start the development server
npm start
```

The UI will be available at `http://localhost:4200`.
