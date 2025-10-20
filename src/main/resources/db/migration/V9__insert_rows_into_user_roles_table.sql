INSERT INTO dts_user_roles (user_id, role_id)
VALUES
((SELECT id from dts_users where username='admin'), 1),
((SELECT id from dts_users where username='dispute_agent_1'), 2),
((SELECT id from dts_users where username='dispute_manager_1'), 3);
