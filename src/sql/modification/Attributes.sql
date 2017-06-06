--Attributes RoleName
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (39,1,'ADMIN',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (39,2,'RECEPTION',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (39,3,'CLIENT',NULL);

--Attributes RoomType
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (28,5,'Luxe',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (29,5,'Content Luxe',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (28,6,'Semi-Luxe',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (29,6,'Content semi-luxe',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (28,7,'Economy',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (29,7,'Content economy',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (28,8,'President',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (29,8,'Content president',NULL);

--Attributes NotificationType
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (40,11,'Resettlement',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (41,11,1); -- для админов
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (40,12,'Renewal',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (41,12,1); -- для админов

--Attributes Rate
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,31,NULL,to_date('02-12-2016','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,31,NULL,to_date('01-03-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,32,NULL,to_date('02-03-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,32,NULL,to_date('01-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,33,NULL,to_date('02-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,33,NULL,to_date('01-09-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,34,NULL,to_date('02-09-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,34,NULL,to_date('01-12-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,47,NULL,to_date('02-12-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,47,NULL,to_date('01-01-2018','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,35,NULL,to_date('02-12-2016','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,35,NULL,to_date('01-03-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,36,NULL,to_date('02-03-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,36,NULL,to_date('01-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,37,NULL,to_date('02-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,37,NULL,to_date('01-09-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,38,NULL,to_date('02-09-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,38,NULL,to_date('01-12-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,48,NULL,to_date('02-12-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,48,NULL,to_date('01-01-2018','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,39,NULL,to_date('02-12-2016','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,39,NULL,to_date('01-03-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,40,NULL,to_date('02-03-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,40,NULL,to_date('01-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,41,NULL,to_date('02-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,41,NULL,to_date('01-09-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,42,NULL,to_date('02-09-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,42,NULL,to_date('01-12-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,49,NULL,to_date('02-12-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,49,NULL,to_date('01-01-2018','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,43,NULL,to_date('02-12-2016','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,43,NULL,to_date('01-03-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,44,NULL,to_date('02-03-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,44,NULL,to_date('01-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,45,NULL,to_date('02-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,45,NULL,to_date('01-09-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,46,NULL,to_date('02-09-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,46,NULL,to_date('01-12-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (30,50,NULL,to_date('02-12-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (31,50,NULL,to_date('01-01-2018','dd-mm-yyyy'));

--Attributes Price
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,55,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,55,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,56,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,56,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,57,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,57,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,58,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,58,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,59,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,59,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,60,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,60,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,61,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,61,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,62,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,62,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,63,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,63,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,64,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,64,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,65,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,65,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,66,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,66,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,67,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,67,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,68,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,68,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,69,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,69,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,70,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,70,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,71,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,71,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,72,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,72,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,73,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,73,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,74,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,74,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,75,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,75,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,76,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,76,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,77,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,77,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,78,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,78,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,79,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,79,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,80,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,80,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,81,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,81,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,82,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,82,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,83,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,83,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,84,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,84,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,85,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,85,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,86,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,86,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,87,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,87,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,88,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,88,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,89,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,89,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,90,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,90,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,91,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,91,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,92,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,92,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,93,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,93,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,94,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,94,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,95,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,95,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,96,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,96,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,97,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,97,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,98,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,98,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,99,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,99,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,100,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,100,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,101,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,101,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,102,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,102,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,103,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,103,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,104,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,104,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,105,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,105,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,106,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,106,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,107,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,107,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,108,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,108,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,109,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,109,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,110,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,110,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,111,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,111,'30000',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,112,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,112,'10000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,113,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,113,'20000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (32,114,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (33,114,'30000',NULL);


--Attributes Room
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (1,127,'101',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (2,127,'1',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (4,127,5);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (1,128,'102',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (2,128,'2',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (4,128,5);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (1,129,'103',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (2,129,'3',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (4,129,5);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (1,130,'104',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (2,130,'1',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (4,130,6);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (1,131,'105',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (2,131,'2',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (4,131,6);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (1,132,'106',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (2,132,'3',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (4,132,7);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (1,133,'107',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (2,133,'1',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (4,133,7);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (1,134,'108',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (2,134,'2',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (4,134,8);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (1,135,'109',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (2,135,'3',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (4,135,8);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (1,136,'110',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (2,136,'1',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (4,136,8);


--Attributes Block
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (34,227,136);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (35,227,NULL,to_date('10-10-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (36,227,NULL,to_date('02-11-2017','dd-mm-yyyy') );
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (37,227,'Repair',NULL);


--Attributes Order
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (5,300,'1',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (6,300,127);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (7,300,900);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (8,300,NULL,to_date('05-05-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (9,300,'false',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (10,300,'false',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (11,300,NULL,to_date('15-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (12,300,NULL,to_date('30-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (13,300,'386',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (14,300,'Хочу розы и фиалки каждое утро!',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (44,300,999);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (57,300,450);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (5,301,'2',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (6,301,128);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (7,301,901);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (8,301,NULL,to_date('22-02-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (9,301,'true',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (10,301,'false',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (11,301,NULL,to_date('29-03-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (12,301,NULL,to_date('05-04-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (13,301,'683',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (14,301,'Денис просил в номер доп. услуги',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (44,301,901);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (57,301,451);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (5,302,'3',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (6,302,129);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (7,302,902);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (8,302,NULL,to_date('10-12-2016','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (9,302,'true',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (10,302,'true',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (11,302,NULL,to_date('27-12-2016','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (12,302,NULL,to_date('04-01-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (13,302,'1057',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (14,302,NULL,NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (44,302,999);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (57,302,452);


--Attributes MODIFICATION HISTORY
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (42,500,300);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (43,500,NULL,to_date('05-05-2017','dd-mm-yyyy'));
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (59,500,127);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (60,500,900);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (61,500,NULL,to_date('05-05-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (62,500,'false',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (63,500,'false',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (64,500,NULL,to_date('16-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (65,500,NULL,to_date('30-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (66,500,'364',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (67,500,'Хочу розы каждое утро!',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (68,500,450);

INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (42,501,301);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (43,501,NULL,to_date('22-02-2017','dd-mm-yyyy'));
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (59,501,128);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (60,501,901);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (61,501,NULL,to_date('05-05-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (62,501,'false',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (63,501,'false',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (64,501,NULL,to_date('29-03-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (65,501,NULL,to_date('05-04-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (66,501,'683',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (67,501,'Денис просил в номер доп. услуги',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (68,501,451);

INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (42,502,302);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (43,502,NULL,to_date('10-12-2016','dd-mm-yyyy'));
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (59,502,129);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (60,502,901);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (61,502,NULL,to_date('05-05-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (62,502,'true',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (63,502,'false',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (64,502,NULL,to_date('27-12-2016','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (65,502,NULL,to_date('04-01-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (66,502,'1057',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (67,502,NULL,NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (68,502,452);

INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (42,503,300);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (43,503,NULL,to_date('06-05-2017','dd-mm-yyyy'));
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (59,503,127);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (60,503,999);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (61,503,NULL,to_date('05-05-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (62,503,'false',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (63,503,'false',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (64,503,NULL,to_date('16-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (65,503,NULL,to_date('30-06-2017','dd-mm-yyyy'));
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (66,503,'364',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (67,503,'Хочу розы и фиалки каждое утро!',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (68,503,450);



--Attributes USER
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (15,900,'stephenking@mail.ru',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (16,900,'$2a$11$MFiUEzTZoYhoAgdrQjj09.glb5srqCtRzO/rV2qiYHrh/PO3cF8Ha',NULL);
/* ДРУЖИЩЕ, ПАРОЛЬ ДЛЯ ЭТОГО ЮЗЕРА = 'awl8yat4oiu' */
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (17,900,'Stephen',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (18,900,'King',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (19,900,'',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (20,900,3);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (3,900,'true',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (15,901,'krasavchik@gmail.com',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (16,901,'lkjaytuayiaaotwiuy',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (17,901,'Johnny',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (18,901,'Depp',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (19,901,'Капитан!',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (20,901,3);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (3,901,'true',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (15,902,'kaskader@china.net',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (16,902,'jkdfhiutyjhsgdklsyre23429768',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (17,902,'Jackie',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (18,902,'Chan',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (19,902,'У меня доспехи Бога',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (20,902,3);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (3,902,'true',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (15,999,'admin@mail.ru',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (16,999,'ad',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (17,999,'Admin',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (18,999,'Adminskyi',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (19,999,'',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (20,999,1);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (3,999,'true',NULL);


--Attributes Phone
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (38,1100,'08001234516',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (38,1101,'08001234517',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (38,1102,'08001234518',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (38,1103,'08001236458',NULL);


-- Attributes NOTIFICATION
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (26,1400,11); -- уведомление типа Resettlement
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (21,1400,900); -- отправлено Кингом
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (22,1400,'Мне выбили стекло камнем',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (27,1400,300); -- в 300-м заказе 
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (23,1400,NULL,to_date('02-04-2017','dd-mm-yyyy'));
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (24,1400,999); -- выполнено Админом Админским
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (25,1400,NULL,to_date('02-04-2017','dd-mm-yyyy'));

INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (26,1401,12); -- уведомление типа Renewal
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (21,1401,901); -- отправитель
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (22,1401,'Я хочу продолжить проживание в отеле',NULL);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (27,1401,301); -- о 301-м заказе
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (23,1401,NULL,to_date('18-06-2017','dd-mm-yyyy'));
-- Так как оно не выполнено, то нет смысла в полях "Кем выполнено" и "Дата выполнения"
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (24,1401,0);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (25,1401,NULL,NULL);


-- Attributes CATEGORY
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (45,450,'all incusive',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (46,450,'5001',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (45,451,'bed and breakfast',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (46,451,'1000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (45,452,'half pansion',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (46,452,'2000',NULL);


-- Attributes MAINTENANCE
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (47,1500,'washing',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (48,1500,'odezhda',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (49,1500,'300',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (47,1501,'breakfast',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (48,1501,'food',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (49,1501,'400',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (47,1502,'dinner',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (48,1502,'food',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (49,1502,'600',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (47,1503,'supper',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (48,1503,'food',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (49,1503,'450',NULL);

INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (47,1504,'whore =*',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (48,1504,'special',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (49,1504,'10000',NULL);

-- Attributes COOMPLIMETNARY
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (51,1601,1501);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (51,1602,1502);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (51,1603,1503);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (51,1604,1500);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (51,1605,1501);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (51,1606,1501);
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (51,1607,1503);


-- Attributes JOURNALRECORD
INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (53,1701,1504);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (54,1701,'3',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (55,1701,'30000',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (56,1701,NULL,to_date('01-04-2017','dd-mm-yyyy'));

INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (53,1702,1503);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (54,1702,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (55,1702,'450',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (56,1702,NULL,to_date('02-04-2017','dd-mm-yyyy'));

INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (53,1703,1500);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (54,1703,'2',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (55,1703,'600',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (56,1703,NULL,to_date('29-12-2016','dd-mm-yyyy'));

INSERT INTO OBJREFERENCE (ATTR_ID, OBJECT_ID, REFERENCE) VALUES (53,1704,1502);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (54,1704,'1',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (55,1704,'600',NULL);
INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) VALUES (56,1704,NULL,to_date('30-12-2016','dd-mm-yyyy'));



COMMIT;









