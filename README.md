# RoadBudget

**RoadBudget** is a Java-based desktop application designed to help users manage car-related expenses with ease. The application supports user registration and login with secure SHA-256 password hashing and salting. Users can register multiple cars, log individual expenses for each vehicle, and view all their transactions in a well-organized dashboard. The dashboard provides filtering by car, displays a detailed expense table including type, amount, date, description, and car name, and shows the total spending. RoadBudget ensures a user-friendly experience with a clean GUI built using Swing and supports date picking via an integrated calendar.

---

## ✨ Features

- Secure user registration/login (SHA-256 with salt)
- Add and manage multiple cars per user
- Log expenses with type, amount, description, and date
- Filter by individual cars or view all
- Track total spending
- User-friendly Java Swing GUI
- Integrated calendar date picker
- Modular structure (GUI, DB, Expense modules)

---

## 🧰 Prerequisites and Dependencies

- **Java 11+**
- **Maven 3.6+**
- **MySQL Connector/J**
- **JCalendar** (for date picker)
- **Swing** (Java GUI)

---

## ⚙️ Installation & Usage

### 1. Clone the Repository

```bash
git clone https://github.com/vrushank2001/RoadBudget.git
cd RoadBudget
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Configure Database Connection

Create a file at:

```
src/main/resources/dbModule/config.properties
```

And add the following content (replace with your MySQL credentials) or follow instructions of `config.properties.sample` in `src/main/java/dbModule`:

```properties
db.url=jdbc:mysql://localhost:3306/
db.username=your_mysql_username
db.password=your_mysql_password
```

### 4. Set Up the Database

Create database named `RoadBudget` in `MySQL` using `CREATE DATABASE ROADBUDGET`.
Run the `DBSetup` class in the `dbModule` package to create required tables:

```bash
mvn exec:java -Dexec.mainClass="dbModule.DBSetup"
```


### 5. Run the Application

```bash
mvn exec:java -Dexec.mainClass="GUI.MainPage"
```

---

## 🔐 Security

Passwords are securely hashed using **SHA-256 with a unique salt** per user. The salt and hash are stored separately in the database to improve security.

---

## 📝 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Vrushank Vaghani**  
GitHub: [@vrushank2001](https://github.com/vrushank2001)  
Email: vrushankv1990@gmail.com
