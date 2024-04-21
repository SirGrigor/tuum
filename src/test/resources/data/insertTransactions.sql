INSERT INTO transaction (amount, currency, direction, description, account_id, balance_id, date_created, last_updated)
VALUES (200.00, 'USD', 'IN', 'Initial deposit', 1, 1, now(), now()),
       (100.00, 'CAD', 'OUT', 'Payment', 2, 2, now(), now());
