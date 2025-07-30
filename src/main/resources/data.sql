--PASSWORD => 123456

INSERT INTO users(username, password, nombres, apellido_paterno, apellido_materno, email, enabled)
SELECT *
FROM (
    VALUES
	('FMALPARTIDA', '$2y$10$y8N5bp3tLu.CqtxngGWro.Z2Lz.xn1o1c/M4IKZDGzJPsd/WobFPy', 'FRANCISCO', 'MALPARTIDA', 'RENGIFO', 'FMALPARTIDA@GMAIL.COM', B'1'),
	('MESCALANTE', '$2y$10$y8N5bp3tLu.CqtxngGWro.Z2Lz.xn1o1c/M4IKZDGzJPsd/WobFPy', 'MARIA', 'ESCALANTE', 'RODRIGUEZ', 'MESCALANTE@GMAIL.COM', B'1'),
	('JRODRIGUEZ', '$2y$10$y8N5bp3tLu.CqtxngGWro.Z2Lz.xn1o1c/M4IKZDGzJPsd/WobFPy', 'JOSE ANTONIO', 'RODRIGUEZ', 'ALCAZAR', 'JRODRIGUEZ@GMAIL.COM', B'0'),
	('MARZAPALO', '$2y$10$y8N5bp3tLu.CqtxngGWro.Z2Lz.xn1o1c/M4IKZDGzJPsd/WobFPy', 'MARLINDA', 'ARZAPALO', 'QUISPE', 'MARZAPALO@GMAIL.COM', B'1'),
	('ILEANDRES', '$2y$10$y8N5bp3tLu.CqtxngGWro.Z2Lz.xn1o1c/M4IKZDGzJPsd/WobFPy', 'IVETH', 'LEANDRES', 'OSORIO', 'ILEANDRES@GMAIL.COM', B'1')
) AS t(username, password, nombres, apellido_paterno, apellido_materno, email, enabled)
WHERE NOT EXISTS (SELECT 1 FROM public.users);

INSERT INTO roles(name)
SELECT *
FROM (
    VALUES
	('ADMIN'),
	('USER')
) AS t(name)
WHERE NOT EXISTS (SELECT 1 FROM public.roles);

INSERT INTO users_roles(user_id, role_id)
SELECT *
FROM (
    VALUES
	(1, 1),
	(1, 2),
	(2, 1),
	(2, 2),
	(3, 2),
	(4, 2),
	(5, 2)
) AS t(user_id, role_id)
WHERE NOT EXISTS (SELECT 1 FROM public.users_roles);

INSERT INTO usage_type(name)
SELECT *
FROM (
    VALUES
	('PERSONAL'),
	('TRABAJO'),
	('CARGA')
) AS t(name)
WHERE NOT EXISTS (SELECT 1 FROM public.usage_type);