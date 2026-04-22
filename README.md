# TransitWise — A Java Based Bus Ticket Booking System

> **OOP Semester 2 Project** | Java + JavaFX

**Areeba Saeed** — Semester 2 OOP Project, 2025

TransitWise is a desktop bus ticket booking application built with JavaFX. It lets passengers register, search buses across major Pakistani cities, book seats using a digital wallet, and view a full booking history.

Route data is sourced and fetched from the **Daewoo Express** booking app.

---

## Features

- **User Registration & Login** — Users register with CNIC & phone both; log in using either
- **Bus Search** — search 30+ routes across 11 cities by origin and destination
- **Seat Booking** — choose a seat number with live availability checking
- **Early-Booking Discounts** — up to 30% off for booking 7+ days in advance
- **Digital Wallet** — top up balance and pay for tickets in-app
- **Ticket History** — searchable log of all past bookings
- **Boarding Pass** — styled popup with passenger details, route, seat, and decorative barcode
- **Persistent Storage** — user accounts and ticket records are saved opposite to their login details 

---

## Cities Covered

Lahore · Karachi · Islamabad · Rawalpindi · Peshawar · Multan · Faisalabad · Hyderabad · Sialkot · Bahawalpur · Abbottabad

---

## Project Structure

```
TransitWise/
│
├── TransitWiseApp.java      # JavaFX entry point + full GUI
├── User.java                # Passenger model (name, CNIC, wallet, history)
├── Bus.java                 # Bus model with seat map
├── Route.java               # Route details (origin, destination, fare, time)
├── Ticket.java              # Confirmed booking record
│
├── AuthService.java         # User registry (HashMap-backed)
├── Authenticator.java       # Input validation + registration/login process
├── WalletService.java       # Deposit and deduction logic (not linked to any banking service)
├── BookTicket.java          # Booking workflow managing class
├── discountCalculator.java  # Early-booking discount algorithm
│
├── Data.java                # Routes & buses info (30 routes)
├── FileManagement.java      # File persistence
├── TicketGenerator.java     # Plain-text ticket formatter
│
└── data/
    ├── users.txt            # Saved user accounts
    └── tickets.txt          # Saved ticket records
```

---

## OOP Concepts Used

| Concept | Where Applied |
|---|---|
| Encapsulation | All classes have private fields, public getters/setters |
| Abstraction | Service classes hide complex logic behind simple method calls |
| Composition | Ticket has-a User and has-a Bus; Bus has-a Route |
| Single Responsibility | Each class has one clearly defined job |
| Static Utility Classes | `Data` and `discountCalculator` |
| Collections & Generics | `HashMap`, `ArrayList`, `LinkedHashSet` |

---

## Discount Algorithm

| Days Before Departure | Discount |
|---|---|
| 7+ days | 30% |
| 6 days | 25% |
| 5 days | 20% |
| 4 days | 15% |
| 2–3 days | 10% |
| 1 day | 5% |
| Same day | 0% |

---

## How to Run

**Requirements:** JDK 17+ with JavaFX on the classpath.

```bash
# Compile
javac --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml *.java

# Run
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml TransitWiseApp
```
---

## Data Persistence

- On **startup**: users and ticket history are loaded from `data/users.txt` and `data/tickets.txt`
- On **booking**: the new ticket is immediately appended to `tickets.txt`
- On **wallet top-up or window close**: all user records are rewritten to `users.txt`

---

## Current Limitations and Future Enhancements

- The **Print Ticket** button opens the boarding pass popup but does not send to a printer (JavaFX PrinterJob isnt linked to the button)
- **Seat maps reset on restart** — booked seats are not persisted between sessions
---

