INSERT INTO dts_user_roles (user_id, role_id)
VALUES
((SELECT id from dts_users where username='user1'), 1),
((SELECT id from dts_users where username='user2'), 2),
((SELECT id from dts_users where username='user3'), 3),
((SELECT id from dts_users where username='user4'), 1),
((SELECT id from dts_users where username='user5'), 2),
((SELECT id from dts_users where username='user6'), 3);
