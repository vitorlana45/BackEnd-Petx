
INSERT INTO ong_tb (id, atualizado_em, criado_em) VALUES (1, NOW(), NOW());
INSERT INTO estoque_tb (ong_id, criado_em, atualizado_em) VALUES (1, NOW(), NOW());

INSERT INTO animal_tb (status,maturidade,atualizado_em,criado_em,id_ong, comportamento,especie,nome,origem,porte,raca,sexo,chip_id) VALUES ('DOENTE','IDOSO',NOW(),NOW(),1,'DOCIL','CACHORRO','FRIDS','RESGATE','PEQUENO','Poodle','FEMEA','q');
INSERT INTO animal_tb (status,maturidade,atualizado_em,criado_em,id_ong, comportamento,especie,nome,origem,porte,raca,sexo,chip_id) VALUES ('SAUDAVEL','FILHOTE',NOW(),NOW(),1,'DOCIL','CACHORRO','REX','ABANDONO','MEDIO','LABRADOR','MACHO','w');
INSERT INTO animal_tb (status,maturidade,atualizado_em,criado_em,id_ong, comportamento,especie,nome,origem,porte,raca,sexo, chip_id) VALUES ('ADOTADO','ADULTO',NOW(),NOW(),1,'DOCIL','CACHORRO','SPEED','ABANDONO','GRANDE','LABRADOR','MACHO', 't');
INSERT INTO animal_tb (status,maturidade,atualizado_em,criado_em,id_ong, comportamento,especie,nome,origem,porte,raca,sexo,chip_id) VALUES ('FALECIDO','IDOSO',NOW(),NOW(),1,'DOCIL','CACHORRO','JORFE','ABANDONO','MEDIO','LABRADOR','MACHO','r');
INSERT INTO animal_tb (status,maturidade,atualizado_em,criado_em,id_ong, comportamento,especie,nome,origem,porte,raca,sexo,chip_id) VALUES ('SAUDAVEL','IDOSO',NOW(),NOW(),1,'AGRESSIVO','GATO','PARK','GUARDA_MUNICIPAL','PEQUENO','indefinido','FEMEA','y');

INSERT INTO animal_doencas (animal_id, doenca) VALUES (1, 'CINOMOSE');
INSERT INTO animal_doencas (animal_id, doenca) VALUES (1, 'RAIVA');
INSERT INTO animal_doencas (animal_id, doenca) VALUES (2, 'CINOMOSE');
INSERT INTO animal_doencas (animal_id, doenca) VALUES (2, 'RAIVA');


-- senha dos usuarios é 1234567, use quando for mandar requisição
INSERT INTO usuario_tb (email, senha, role,atualizado_em, criado_em, ong_id) VALUES ('admin@gmail.com', '$2a$10$X1.UGHrhwjGE5eS2Cezy4e2bZwwl15PA6VUeZkhwBd1RNPasBMia6','ADMIN',NOW(), NOW(),1) ;
INSERT INTO usuario_tb (email, senha, role,atualizado_em, criado_em, ong_id) VALUES ('colab@gmail.com', '$2a$10$X1.UGHrhwjGE5eS2Cezy4e2bZwwl15PA6VUeZkhwBd1RNPasBMia6','COLABORADOR',NOW(), NOW(),1 );

-- INSERT INTO tutor_tb (nome, telefone, criado_em, atualizado_em,cep,cidade,bairro,id_ong, rua,bairro, cidade,cpf) VALUES ('John', 77777777, NOW(), NOW(), '12345678', 'São Paulo', 'Vila Mariana', 1, 'Rua 1', 'abcwe','São Paulo','123456789');
-- INSERT INTO tutor_tb (nome, telefone, criado_em, atualizado_em,cep,cidade,bairro,id_ong, rua,bairro, cidade,cpf) VALUES ('Jorge', 88888888, NOW(), NOW(), '12345678', 'São Paulo', 'Vila Mariana', 1, 'Rua 2', 'abcwe','São Paulo','987654321');
-- INSERT INTO tutor_tb (nome, telefone, criado_em, atualizado_em,cep,cidade,bairro,id_ong, rua, bairro, cidade,cpf) VALUES ('Steve', 999999999, NOW(), NOW(), '12345678', 'São Paulo', 'Vila Mariana', 1, 'Rua 3', 'abcwe','São Paulo','123456789');
--
-- INSERT INTO tutor_tb (nome, telefone, criado_em, atualizado_em,cep,cidade,bairro,id_ong, rua,cpf, idade) VALUES ('Steve', 999999999, NOW(), NOW(), '12345678', 'São Paulo', 'Vila Mariana', 1, 'Rua 3', '064.123.090-71',98);


-- INSERT INTO  estoque_tb (atualizado_em, criado_em, id_ong, nome, quantidade,especie) VALUES (NOW(), NOW(), 1, 'Ração', '40kg',0);
-- INSERT INTO  estoque_tb (atualizado_em, criado_em, id_ong, nome, quantidade,especie) VALUES (NOW(), NOW(), 1, 'Ração', '30g', 0);
-- INSERT INTO  estoque_tb (atualizado_em, criado_em, id_ong, nome, quantidade,especie) VALUES (NOW(), NOW(), 1, 'Ração', '45kg',1);
-- INSERT INTO  estoque_tb (atualizado_em, criado_em, id_ong, nome, quantidade,especie) VALUES (NOW(), NOW(), 1, 'Ração', '12kg',1);
-- INSERT INTO  estoque_tb (atualizado_em, criado_em, id_ong, nome, quantidade,especie) VALUES (NOW(), NOW(), 1, 'Ração', '76g', 1);
-- INSERT INTO estoque_tb (atualizado_em, criado_em, id_ong, nome, quantidade, especie) VALUES (NOW(), NOW(), 1, 'Ração', '2kg', 0);
-- INSERT INTO estoque_tb (atualizado_em, criado_em, id_ong, nome , quantidade,especie) VALUES (NOW(), NOW(), 1, 'Ração', '22kg', 0);

-- INSERT INTO tutor_animal_tb (animal_id, tutor_id) VALUES (1, 1);
-- INSERT INTO tutor_animal_tb (animal_id, tutor_id) VALUES (1, 2);
-- INSERT INTO tutor_animal_tb (animal_id, tutor_id) VALUES (3, 1);
--
-- insert into  animal_tutores (animal_id, tutor_id) values (1, 1);
-- insert into  animal_tutores (animal_id, tutor_id) values (1, 2);
-- insert into  animal_tutores (animal_id, tutor_id) values (1, 3);
