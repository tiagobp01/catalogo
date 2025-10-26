-- Inserção de categorias iniciais
INSERT INTO categoria (nm_catego_cat, ds_catego_cat, ds_icone_cat) VALUES ('Livros', 'Livros físicos de diversos gêneros', 'fas fa-book');
INSERT INTO categoria (nm_catego_cat, ds_catego_cat, ds_icone_cat) VALUES ('Filmes', 'Filmes de diversos gêneros', 'fas fa-film');
INSERT INTO categoria (nm_catego_cat, ds_catego_cat, ds_icone_cat) VALUES ('Jogos', 'Jogos de diversos gêneros', 'fas fa-gamepad');
INSERT INTO categoria (nm_catego_cat, ds_catego_cat, ds_icone_cat) VALUES ('Sérias', 'Séries em geral', 'fas fa-tv');
INSERT INTO categoria (nm_catego_cat, ds_catego_cat, ds_icone_cat) VALUES ('Álbum', 'Albuns musicais em geral', 'fas fa-compact-disc');
INSERT INTO categoria (nm_catego_cat, ds_catego_cat, ds_icone_cat) VALUES ('Quadrinho', 'Quadrinhos raros', 'fas fa-book-open');
INSERT INTO categoria (nm_catego_cat, ds_catego_cat, ds_icone_cat) VALUES ('Documentário', 'Documentários de diversos gêneros', 'fas fa-film');
INSERT INTO categoria (nm_catego_cat, ds_catego_cat, ds_icone_cat) VALUES ('Curta-metragem', 'Curta-metragens de diversos gêneros', 'fas fa-film');

-- Autores para Livros
INSERT INTO autor (nm_autor_aut, ds_autor_aut) VALUES
('J.K. Rowling', 'Autora da série Harry Potter'),
('George Orwell', 'Autor de 1984 e Revolução dos Bichos'),
('J.R.R. Tolkien', 'Autor de O Senhor dos Anéis e O Hobbit'),
('Stephen King', 'Mestre do terror e ficção'),
('Agatha Christie', 'Rainha do romance policial');

-- Diretores para Filmes
INSERT INTO autor (nm_autor_aut, ds_autor_aut) VALUES
('Steven Spielberg', 'Diretor de Jurassic Park, ET, Tubarão'),
('Christopher Nolan', 'Diretor de A Origem, Batman, Interestelar'),
('Quentin Tarantino', 'Diretor de Pulp Fiction, Django Livre'),
('Martin Scorsese', 'Diretor de Taxi Driver, Os Infiltrados'),
('James Cameron', 'Diretor de Titanic, Avatar, O Exterminador');

-- Desenvolvedores para Jogos
INSERT INTO autor (nm_autor_aut, ds_autor_aut) VALUES
('Hideo Kojima', 'Criador da série Metal Gear Solid'),
('Shigeru Miyamoto', 'Criador de Mario, Zelda, Donkey Kong'),
('Gabe Newell', 'Co-fundador da Valve (Half-Life, Portal)'),
('Todd Howard', 'Diretor de The Elder Scrolls e Fallout'),
('Hidetaka Miyazaki', 'Diretor de Dark Souls e Bloodborne');

-- Criadores para Séries
INSERT INTO autor (nm_autor_aut, ds_autor_aut) VALUES
('David Benioff', 'Co-criador de Game of Thrones'),
('D.B. Weiss', 'Co-criador de Game of Thrones'),
('Vince Gilligan', 'Criador de Breaking Bad e Better Call Saul'),
('Shonda Rhimes', 'Criadora de Grey''s Anatomy, Scandal'),
('Ryan Murphy', 'Criador de American Horror Story, Glee');

-- Artistas para Álbuns
INSERT INTO autor (nm_autor_aut, ds_autor_aut) VALUES
('The Beatles', 'Banda britânica de rock'),
('Michael Jackson', 'Rei do Pop'),
('Queen', 'Banda britânica de rock'),
('Bob Dylan', 'Cantor e compositor folk'),
('Beyoncé', 'Cantora e compositora de R&B');

-- Autores de Quadrinhos
INSERT INTO autor (nm_autor_aut, ds_autor_aut) VALUES
('Stan Lee', 'Co-criador de Homem-Aranha, X-Men, Vingadores'),
('Jack Kirby', 'Co-criador de Capitão América, Quarteto Fantástico'),
('Alan Moore', 'Autor de Watchmen, V de Vingança'),
('Frank Miller', 'Autor de Batman: O Cavaleiro das Trevas'),
('Neil Gaiman', 'Autor de Sandman');

