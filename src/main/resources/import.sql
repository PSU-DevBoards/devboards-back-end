INSERT INTO role (name)
VALUES ('Developer'),
       ('Scrum Master');

INSERT INTO permission(key)
VALUES ('work_item(type=="Task"):create'),
       ('work_item(type=="Task"):update'),
       ('work_item(type=="Task"):delete'),
       ('work_item(type=="Feature"):create'),
       ('work_item(type=="Feature"):update'),
       ('work_item(type=="Feature"):delete'),
       ('work_item(type=="Story"):create'),
       ('work_item(type=="Story"):update'),
       ('work_item(type=="Story"):delete'),
       ('work_item:view');

INSERT INTO role_permissions (role_id, permissions_id)
VALUES
       (SELECT id from role WHERE name = 'Developer',
        SELECT id from permission WHERE key = 'work_item(type=="Task"):create'),
       (SELECT id from role WHERE name = 'Developer',
        SELECT id from permission WHERE key = 'work_item(type=="Task"):update'),
       (SELECT id from role WHERE name = 'Developer',
           SELECT id from permission WHERE key = 'work_item(type=="Task"):delete'),
       (SELECT id from role WHERE name = 'Developer',
           SELECT id from permission WHERE key = 'work_item:view');