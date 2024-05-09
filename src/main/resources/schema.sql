CREATE TABLE IF NOT EXISTS bank (
                                    bank_id INTEGER PRIMARY KEY,
                                    bank_name TEXT NOT NULL,
                                    total_transaction_fee_amount REAL DEFAULT 0.0,
                                    total_transfer_amount REAL DEFAULT 0.0,
                                    transaction_flat_fee_amount REAL,
                                    transaction_percent_fee_value REAL
);

CREATE TABLE IF NOT EXISTS accounts (
                                        account_id INTEGER PRIMARY KEY,
                                        user_name TEXT,
                                        balance REAL,
                                        bank_id INTEGER,
                                        FOREIGN KEY (bank_id) REFERENCES bank(bank_id)
);

CREATE TABLE IF NOT EXISTS transactions (
                                            transaction_id INTEGER PRIMARY KEY,
                                            amount REAL,
                                            originating_account_id INTEGER,
                                            resulting_account_id INTEGER,
                                            transaction_reason TEXT,
                                            transaction_type TEXT
);
