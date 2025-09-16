CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE dts_users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE dts_clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE dts_savings_accounts (
    id SERIAL PRIMARY KEY,
    account_number BIGINT UNIQUE NOT NULL,
    client_id INT REFERENCES dts_clients(id) NOT NULL,
    balance DOUBLE PRECISION NOT NULL,
    account_creation_date DATE NOT NULL DEFAULT CURRENT_DATE,
    is_blocked_for_credit BOOLEAN DEFAULT FALSE,
    is_blocked_for_debit BOOLEAN DEFAULT FALSE
);

CREATE TABLE dts_debit_cards (
    id SERIAL PRIMARY KEY,
    savings_account_id INT REFERENCES dts_savings_accounts(id) NOT NULL,
    card_type VARCHAR(100) NOT NULL,
    card_no BIGINT UNIQUE NOT NULL,
    debit_card_issued_date DATE NOT NULL DEFAULT CURRENT_DATE,
    expiry_month INT NOT NULL,
    expiry_year INT NOT NULL,
    cvv_no INT NOT NULL,
    pin VARCHAR(255) NOT NULL,
    is_blocked BOOLEAN DEFAULT FALSE
);

CREATE TABLE dts_savings_account_transactions (
    id SERIAL PRIMARY KEY,
    savings_account_id INT REFERENCES dts_savings_accounts(id) NOT NULL,
    debit_card_id INT REFERENCES dts_debit_cards(id) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_rail VARCHAR(100) NOT NULL,
    payment_rail_instance_id UUID DEFAULT gen_random_uuid() NOT NULL,
    merchant_name VARCHAR(100) NOT NULL,
    merchant_category VARCHAR(100) NOT NULL,
    transaction_type VARCHAR(100) NOT NULL,
    transaction_mode VARCHAR(100) NOT NULL,
    disputable BOOLEAN DEFAULT FALSE
);

CREATE TABLE dts_disputes (
    id SERIAL PRIMARY KEY,
    transaction_id INT REFERENCES dts_savings_account_transactions(id) NOT NULL,
    source VARCHAR(255) NOT NULL,
    reason VARCHAR(255),
    description VARCHAR(255),
    created_date DATE NOT NULL,
    status INT NOT NULL,
    sub_status INT NOT NULL
);

CREATE TABLE dts_dispute_transactions (
    id SERIAL PRIMARY KEY,
    dispute_id INT REFERENCES dts_disputes(id) NOT NULL,
    savings_account_transaction_id INT REFERENCES dts_savings_account_transactions(id) NOT NULL,
    dispute_entry_date DATE NOT NULL DEFAULT CURRENT_DATE,
    disputed_amount NUMERIC(12,2) NOT NULL,
    status INT ,
    sub_status INT
);

CREATE TABLE dts_configurable_lists (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500)
);

CREATE TABLE dts_configurable_list_details (
    id SERIAL PRIMARY KEY,
    configurable_list_id INT REFERENCES dts_configurable_lists(id) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500)
);

CREATE TABLE dts_dispute_reasons(
    id SERIAL PRIMARY KEY,
    reason VARCHAR(255) NOT NULL,
    reason_description VARCHAR(500)
);

CREATE TABLE dts_roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500)
);

CREATE TABLE dts_permissions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500)
);

CREATE TABLE dts_user_roles (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES dts_users(id) NOT NULL,
    role_id INT REFERENCES dts_roles(id) NOT NULL
);

CREATE TABLE dts_role_permissions (
    id SERIAL PRIMARY KEY,
    role_id INT REFERENCES dts_roles(id) NOT NULL,
    permission_id INT REFERENCES dts_permissions(id) NOT NULL
);