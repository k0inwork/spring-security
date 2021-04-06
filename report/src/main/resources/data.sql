insert into users(username,password,enabled)
	values
    ('admin','$2a$10$Q/W4AjWIrPPerArhKA5JnO56s1E/KS533MtEjqK8t8/bK/.9T2Aoe',true),
	('user1','$2a$10$Q/W4AjWIrPPerArhKA5JnO56s1E/KS533MtEjqK8t8/bK/.9T2Aoe',true),
	('user2','$2a$10$Q/W4AjWIrPPerArhKA5JnO56s1E/KS533MtEjqK8t8/bK/.9T2Aoe',true),
	('user3','$2a$10$Q/W4AjWIrPPerArhKA5JnO56s1E/KS533MtEjqK8t8/bK/.9T2Aoe',true),
	('user4','$2a$10$Q/W4AjWIrPPerArhKA5JnO56s1E/KS533MtEjqK8t8/bK/.9T2Aoe',true);

insert into authorities(username,authority) 
	values
    ('admin','ROLE_ADMIN'),
    ('admin','ROLE_USER'),
    ('admin','ROLE_OPERATOR'),
    ('user1','ROLE_USER'),
    ('user1','ROLE_OPERATOR'),
    ('user2','ROLE_USER'),
    ('user3','ROLE_USER'),
    ('user4','ROLE_USER')
    ;