-- Diretores de Documentários
INSERT INTO autor (nm_autor_aut, ds_autor_aut) VALUES
('Ken Burns', 'Documentarista de A Guerra Civil, Jazz'),
('Werner Herzog', 'Documentarista de O Homem Urso, Encontros no Fim do Mundo'),
('Michael Moore', 'Documentarista de Tiros em Columbine, Fahrenheit 9/11'),
('Louis Theroux', 'Documentarista de diversos temas sociais'),
('David Attenborough', 'Narrador e apresentador de documentários da natureza');

-- Diretores de Curtas-metragens
INSERT INTO autor (nm_autor_aut, ds_autor_aut) VALUES
('Tim Burton', 'Diretor de Vincent, Frankenweenie'),
('Nicolas Winding Refn', 'Diretor de Pusher'),
('Martin McDonagh', 'Diretor de Six Shooter'),
('Andrea Arnold', 'Diretora de Wasp'),
('Lynne Ramsay', 'Diretora de Gasman');

-- Inserir diretores famosos
INSERT INTO diretor (nm_direto_dir, ds_direto_dir) VALUES
('Steven Spielberg', 'Diretor de Jurassic Park, ET, Tubarão'),
('Christopher Nolan', 'Diretor de A Origem, Batman, Interestelar'),
('Quentin Tarantino', 'Diretor de Pulp Fiction, Django Livre'),
('Hayao Miyazaki', 'Diretor de A Viagem de Chihiro, Meu Amigo Totoro'),
('James Cameron', 'Diretor de Titanic, Avatar, O Exterminador'),
('Vince Gilligan', 'Criador de Breaking Bad e Better Call Saul'),
('David Benioff', 'Co-criador de Game of Thrones'),
('Stan Lee', 'Co-criador de Homem-Aranha, X-Men'),
('Shigeru Miyamoto', 'Criador de Mario, Zelda, Donkey Kong'),
('Tim Burton', 'Diretor de Vincent, Frankenweenie, Noiva Cadáver');

-- Inserir perfis apenas se não existirem
INSERT INTO perfil (nm_perfil_per) 
SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM perfil WHERE nm_perfil_per = 'ADMIN');

INSERT INTO perfil (nm_perfil_per) 
SELECT 'USER' WHERE NOT EXISTS (SELECT 1 FROM perfil WHERE nm_perfil_per = 'USER');

-- Inserir usuário admin com senha criptografada (senha: admin123) se não existir
INSERT INTO usuario (id_usuari_usu, nm_usuari_usu, ds_email_usu, ds_senha_usu, fl_ativo_usu)
SELECT RANDOM_UUID(), 'admin', 'admin@admin.com', '$2a$10$EAzAJhzUOV6OZJqbewJ6uuIZAlGe2stir7eJHt8u4Um0fdS5b4RoS', true
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE ds_email_usu = 'admin@admin.com');

-- Associar perfil ADMIN ao usuário admin
INSERT INTO usuario_perfil (id_usuari_usu, id_perfil_per)
SELECT u.id_usuari_usu, p.id_perfil_per
FROM usuario u, perfil p
WHERE u.ds_email_usu = 'admin@admin.com'
AND p.nm_perfil_per = 'ADMIN';

-- Livros
INSERT INTO produto (nm_produt_pro, ds_produt_pro, id_catego_cat, id_autor_aut, id_direto_dir, vl_preco_pro, mm_foto_pro, dt_lancam_pro)
SELECT
    'Harry Potter e a Pedra Filosofal',
    'Primeiro livro da saga do jovem bruxo Harry Potter',
    c.id_catego_cat,
    a.id_autor_aut,
    d.id_direto_dir,
    39.90,
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjMWY0ZjdmIi8+CiAgPHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIyNCIgZmlsbD0id2hpdGUiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGR5PSIuM2VtIj5IYXJyeSBQb3R0ZXI8L3RleHQ+CiAgPHRleHQgeD0iNTAlIiB5PSI2MCUiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0id2hpdGUiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGR5PSIuM2VtIj5QZWRyYSBGaWxvc29mYWw8L3RleHQ+Cjwvc3ZnPg==',
    '1997-06-26 00:00:00'
FROM categoria c, autor a, diretor d
WHERE c.nm_catego_cat = 'Livros'
AND a.nm_autor_aut = 'J.K. Rowling'
AND d.nm_direto_dir = 'Steven Spielberg';

