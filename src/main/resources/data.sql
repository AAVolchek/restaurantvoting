INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO USER_ROLES (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO restaurant (name, location)
VALUES ('Coqfighter Finsbury Park', '75 Beak Str.' ),
       ('Piazza Italiana', 'str. Mozart 89'),
       ('ZIMA Russian Restaurant', 'str. Georgy 1a');

INSERT INTO menu (name,  restaurant_id, registered_date)
VALUES ('Menu Coqfighter Finsbury Park', 1, now()),
       ('LUNCH SET MENU', 2, now() - interval 1 DAY);

INSERT INTO dish (name, price, restaurant_id)
VALUES ('Honey Ginger Buffalo', '10', 1),
       ('Fries', '3', 1),
       ('Raspberry Lemonade', '4', 1),
       ('ZUPPA CREMOSA DI CAVOLFIORE ARROSTO', '8', 2),
       ('LASAGNA VEGETARIANA', '6', 2),
       ('TIRAMISU CLASSICO CON GELATO AL CAFFÈ', '2', 2),
       ('Green tea', '1', 2),
       ('Pelmeni Sibirskiye', '12', 3),
       ('Borsh Krasnodarsky', '9', 3),
       ('black tea', '2', 3);

INSERT INTO menu_dishes(menu_id, dish_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 7);

INSERT INTO vote(user_id, registered_date, restaurant_id)
values (2, now(), 1)


