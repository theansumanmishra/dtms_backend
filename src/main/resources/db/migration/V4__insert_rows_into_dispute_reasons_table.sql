INSERT INTO dts_dispute_reasons (id, reason, reason_description)
VALUES
    (1, 'CASH NOT DISPENSED', 'The customer did not receive the cash from the ATM'),
    (2, 'DUPLICATE TRANSACTION', 'The transaction was processed more than once'),
    (3, 'UNAUTHORIZED TRANSACTION', 'The transaction was not authorized by the customer'),
    (4, 'INCORRECT AMOUNT', 'The amount charged is incorrect'),
    (5, 'SERVICE NOT RECEIVED', 'The customer did not receive the service'),
    (6, 'CHARGED AFTER CANCEL', 'The customer was charged after canceling the transaction'),
    (7, 'POS NOT COMPLETED', 'POS transaction approved but purchase not completed'),
    (8, 'OTHERS', 'Client has reasons other than the listed reasons');