INSERT INTO produto (nm_produt_pro, ds_produt_pro, id_catego_cat, id_autor_aut, id_direto_dir, vl_preco_pro, mm_foto_pro, dt_lancam_pro)
SELECT
    '1984',
    'Distopia clássica sobre controle totalitário e vigilância',
    c.id_catego_cat,
    a.id_autor_aut,
    d.id_direto_dir,
    29.90,
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjMzMzIi8+CiAgPHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIzMCIgZmlsbD0id2hpdGUiIHRleHQtYW5jaG9yPSJtaWRkbGUiPjE5ODQ8L3RleHQ+Cjwvc3ZnPg==',
    '1949-06-08 00:00:00'
FROM categoria c, autor a, diretor d
WHERE c.nm_catego_cat = 'Livros'
AND a.nm_autor_aut = 'George Orwell'
AND d.nm_direto_dir = 'Steven Spielberg';

-- Filmes
INSERT INTO produto (nm_produt_pro, ds_produt_pro, id_catego_cat, id_autor_aut, id_direto_dir, vl_preco_pro, mm_foto_pro, dt_lancam_pro)
SELECT
    'Jurassic Park',
    'Parque temático com dinossauros criados geneticamente',
    c.id_catego_cat,
    a.id_autor_aut,
    d.id_direto_dir,
    24.90,
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjMTY1ZjM0Ii8+CiAgPGNpcmNsZSBjeD0iMTAwIiBjeT0iMTAwIiByPSI0MCIgZmlsbD0iI2ZmZiIvPgogIDxjaXJjbGUgY3g9IjEwMCIgY3k9IjEwMCIgcj0iMzAiIGZpbGw9IiMxNjVmMzQiLz4KICA8dGV4dCB4PSI1MCUiIHk9IjY1JSIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE2IiBmaWxsPSJ3aGl0ZSIgdGV4dC1hbmNob3I9Im1pZGRsZSI+SnVyYXNzaWMgUGFyazwvdGV4dD4KPC9zdmc+',
    '1993-06-11 00:00:00'
FROM categoria c, autor a, diretor d
WHERE c.nm_catego_cat = 'Filmes'
AND a.nm_autor_aut = 'George Orwell'
AND d.nm_direto_dir = 'Steven Spielberg';

INSERT INTO produto (nm_produt_pro, ds_produt_pro, id_catego_cat, id_autor_aut, id_direto_dir, vl_preco_pro, mm_foto_pro, dt_lancam_pro)
SELECT
    'A Origem',
    'Roubo de ideias através do mundo dos sonhos',
    c.id_catego_cat,
    a.id_autor_aut,
    d.id_direto_dir,
    19.90,
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjMDAwIi8+CiAgPGNpcmNsZSBjeD0iMTAwIiBjeT0iODAiIHI9IjMwIiBmaWxsPSIjZmZmIiBvcGFjaXR5PSIwLjMiLz4KICA8Y2lyY2xlIGN4PSIxMDAiIGN5PSIxMDAiIHI9IjIwIiBmaWxsPSIjZmZmIiBvcGFjaXR5PSIwLjYiLz4KICA8dGV4dCB4PSI1MCUiIHk9IjYwJSIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE4IiBmaWxsPSJ3aGl0ZSIgdGV4dC1hbmNob3I9Im1pZGRsZSI+SW5jZXB0aW9uPC90ZXh0Pgo8L3N2Zz4=',
    '2010-07-16 00:00:00'
FROM categoria c, autor a, diretor d
WHERE c.nm_catego_cat = 'Filmes'
AND a.nm_autor_aut = 'Christopher Nolan'
AND d.nm_direto_dir = 'Christopher Nolan';

-- Jogos
INSERT INTO produto (nm_produt_pro, ds_produt_pro, id_catego_cat, id_autor_aut, id_direto_dir, vl_preco_pro, mm_foto_pro, dt_lancam_pro)
SELECT
    'The Legend of Zelda: Breath of the Wild',
    'Aventura em Hyrule em mundo aberto',
    c.id_catego_cat,
    a.id_autor_aut,
    d.id_direto_dir,
    199.90,
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjMDBiNDZmIi8+CiAgPHBhdGggZD0iTTUwLDE1MCBMNzAsMTIwIEwxMzAsMTIwIEwxNTAsMTUwIEwxMDAsMTgwIFoiIGZpbGw9IiNmZmE0MDAiLz4KICA8dGV4dCB4PSI1MCUiIHk9IjgwJSIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE0IiBmaWxsPSJ3aGl0ZSIgdGV4dC1hbmNob3I9Im1pZGRsZSI+WmVsZGE8L3RleHQ+Cjwvc3ZnPg==',
    '2017-03-03 00:00:00'
FROM categoria c, autor a, diretor d
WHERE c.nm_catego_cat = 'Jogos'
AND a.nm_autor_aut = 'Shigeru Miyamoto'
AND d.nm_direto_dir = 'Shigeru Miyamoto';

