INSERT INTO dts_configurable_list_details (id, configurable_list_id, name, description)
VALUES
    (1, 1, 'INITIATED', 'The dispute is created'),
    (2, 1, 'IN_PROGRESS', 'The dispute is open and under review'),
    (3, 1, 'RESOLVED', 'The dispute has been resolved'),
    (4, 2, 'PENDING_REVIEW', 'The dispute is pending for review by the relevant team member'),
    (5, 2, 'UNDER_REVIEW', 'The dispute is under review by the relevant authority'),
    (6, 2, 'ACCEPTED', 'The dispute has been accepted'),
    (7, 2, 'PARTIALLY_ACCEPTED', 'The dispute has been partially accepted'),
    (8, 2, 'REJECTED', 'The dispute has been rejected');
