TRUNCATE TABLE transaction CASCADE;
TRUNCATE TABLE balance CASCADE;
TRUNCATE TABLE account CASCADE;

-- Resetting auto-increment counters for PostgreSQL
ALTER SEQUENCE account_id_seq RESTART WITH 1;
ALTER SEQUENCE balance_id_seq RESTART WITH 1;
ALTER SEQUENCE transaction_id_seq RESTART WITH 1;