INSERT INTO produto (nm_produt_pro, ds_produt_pro, id_catego_cat, id_autor_aut, id_direto_dir, vl_preco_pro, mm_foto_pro, dt_lancam_pro)
SELECT
    'Dark Souls III',
    'RPG desafiador com combate estratégico',
    c.id_catego_cat,
    a.id_autor_aut,
    d.id_direto_dir,
    119.90,
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjMjIyIi8+CiAgPGNpcmNsZSBjeD0iMTAwIiBjeT0iMTAwIiByPSI0MCIgZmlsbD0iI2ZmZiIgb3BhY2l0eT0iMC4xIi8+CiAgPHBhdGggZD0iTTgwLDEyMCBMMTIwLDEyMCBMMTAwLDE2MCBaIiBmaWxsPSIjYzBhMDAwIi8+CiAgPHRleHQgeD0iNTAlIiB5PSIyMjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0id2hpdGUiIHRleHQtYW5jaG9yPSJtaWRkbGUiPkRhcmsgU291bHM8L3RleHQ+Cjwvc3ZnPg==',
    '2016-03-24 00:00:00'
FROM categoria c, autor a, diretor d
WHERE c.nm_catego_cat = 'Jogos'
AND a.nm_autor_aut = 'Hidetaka Miyazaki'
AND d.nm_direto_dir = 'Hidetaka Miyazaki';

-- Séries
INSERT INTO produto (nm_produt_pro, ds_produt_pro, id_catego_cat, id_autor_aut, id_direto_dir, vl_preco_pro, mm_foto_pro, dt_lancam_pro)
SELECT
    'Breaking Bad - Temporada 1',
    'Professor de química vira fabricante de drogas',
    c.id_catego_cat,
    a.id_autor_aut,
    d.id_direto_dir,
    79.90,
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjMDA3YzQ2Ii8+CiAgPHJlY3QgeD0iNTAiIHk9IjEwMCIgd2lkdGg9IjEwMCIgaGVpZ2h0PSI1MCIgZmlsbD0iI2ZmZiIvPgogIDx0ZXh0IHg9IjUwJSIgeT0iODAlIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTYiIGZpbGw9IndoaXRlIiB0ZXh0LWFuY2hvcj0ibWlkZGxlIj5CcmVha2luZyBCYWQ8L3RleHQ+Cjwvc3ZnPg==',
    '2008-01-20 00:00:00'
FROM categoria c, autor a, diretor d
WHERE c.nm_catego_cat = 'Sérias'
AND a.nm_autor_aut = 'Vince Gilligan'
AND d.nm_direto_dir = 'Vince Gilligan';

-- Álbuns
INSERT INTO produto (nm_produt_pro, ds_produt_pro, id_catego_cat, id_autor_aut, id_direto_dir, vl_preco_pro, mm_foto_pro, dt_lancam_pro)
SELECT
    'Thriller',
    'Álbum mais vendido da história de Michael Jackson',
    c.id_catego_cat,
    a.id_autor_aut,
    d.id_direto_dir,
    54.90,
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjMDAwIi8+CiAgPGNpcmNsZSBjeD0iMTAwIiBjeT0iMTAwIiByPSI2MCIgZmlsbD0iI2ZmZiIvPgogIDxjaXJjbGUgY3g9IjEwMCIgY3k9IjEwMCIgcj0iNDAiIGZpbGw9IiMwMDAiLz4KICA8dGV4dCB4PSI1MCUiIHk9IjIwMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjE2IiBmaWxsPSJ3aGl0ZSIgdGV4dC1hbmNob3I9Im1pZGRsZSI+VGhyaWxsZXI8L3RleHQ+Cjwvc3ZnPg==',
    '1982-11-30 00:00:00'
FROM categoria c, autor a, diretor d
WHERE c.nm_catego_cat = 'Álbum'
AND a.nm_autor_aut = 'Michael Jackson'
AND d.nm_direto_dir = 'Steven Spielberg';

