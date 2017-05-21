-- USER ROLE
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1, NULL, 10, 'Admin', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (2, NULL, 10, 'Reception', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (3, NULL, 10, 'Client', NULL);


-- ROOM TYPE
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (5, NULL, 5, 'TypeLuxe', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (6, NULL, 5, 'TypeSemiLuxe', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (7, NULL, 5, 'TypeEconomy', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (8, NULL, 5, 'TypePresident', NULL);
--to 10


-- NOTIFICATION TYPE
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (11, NULL, 11, 'NotificationType1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (12, NULL, 11, 'NotificationType2', NULL);
--to 30


-- RATE
-- for Luxe
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (31, 5, 6, '02.12-01.03', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (32, 5, 6, '02.03-01.06', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (33, 5, 6, '02.06-01.09', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (34, 5, 6, '02.09-01.12', NULL);
-- for SemiLuxe
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (35, 6, 6, '02.12-01.03', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (36, 6, 6, '02.03-01.06', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (37, 6, 6, '02.06-01.09', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (38, 6, 6, '02.09-01.12', NULL);
-- for Econom
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (39, 7, 6, '02.12-01.03', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (40, 7, 6, '02.03-01.06', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (41, 7, 6, '02.06-01.09', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (42, 7, 6, '02.09-01.12', NULL);
-- for President
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (43, 8, 6, '02.12-01.03', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (44, 8, 6, '02.03-01.06', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (45, 8, 6, '02.06-01.09', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (46, 8, 6, '02.09-01.12', NULL);
-- to 54


-- PRICE
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (55, 31, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (56, 31, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (57, 31, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (58, 32, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (59, 32, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (60, 32, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (61, 33, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (62, 33, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (63, 33, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (64, 34, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (65, 34, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (66, 34, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (67, 35, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (68, 35, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (69, 35, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (70, 36, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (71, 36, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (72, 36, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (73, 37, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (74, 37, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (75, 37, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (76, 38, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (77, 38, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (78, 38, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (79, 39, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (80, 39, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (81, 39, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (82, 40, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (83, 40, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (84, 40, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (85, 41, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (86, 41, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (87, 41, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (88, 42, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (89, 42, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (90, 42, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (91, 43, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (92, 43, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (93, 43, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (94, 44, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (95, 44, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (96, 44, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (97, 45, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (98, 45, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (99, 45, 7, '3000 | 3', NULL);

INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (100, 46, 7, '1000 | 1', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (101, 46, 7, '2000 | 2', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (102, 46, 7, '3000 | 3', NULL);
-- to 126




-- ROOM
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (127, NULL, 1, 'Room101', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (128, NULL, 1, 'Room102', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (129, NULL, 1, 'Room103', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (130, NULL, 1, 'Room104', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (131, NULL, 1, 'Room105', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (132, NULL, 1, 'Room106', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (133, NULL, 1, 'Room107', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (134, NULL, 1, 'Room108', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (135, NULL, 1, 'Room109', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (136, NULL, 1, 'Room110', NULL);
--to 226



-- BLOCK
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (227, NULL, 8, 'Block101', NULL);

-- ORDER
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (300, NULL, 2, 'Order01', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (301, NULL, 2, 'Order02', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (302, NULL, 2, 'Order03', NULL);

-- CATEGORY
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (450, NULL, 13, 'all_incusive', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (451, NULL, 13, 'bed_and_breakfast', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (452, NULL, 13, 'half_pansion', NULL);

 -- MODIFICATION HISTORY
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (500, 300, 12, 'HistoryOrder01', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (501, 301, 12, 'HistoryOrder02', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (502, 302, 12, 'HistoryOrder03', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (503, 300, 12, 'HistoryOrder03', NULL);

-- USER
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (900, NULL, 3, 'StephenKing', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (901, NULL, 3, 'JohnnyDepp', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (902, NULL, 3, 'JackieChan', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (999, NULL, 3, 'AdminAdmin', NULL);

-- PHONE
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1100, 900, 9, '08001234516', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1101, 901, 9, '08001234517', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1102, 901, 9, '08001234518', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1103, 902, 9, '08001236458', NULL);

-- NOTIFICATION
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1400, NULL, 4, 'Notification01', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1401, NULL, 4, 'Notification02', NULL);

-- MAINTENANCE
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1500, NULL, 14, 'washing', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1501, NULL, 14, 'breakfast', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1502, NULL, 14, 'dinner', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1503, NULL, 14, 'supper', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1504, NULL, 14, 'whore', NULL);

-- COMPLIMENTARY
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1601, 450, 15, 'breakfastInAllInclusive', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1602, 450, 15, 'dinnerInAllInclusive', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1603, 450, 15, 'supperInAllInclusive', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1604, 450, 15, 'washingInAllInclusive', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1605, 451, 15, 'breakfastInBedAndDreakfast', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1606, 452, 15, 'breakfastInHalfPansion', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1607, 452, 15, 'supperInHalfPansion', NULL);

-- JOURNALRECORD
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1701, 301, 16, 'uslugaDenisu', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1702, 301, 16, 'supperDenisu', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1703, 302, 16, 'washingToPan', NULL);
INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1704, 302, 16, 'dinnerToPan', NULL);



COMMIT;