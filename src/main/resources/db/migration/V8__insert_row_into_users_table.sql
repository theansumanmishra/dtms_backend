-- Insert admin user (username = admin, password = bcrypt hash of "admin123")
INSERT INTO dts_users (username, password)
VALUES ('admin', '$2a$12$50QTeT4UJbLXtaTyZn7RYeZzfksCZRfBvFv6H2PMqPFA6wtITxNYu')
ON CONFLICT (id) DO NOTHING;

-- Insert role ADMIN
INSERT INTO dts_roles (name)
VALUES ('ADMIN')
ON CONFLICT (id) DO NOTHING;

-- Assign role to user via join table
INSERT INTO dts_user_roles (user_id, role_id)
VALUES (1, 1)
ON CONFLICT DO NOTHING;