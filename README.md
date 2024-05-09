# Java Bank System

Welcome to the Java Bank System! This system is designed to simulate basic banking operations such as creating banks, accounts, performing transactions, and more. Below are the instructions to set up and run the system.

## Setup Instructions

### 1. Clone the Repository

Clone this repository to your local machine using the following command:

git clone https://github.com/stinaosmani/java-bank-system.git 

(Use IntelliJ to run the program easier)

### 2. Install SQLite and Maven (if not already installed)
If you haven't already installed SQLite on your machine, follow the instructions for your operating system:

For Windows: Download the SQLite command-line shell program from the SQLite website: SQLite Download Page

Extract the downloaded file and add the directory containing sqlite3.exe to your system's PATH environment variable.

For macOS: SQLite comes pre-installed on macOS. You can access it from the terminal.

For Linux: Install SQLite using your distribution's package manager. For example, on Ubuntu, you can use:

sudo apt-get install sqlite3

To install Maven, follow these steps:

Download the latest version of Apache Maven from the official website.
Extract the downloaded archive to a directory on your computer.
Add the bin directory of the extracted Maven folder to your system's PATH environment variable.

### 3. Set Up Database
The system uses an SQLite database to store bank and account information.  
You should have the `MyDatabase.db` file available in the project directory. To view the database structure, open the `MyDatabase.db` file with an SQLite database viewer or IDE. You can also click the "Apply" button and then "Okay" to see the tables in the database sector.

### 4. Run the Program
Run the BankSystem class to start the Java Bank System. Locate the `BankSystem` class in the project structure and right-click on it.
Select the option to run `BankSystem`.

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
Tests for the Java Bank System are written using JUnit. To run the tests, locate the `BankSystemTest` class in the project structure and right-click on it. 
Select the option to run the tests.