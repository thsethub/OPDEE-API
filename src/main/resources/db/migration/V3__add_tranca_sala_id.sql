ALTER TABLE ambientes ADD COLUMN tranca_sala_id BIGINT NULL;

-- Mapeamento OPDEE <-> trancasDEE
UPDATE ambientes SET tranca_sala_id = 7  WHERE nome = 'Banheiro Térreo - Masculino';
UPDATE ambientes SET tranca_sala_id = 4  WHERE nome = 'Banheiro 1° andar - Masculino';
UPDATE ambientes SET tranca_sala_id = 5  WHERE nome = 'Banheiro 1° andar - Feminino';
UPDATE ambientes SET tranca_sala_id = 8  WHERE nome = 'Copa Alunos';
UPDATE ambientes SET tranca_sala_id = 9  WHERE nome = 'CAPIBARIBE';
UPDATE ambientes SET tranca_sala_id = 10 WHERE nome = 'Copa Professores';
UPDATE ambientes SET tranca_sala_id = 6  WHERE nome = 'Gepae';
UPDATE ambientes SET tranca_sala_id = 1  WHERE nome = 'Laboratório de Circuitos';
UPDATE ambientes SET tranca_sala_id = 2  WHERE nome = 'Laboratório CLP 1';
UPDATE ambientes SET tranca_sala_id = 3  WHERE nome = 'Porta Principal';
