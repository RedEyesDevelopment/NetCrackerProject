
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (1,NULL,'Room','Комната',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (2,NULL,'Order','Заказ',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (3,NULL,'User','Пользователь',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (4,NULL,'Notification','Уведомление',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (5,NULL,'Room_type','Тип комнаты',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (6,5,'Rate','Тариф',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (7,6,'Price','Цена',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (8,NULL,'Block','Блокировка',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (9,3,'Phone','Телефон',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (10,NULL,'Role','Роль',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (11,NULL,'Notification_type','Тип уведомления',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (12,2,'Modification_history','История изменений заказа',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (13,NULL,'Category','Категория ',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (14,NULL,'Maintenance','Дополнительная услуга',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (15,13,'Complimentary','Включённая услуга',NULL);
INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (16,2,'JournalRecord','Запись о предоставленной услуге',NULL);


INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (1,1,NULL,'Room_number','Номер комнаты');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (2,1,NULL,'Number_of_residents','Количество мест');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (4,1,5,'Has_room_type','Тип комнаты');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (5,2,NULL,'Order_id','ID заказа');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (6,2,1,'Booked','Номер комнаты');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (7,2,3,'Belong','Клиент');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (8,2,NULL,'Registration_date','Дата составления заказа');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (9,2,NULL,'Is_paid_for','Оплачено');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (10,2,NULL,'Is_confirmed','Утверждено');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (11,2,NULL,'Living_start_date','Дата въезда');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (12,2,NULL,'Living_finish_date','Дата выезда');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (13,2,NULL,'Sum','Сумма к оплате');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (14,2,NULL,'Comment','Комментарий');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (44,2,3,'Modificated_by','Автор последнего изменения');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (57,2,13,'Has_category','Имеет категорию');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (15,3,NULL,'Email','Email');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (16,3,NULL,'Password','Пароль');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (17,3,NULL,'First_name','Имя');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (18,3,NULL,'Last_name','Фамилия');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (19,3,NULL,'Additional_info','Доп. информация');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (20,3,10,'Has_role','Роль');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (3,3,NULL,'Enabled','Активный');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (21,4,3,'Sent_by','Автор');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (22,4,NULL,'Message','Сообщение');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (23,4,NULL,'Send_date','Отправлено');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (24,4,3,'Executed_by','Выполнено');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (25,4,NULL,'Executed_date','Дата выполнения');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (26,4,11,'Has_notif_type','Тип уведомления');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (27,4,2,'Consider','Про заказ');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (28,5,NULL,'Room_type_title','Тип комнаты');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (29,5,NULL,'Content','Описание');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (30,6,NULL,'Rate_from_date','Начало периода');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (31,6,NULL,'Rate_to_date','Конец периода');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (32,7,NULL,'Number_of_people','Количество проживающих');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (33,7,NULL,'Rate','Тариф');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (34,8,1,'Blocks','Номер комнаты');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (35,8,NULL,'Block_start_date','Начало блокировки');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (36,8,NULL,'Block_finish_date','Конец блокировки');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (37,8,NULL,'Reason','Причина');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (38,9,NULL,'Phone_number','Номер телефона');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (39,10,NULL,'Role_name','Название роли');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (40,11,NULL,'Notif_type_title','Тип уведомления');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (41,11,10,'Oriented','Для группы');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (42,12,3,'Changed_by','Автор модификации');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (43,12,NULL,'Modif_date','Дата модификации');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (45,13,NULL,'Category_title','Название категории');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (46,13,NULL,'Category_price','Цена за категорию');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (47,14,NULL,'Maintenance_title','Название услуги');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (48,14,NULL,'Maintenance_type','Тип услуги');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (49,14,NULL,'Maintenance_price','Цена за услугу');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (51,15,14,'Contains','Содержит услугу');

INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (53,16,14,'Commit','Предоставляемая услуга');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (54,16,NULL,'Count','Количество услуг(в штуках)');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (55,16,NULL,'Cost','Стоимость');
INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (56,16,NULL,'Used_date','Дата предоставления услуги');



COMMIT;






