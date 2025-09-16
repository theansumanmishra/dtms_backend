INSERT INTO dts_permissions (id, name, description)
VALUES
--user
    (1, 'CREATE_USER', 'Permission to create new user accounts'),
    (2, 'VIEW_USER', 'Permission to view user account details'),
    (3, 'UPDATE_USER', 'Permission to update user account information'),
    (4, 'MANAGE_USER', 'Permission to manage user accounts'),
--client
    (5, 'CREATE_CLIENT', 'Permission to create new client accounts'),
    (6, 'VIEW_CLIENT', 'Permission to view client account details'),
    (7, 'UPDATE_CLIENT', 'Permission to update client account information'),
    (8, 'MANAGE_CLIENT', 'Permission to manage client accounts'),
--dispute
    (9, 'CREATE_DISPUTE', 'Permission to create a new dispute'),
    (10, 'VIEW_DISPUTE', 'Permission to view dispute details'),
    (11, 'REVIEW_DISPUTE', 'Permission to review and update dispute status'),
    (12, 'MANAGE_DISPUTE', 'Permission to manage all disputes');

