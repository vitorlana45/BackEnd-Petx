
INSERT INTO ong_tb (id, atualizado_em, criado_em) VALUES (1, NOW(), NOW());
INSERT INTO estoque_tb (ong_id, criado_em, atualizado_em) VALUES (1, NOW(), NOW());


-- senha dos usuarios é 1234567, use quando for mandar requisição
INSERT INTO usuario_tb (nome, email, senha, role,atualizado_em, criado_em, ong_id) VALUES ('admin','admin@gmail.com', '$2a$10$X1.UGHrhwjGE5eS2Cezy4e2bZwwl15PA6VUeZkhwBd1RNPasBMia6','ADMIN',NOW(), NOW(),1) ;
INSERT INTO usuario_tb (nome, email, senha, role,atualizado_em, criado_em, ong_id) VALUES ('colab', 'colab@gmail.com', '$2a$10$X1.UGHrhwjGE5eS2Cezy4e2bZwwl15PA6VUeZkhwBd1RNPasBMia6','COLABORADOR',NOW(), NOW(),1 );
