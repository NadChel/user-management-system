INSERT INTO users(username, password, name, last_name, department, salary, age, email, enabled)
SELECT *
FROM (VALUES ('mickey_m', '$2y$10$i5SJc2KAriHGn7Dz2rRQHuQ3JfBxlzMaPVKP1YdEJCukryDY9NbVC',
              'Mickey', 'Mouse', 'sales', 180000, 95, 'mickey_m@gmail.com', 1),

             ('donald_d', '$2y$10$E6SHpAN0aZGQ43HAO.TPiO27kDKXOGIgDc8xWDJdl2Prn2wzQzz5y',
              'Donald', 'Duck', 'information technology', 190000, 89, 'donald_d@outlook.com', 1),

             ('scrooge_m', '$2a$10$ycVpl2BSD6o19wa3xXtmrOxc8qZjwoIk.e3oZqgQBOE.3NHANAYqa',
              'Scrooge', 'McDuck', 'board of directors', 700000, 76, 'scrooge@gmail.com', 1),

             ('minerva_m', '$2y$10$sLgPImwI1WR5KZ98AFPHHut6MvbDoOdBnDC232oLRfejVsganoVyC',
              'Minerva', 'Mouse', 'sales', 180000, 95, 'minnie_m@outlook.com', 1),

             ('goofus_d', '$2y$10$uRP3KtW/yviK.H2VazCECOEQdZxZUZNqHqy/bL4kZgmaBCdprNeE2',
              'Goofus', 'Dawg', 'information technology', 190000, 91, 'goofy@gmail.com', 1),

             ('daisy_d', '$2a$10$mYfix0eIUMIWeTz6kM5.vuI/1demwzsvnY/mSgp5W6rcQVjc3Eyny',
              'Daisy', 'Duck', 'human resources', 165000, 86, 'daisy@outlook.com', 1)) sample_rows

WHERE NOT EXISTS(SELECT NULL FROM users);

INSERT IGNORE INTO roles(role)
VALUES ('USER'),
       ('ADMIN');

INSERT IGNORE INTO user_role (user_id, username, role_id, role)
SELECT users.id, users.username, roles.id, roles.role
FROM (
         VALUES
             ROW('mickey_m', 'USER'),
             ROW('donald_d', 'USER'),
             ROW('scrooge_m', 'USER'),
             ROW('scrooge_m', 'ADMIN')
     ) AS user_to_role (username, role)
         JOIN users ON users.username = user_to_role.username
         JOIN roles ON roles.role = user_to_role.role;