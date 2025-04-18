# 🚗 RoadBudget

**RoadBudget** is a Java-based desktop application designed to help users manage car-related expenses with ease. The application supports user registration and login with secure SHA-256 password hashing and salting. Users can register multiple cars, log individual expenses for each vehicle, and view all their transactions in a well-organized dashboard. The dashboard provides filtering by car, displays a detailed expense table including type, amount, date, description, and car name, and shows the total spending. RoadBudget ensures a user-friendly experience with a clean GUI built using Swing and supports date picking via an integrated calendar.

---

## ✨ Features

- 🔐 Secure user registration and login with SHA-256 + salt
- 🚘 Add and manage multiple cars
- 💸 Log expenses with type, amount, date, and description
- 📅 Integrated calendar (JDateChooser) for expense dates
- 📊 View all expenses in a table with filtering by car
- 💰 Grand total display of all expenses
- 📁 All data persisted in MySQL

---

## 📂 Project Structure

```
RoadBudget/
├── dbModule/               # Database setup and operations
├── ui/                    # Swing-based user interface
├── models/                 # Data models (e.g., ExpenseRecord, Car)
├── utils/                  # Security (password hashing, salting)
├── config.properties       # DB config (username, password, etc.)
└── MainPage.java               # App entry point
```

---

## 🛠️ Setup Instructions

1. **Clone the repo:**
   ```bash
   git clone https://github.com/vrushank2001/RoadBudget.git
   cd RoadBudget
   ```

2. **Configure the database:**
   - Create a MySQL database.
   - Update `config.properties` with your DB credentials.

3. **Run the setup:**
   ```bash
   Create database named `RoadBudget` in `MySQL` using `CREATE DATABASE ROADBUDGET`.
   Run `DBSetup.java` to create all required tables.
   ```

4. **Launch the app:**
   Run `MainPage.java` to start the RoadBudget application.

---

## 📦 Dependencies

- Java 8+
- MySQL JDBC Driver
- [JDateChooser](https://toedter.com/jcalendar/) for calendar picker

---

## 🔒 Security

- Passwords are stored using SHA-256 hashing with random salts.
- Salts are stored alongside hashed passwords in the database.

---

## 📜 License

This project is licensed under the MIT License.

---

## 👨‍💻 Author

Developed by Vrushank Vaghani.  
Feel free to contribute or suggest improvements!
