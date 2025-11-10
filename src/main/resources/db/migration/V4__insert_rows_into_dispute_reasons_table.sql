INSERT INTO dts_dispute_reasons (id, reason, reason_description)
VALUES
    --ATM
    (1,'CASH NOT DISPENSED', 'The ATM did not dispense cash, but the amount was deducted'),
    (2,'NETWORK FAILURE','Transaction failed mid-way due to system or network interruption'),
    (3,'MULTIPLE DEBITS','Account was debited multiple times for one withdrawal attempt'),
    (4,'TRANSACTION TIMEOUT','Transaction was shown as failed, but the balance was deducted.'),
    (5,'UNAUTHORIZED TRANSACTION', 'The transaction was not authorized by the customer'),
    --POS
    (6,'TRANSACTION DECLINE','Payment failed at the merchant terminal, yet funds were deducted'),
    (7,'DUPLICATE TRANSACTION','The same transaction amount was charged more than once at the merchant'),
    (8,'CHARGED AFTER CANCEL', 'The customer was charged after canceling the transaction'),
    (9,'POS NOT COMPLETED', 'POS transaction approved but vendor did not receive amount'),
    (10,'OTHERS', 'Client has reasons other than the listed reasons');
