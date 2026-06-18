-- Amplia colunas de texto livre / URL do animal para TEXT.
-- Necessário em bases criadas com varchar(255) (ex.: prod), onde ddl-auto=update
-- não altera o tipo da coluna. Idempotente: em bases já TEXT é no-op.
ALTER TABLE IF EXISTS public.animal_tb ALTER COLUMN imagem_principal_perfil TYPE text;
ALTER TABLE IF EXISTS public.animal_tb ALTER COLUMN comportamento          TYPE text;
ALTER TABLE IF EXISTS public.animal_tb ALTER COLUMN condicao_animal         TYPE text;
