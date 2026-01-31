INSERT INTO ong_tb (id, atualizado_em, criado_em) VALUES (1, NOW(), NOW());

-- Criar estoque padrão para a ONG
-- INSERT INTO estoque_tb (id, nome, criado_em, atualizado_em, ong_id) VALUES (1, 'Estoque Principal', NOW(), NOW(), 1);

-- senha dos usuarios é 1234567, use quando for mandar requisição
INSERT INTO usuario_tb (nome, email, senha, role,atualizado_em, criado_em, ong_id) VALUES ('admin','admin@gmail.com', '$2a$10$X1.UGHrhwjGE5eS2Cezy4e2bZwwl15PA6VUeZkhwBd1RNPasBMia6','ADMIN',NOW(), NOW(),1) ;
INSERT INTO usuario_tb (nome, email, senha, role,atualizado_em, criado_em, ong_id) VALUES ('colab', 'colab@gmail.com', '$2a$10$X1.UGHrhwjGE5eS2Cezy4e2bZwwl15PA6VUeZkhwBd1RNPasBMia6','COLABORADOR',NOW(), NOW(),1 );