INSERT INTO produto (nm_produt_pro, ds_produt_pro, id_catego_cat, id_autor_aut, id_direto_dir, vl_preco_pro, mm_foto_pro, dt_lancam_pro)
SELECT
    'Abbey Road',
    'Álbum clássico dos Beatles com icônica capa',
    c.id_catego_cat,
    a.id_autor_aut,
    d.id_direto_dir,
    59.90,
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZmZmIi8+CiAgPHJlY3QgeD0iMCIgeT0iMTMwIiB3aWR0aD0iMjAwIiBoZWlnaHQ9IjEwIiBmaWxsPSIjMDAwIi8+CiAgPHJlY3QgeD0iMCIgeT0iMTUwIiB3aWR0aD0iMjAwIiBoZWlnaHQ9IjEwIiBmaWxsPSIjMDAwIi8+CiAgPHJlY3QgeD0iMCIgeT0iMTcwIiB3aWR0aD0iMjAwIiBoZWlnaHQ9IjEwIiBmaWxsPSIjMDAwIi8+CiAgPHRleHQgeD0iNTAlIiB5PSIyMjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzAwMCIgdGV4dC1hbmNob3I9Im1pZGRsZSI+QWJiZXkgUm9hZDwvdGV4dD4KPC9zdmc+',
    '1969-09-26 00:00:00'
FROM categoria c, autor a, diretor d
WHERE c.nm_catego_cat = 'Álbum'
AND a.nm_autor_aut = 'The Beatles'
AND d.nm_direto_dir = 'Steven Spielberg';

-- Quadrinhos
INSERT INTO produto (nm_produt_pro, ds_produt_pro, id_catego_cat, id_autor_aut, id_direto_dir, vl_preco_pro, mm_foto_pro, dt_lancam_pro)
SELECT
    'Homem-Aranha #1',
    'Primeira aparição do amigão da vizinhança',
    c.id_catego_cat,
    a.id_autor_aut,
    d.id_direto_dir,
    999.90,
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZDUwMDE2Ii8+CiAgPHBhdGggZD0iTTUwLDEwMCBMMTUwLDEwMCBMMTAwLDE4MCBaIiBmaWxsPSIjMDAwIi8+CiAgPHRleHQgeD0iNTAlIiB5PSIyMjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0id2hpdGUiIHRleHQtYW5jaG9yPSJtaWRkbGUiPlNwaWRlci1NYW48L3RleHQ+Cjwvc3ZnPg==',
    '1963-08-10 00:00:00'
FROM categoria c, autor a, diretor d
WHERE c.nm_catego_cat = 'Quadrinho'
AND a.nm_autor_aut = 'Stan Lee'
AND d.nm_direto_dir = 'Stan Lee';

-- Documentários
INSERT INTO produto (nm_produt_pro, ds_produt_pro, id_catego_cat, id_autor_aut, id_direto_dir, vl_preco_pro, mm_foto_pro, dt_lancam_pro)
SELECT
    'Planet Earth II',
    'Documentário sobre vida selvagem mundial',
    c.id_catego_cat,
    a.id_autor_aut,
    d.id_direto_dir,
    89.90,
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjMDA3N2ZmIi8+CiAgPGNpcmNsZSBjeD0iMTAwIiBjeT0iMTAwIiByPSI2MCIgZmlsbD0iIzAwYTMzZiIvPgogIDxjaXJjbGUgY3g9IjgwIiBjeT0iODAiIHI9IjEwIiBmaWxsPSIjZmZmIi8+CiAgPHRleHQgeD0iNTAlIiB5PSIyMjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0id2hpdGUiIHRleHQtYW5jaG9yPSJtaWRkbGUiPlBsYW5ldCBFYXJ0aDwvdGV4dD4KPC9zdmc+',
    '2016-11-06 00:00:00'
FROM categoria c, autor a, diretor d
WHERE c.nm_catego_cat = 'Documentário'
AND a.nm_autor_aut = 'David Attenborough'
AND d.nm_direto_dir = 'David Attenborough';

-- Curtas-metragens
INSERT INTO produto (nm_produt_pro, ds_produt_pro, id_catego_cat, id_autor_aut, id_direto_dir, vl_preco_pro, mm_foto_pro, dt_lancam_pro)
SELECT
    'Vincent',
    'Animação sobre menino que quer ser Vincent Price',
    c.id_catego_cat,
    a.id_autor_aut,
    d.id_direto_dir,
    14.90,
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjMzMzIi8+CiAgPHJlY3QgeD0iNjAiIHk9IjgwIiB3aWR0aD0iODAiIGhlaWdodD0iMTAwIiBmaWxsPSIjNjY2Ii8+CiAgPHRleHQgeD0iNTAlIiB5PSIyMjAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0id2hpdGUiIHRleHQtYW5jaG9yPSJtaWRkbGUiPlZpbmNlbnQ8L3RleHQ+Cjwvc3ZnPg==',
    '1982-10-01 00:00:00'
FROM categoria c, autor a, diretor d
WHERE c.nm_catego_cat = 'Curta-metragem'
AND a.nm_autor_aut = 'Tim Burton'
AND d.nm_direto_dir = 'Tim Burton';

