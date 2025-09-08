-- V13__insert_rows_into_savings_account_transactions.sql
-- 50 dummy transactions (5 per savings account)

INSERT INTO dts_savings_account_transactions
(savings_account_id, debit_card_id, amount, transaction_type, transaction_mode) VALUES
-- Account 1
(1, 1, 5000.00, 'credit', 'ATM'),
(1, 1, 2000.00, 'debit',  'POS'),
(1, 1, 15000.00, 'credit', 'ATM'),
(1, 1, 1000.00, 'debit',  'POS'),
(1, 1, 7000.00, 'debit',  'ATM'),

-- Account 2
(2, 2, 3000.00, 'credit', 'POS'),
(2, 2, 12000.00, 'debit', 'ATM'),
(2, 2, 2500.00, 'debit',  'POS'),
(2, 2, 18000.00, 'credit','ATM'),
(2, 2, 6000.00, 'credit', 'POS'),

-- Account 3
(3, 3, 2200.00, 'debit',  'POS'),
(3, 3, 15000.00, 'credit','ATM'),
(3, 3, 9000.00, 'credit', 'POS'),
(3, 3, 4000.00, 'debit',  'ATM'),
(3, 3, 2000.00, 'debit',  'POS'),

-- Account 4
(4, 4, 8000.00, 'credit','ATM'),
(4, 4, 3500.00, 'debit', 'POS'),
(4, 4, 1000.00, 'debit', 'ATM'),
(4, 4, 6500.00, 'credit','POS'),
(4, 4, 2500.00, 'debit', 'ATM'),

-- Account 5
(5, 5, 7000.00, 'credit','ATM'),
(5, 5, 2000.00, 'debit', 'POS'),
(5, 5, 9500.00, 'credit','POS'),
(5, 5, 1500.00, 'debit', 'ATM'),
(5, 5, 4500.00, 'debit', 'POS'),

-- Account 6
(6, 6, 12500.00, 'credit','ATM'),
(6, 6, 3000.00, 'debit', 'POS'),
(6, 6, 10000.00, 'credit','POS'),
(6, 6, 2500.00, 'debit', 'ATM'),
(6, 6, 4000.00, 'debit', 'POS'),

-- Account 7
(7, 7, 5000.00, 'credit','POS'),
(7, 7, 22000.00, 'credit','ATM'),
(7, 7, 1200.00, 'debit', 'POS'),
(7, 7, 8000.00, 'debit', 'ATM'),
(7, 7, 3000.00, 'credit','POS'),

-- Account 8
(8, 8, 1400.00, 'debit', 'POS'),
(8, 8, 7500.00, 'credit','ATM'),
(8, 8, 900.00,  'debit', 'POS'),
(8, 8, 5000.00, 'credit','ATM'),
(8, 8, 2200.00, 'debit', 'POS'),

-- Account 9
(9, 9, 3000.00, 'credit','POS'),
(9, 9, 18000.00, 'credit','ATM'),
(9, 9, 4500.00, 'debit', 'POS'),
(9, 9, 12000.00, 'credit','ATM'),
(9, 9, 2000.00, 'debit', 'POS'),

-- Account 10
(10, 10, 5000.00, 'debit', 'POS'),
(10, 10, 15000.00, 'credit','ATM'),
(10, 10, 2200.00, 'debit', 'POS'),
(10, 10, 9000.00, 'credit','ATM'),
(10, 10, 3500.00, 'debit', 'POS');
