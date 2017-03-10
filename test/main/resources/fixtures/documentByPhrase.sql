INSERT INTO Document (number, content, status, title, creator_id, created_at, changed_at, verified_at, published_at, editor_id, verifier_id, publisher_id)
VALUES ('1', 'test fancy content content content', 'DRAFT', 'Operacja wyrostka robaczkowego', 1, TIMESTAMP '2017-01-01 10:44:00', TIMESTAMP '2017-01-02 10:44:00', TIMESTAMP '2017-01-03 10:44:00', TIMESTAMP '2017-01-04 10:44:00', 1, 2, 3);

INSERT INTO Document (number, content, status, title, creator_id, created_at, changed_at, verified_at, published_at, editor_id, verifier_id, publisher_id)
VALUES ('2', 'test srontent srontent srontent', 'ARCHIVED', 'Operacja fancy ręki', 2, TIMESTAMP '2017-01-01 10:44:00', TIMESTAMP '2017-01-02 10:44:00', TIMESTAMP '2017-01-03 10:44:00', TIMESTAMP '2017-01-04 10:44:00', 1, 2, 3);

INSERT INTO Document (number, content, status, title, creator_id, created_at, changed_at, verified_at, published_at, editor_id, verifier_id, publisher_id)
VALUES ('fancy', 'test bla bla bla', 'DRAFT', 'Operacja nogi', 2, TIMESTAMP '2017-01-02 10:44:00', TIMESTAMP '2017-01-03 10:44:00', TIMESTAMP '2017-01-04 10:44:00', TIMESTAMP '2017-01-05 10:44:00', 2, 3, 1);

INSERT INTO Document (number, content, status, title, creator_id, created_at, changed_at, verified_at, published_at, editor_id, verifier_id, publisher_id)
VALUES ('4', 'test asd asd asd', 'DRAFT', 'Sprzątanie sali', 2, TIMESTAMP '2017-01-05 10:44:00', TIMESTAMP '2017-01-06 10:44:00', TIMESTAMP '2017-01-07 10:44:00', TIMESTAMP '2017-01-08 10:44:00', 3, 2, 1);