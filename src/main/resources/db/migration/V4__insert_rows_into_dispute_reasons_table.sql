INSERT INTO dts_dispute_reasons (id, reason, reason_description)
VALUES
    (1, 'CASH_NOT_DISPENSED', 'The customer did not receive the cash from the ATM'),
    (2, 'DUPLICATE_TRANSACTION', 'The transaction was processed more than once'),
    (3, 'UNAUTHORIZED_TXN', 'The transaction was not authorized by the customer'),
    (4, 'INCORRECT_AMOUNT', 'The amount charged is incorrect'),
    (5, 'SERVICE_NOT_RECEIVED', 'The customer did not receive the service'),
    (6, 'CHARGED_AFTER_CANCEL', 'The customer was charged after canceling the transaction'),
    (7, 'POS_NOT_COMPLETED', 'POS transaction approved but purchase not completed');
