

INSERT INTO payments.Payment_Method
(id, name, status, creationDate)
VALUES(1, 'APM', 1, '2025-05-05 21:29:49.25');



INSERT INTO payments.Payment_Type
(id, `type`, status, creationDate)
VALUES(1, 'SALE', 1, '2025-05-05 21:30:42.05');


INSERT INTO payments.Provider
(id, providerName, status, creationDate)
VALUES(1, 'PAYPAL', 1, '2025-05-05 21:31:28.08');


INSERT INTO payments.Transaction_Status
(id, name, status, creationDate)
VALUES(1, 'CREATED', 1, '2025-05-05 21:33:39.84');
INSERT INTO payments.Transaction_Status
(id, name, status, creationDate)
VALUES(2, 'INITIATED', 1, '2025-05-05 21:33:39.84');
INSERT INTO payments.Transaction_Status
(id, name, status, creationDate)
VALUES(3, 'PENDING', 1, '2025-05-05 21:33:39.84');
INSERT INTO payments.Transaction_Status
(id, name, status, creationDate)
VALUES(4, 'APPROVED', 1, '2025-05-05 21:33:39.84');
INSERT INTO payments.Transaction_Status
(id, name, status, creationDate)
VALUES(5, 'SUCCESS', 1, '2025-05-05 21:33:39.84');
INSERT INTO payments.Transaction_Status
(id, name, status, creationDate)
VALUES(6, 'FAILED', 1, '2025-05-05 21:33:39.84');



-- ==== Dummy data for testing
INSERT INTO payments.Transaction 
(userId, paymentMethodId, providerId, paymentTypeId, amount, currency, txnStatusId, merchantTransactionReference, txnReference, providerReference, retryCount) 
VALUES 
(101, 1, 1, 1, 1.50, 'USD', 3, 'MTR12345', 'TXN12345', 'PROVREF123', 0);


INSERT INTO payments.Transaction 
(userId, paymentMethodId, providerId, paymentTypeId, amount, currency, txnStatusId, merchantTransactionReference, txnReference, providerReference, retryCount) 
VALUES 
(102, 1, 1, 1, 2.75, 'USD', 4, 'MTR12346', 'TXN12346', 'PROVREF124', 0);


INSERT INTO payments.Transaction 
(userId, paymentMethodId, providerId, paymentTypeId, amount, currency, txnStatusId, merchantTransactionReference, txnReference, providerReference, retryCount) 
VALUES 
(103, 1, 1, 1, 3.00, 'USD', 3, 'MTR12347', 'TXN12347', 'PROVREF125', 0);
