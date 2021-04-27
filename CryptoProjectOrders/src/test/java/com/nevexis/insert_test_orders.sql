INSERT INTO orders (id, source_amount, exchange_rate, order_type, remaining_amount, status, 
currency_pair_crypto_code, currency_pair_fiat_code, trader_id, timestamp) 
VALUES 
('1', '10', '58000', 'BUY', '10', 'OPEN', 'BTC', 'USD', '1', current_timestamp()),
('2', '200', '57000', 'BUY', '200', 'OPEN', 'BTC', 'USD', '1', current_timestamp()),
('3', '10', '59000', 'SELL', '10', 'OPEN', 'BTC', 'USD', '2', current_timestamp()),
('4', '200', '60000', 'SELL', '200', 'OPEN', 'BTC', 'USD', '2', current_timestamp()),
('5', '30', '1.2', 'BUY', '30', 'OPEN', 'ADA', 'USD', '1', current_timestamp()),
('6', '40', '1.1', 'BUY', '40', 'OPEN', 'ADA', 'USD', '1', current_timestamp()),
('7', '70', '1.3', 'SELL', '70', 'OPEN', 'ADA', 'USD', '2', current_timestamp()),
('8', '80', '1.4', 'SELL', '80', 'OPEN', 'ADA', 'USD', '2', current_timestamp());