INSERT INTO dts_savings_accounts (client_id, account_number, balance, is_blocked_for_credit, is_blocked_for_debit)
VALUES
(1, 100000000001, 25000, false, false),
(2, 100000000002, 15000, false, false),
(3, 100000000003, 78000, true,  false),
(4, 100000000004, 5000,  false, true),
(5, 100000000005, 92000, false, false),
(6, 100000000006, 34000, false, false),
(7, 100000000007, 60000, false, true),
(8, 100000000008, 12000, true,  false),
(9, 100000000009, 45000, false, false),
(10, 100000000010, 87000, false, false);