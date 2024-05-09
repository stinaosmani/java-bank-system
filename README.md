# Java Bank System

Welcome to the Java Bank System! This system is designed to simulate basic banking operations such as creating banks, accounts, performing transactions, and more. Below are the instructions to set up and run the system.

## Setup Instructions

### 1. Clone the Repository

Clone this repository to your local machine using the following command:

git clone https://github.com/your-username/javabanksystem.git

###  2. Install SQLite (if not already installed)

If you haven't already installed SQLite on your machine, follow the instructions for your operating system:

For Windows:
Download the SQLite command-line shell program from the SQLite website: SQLite Download Page

Extract the downloaded file and add the directory containing sqlite3.exe to your system's PATH environment variable.

For macOS:
SQLite comes pre-installed on macOS. You can access it from the terminal.

For Linux:
Install SQLite using your distribution's package manager. For example, on Ubuntu, you can use:

sudo apt-get install sqlite3

### 3. Set Up Database
The system uses an SQLite database to store bank and account information. To initialize the database, navigate to the project directory and run the following command:

sqlite3 MyDatabase.db < src/main/resources/schema.sql

### 4. Compile the Java Files
Compile the Java files using a Java compiler. You can use an IDE like IntelliJ IDEA or compile from the command line using javac.

### 5. Run the Program
Run the BankSystem class to start the Java Bank System. You can do this from your IDE or using the following command:

java -classpath . com.example.javabanksystem.BankSystem

Follow the on-screen prompts to interact with the system.

### Usage
Once the program is running, you can use the menu options to perform various banking operations:

Create a bank
Create an account
Perform transactions
Withdraw money
Deposit money
See a list of transactions for any account
Check the account balance for any account
See a list of bank accounts
Check the bank's total transaction fee amount
Check the bank's total transfer amount
Exit

### Running Tests
Tests for the Java Bank System are written using JUnit. To run the tests, execute the BankSystemTest class. You can do this from your IDE or using the following command:

java -classpath . org.junit.runner.JUnitCore com.example.javabanksystem.BankSystemTest