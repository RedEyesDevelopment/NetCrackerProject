drop table OBJTYPE CASCADE CONSTRAINTS;
drop table ATTRTYPE CASCADE CONSTRAINTS;
drop table OBJECTS CASCADE CONSTRAINTS;
drop table ATTRIBUTES CASCADE CONSTRAINTS;
drop table OBJREFERENCE CASCADE CONSTRAINTS;

CREATE TABLE OBJTYPE
  (
    OBJECT_TYPE_ID NUMBER(20) NOT NULL ENABLE,
    PARENT_ID      NUMBER(20),
    CODE           VARCHAR2(20) NOT NULL UNIQUE,
    NAME           VARCHAR2(200 BYTE),
    DESCRIPTION    VARCHAR2(1000 BYTE),
    CONSTRAINT CON_OBJECT_TYPE_ID PRIMARY KEY (OBJECT_TYPE_ID),
    CONSTRAINT CON_PARENT_ID FOREIGN KEY (PARENT_ID) REFERENCES OBJTYPE (OBJECT_TYPE_ID) ON DELETE CASCADE ENABLE
  );

COMMENT ON TABLE OBJTYPE IS 'Таблица описаний объектных типов';

COMMENT ON COLUMN OBJTYPE.OBJECT_TYPE_ID IS 'Идентификатор объектного типа';
COMMENT ON COLUMN OBJTYPE.PARENT_ID IS 'ссылка на идентификатор родительского объектного типа';
COMMENT ON COLUMN OBJTYPE.CODE IS 'название объектного типа в английской кодировке';
COMMENT ON COLUMN OBJTYPE.NAME IS 'название объектного типа в национальной кодировке (для GUI)';
COMMENT ON COLUMN OBJTYPE.DESCRIPTION IS 'разверное описание объектного типа в национальной кодировке (для GUI)';

CREATE TABLE ATTRTYPE (
    ATTR_ID      		NUMBER(20) NOT NULL,
    OBJECT_TYPE_ID 		NUMBER(20) NOT NULL,
	OBJECT_TYPE_ID_REF 	NUMBER(20),
    CODE         		VARCHAR2(20),
    NAME         		VARCHAR2(200 BYTE),
    CONSTRAINT CON_ATTR_ID PRIMARY KEY (ATTR_ID),
    CONSTRAINT CON_ATTR_OBJECT_TYPE_ID FOREIGN KEY (OBJECT_TYPE_ID) REFERENCES OBJTYPE (OBJECT_TYPE_ID),
	CONSTRAINT CON_ATTR_OBJECT_TYPE_ID_REF FOREIGN KEY (OBJECT_TYPE_ID_REF) REFERENCES OBJTYPE (OBJECT_TYPE_ID)
);

COMMENT ON TABLE ATTRTYPE
	IS 'Таблица описаний атрибутных типов';
COMMENT ON COLUMN ATTRTYPE.OBJECT_TYPE_ID
	IS 'ссылка на идентификатор объектного типа класса, который характеризует данный атрибутный тип';
COMMENT ON COLUMN ATTRTYPE.OBJECT_TYPE_ID_REF
	IS 'ссылка на идентификатор объектного типа класса, который для кратности "один-ко-многим" находится в отношении "один"
	при отношении "много", в котором находится объектный тип класса из ATTRTYPE.OBJECT_TYPE_ID';
COMMENT ON COLUMN ATTRTYPE.CODE
	IS 'название атрибутного типа в английской кодировке';
COMMENT ON COLUMN ATTRTYPE.NAME
	IS 'название атрибутного типа в национальной кодировке (для GUI)';

CREATE TABLE OBJECTS (
    OBJECT_ID      NUMBER(20) NOT NULL,
    PARENT_ID      NUMBER(20),
    OBJECT_TYPE_ID NUMBER(20) NOT NULL,
    NAME           VARCHAR2(2000 BYTE),
    DESCRIPTION    VARCHAR2(4000 BYTE),
    CONSTRAINT CON_OBJECTS_ID PRIMARY KEY (OBJECT_ID),
    CONSTRAINT CON_PARENTS_ID FOREIGN KEY (PARENT_ID) REFERENCES OBJECTS (OBJECT_ID) ON DELETE CASCADE DEFERRABLE,
    CONSTRAINT CON_OBJ_TYPE_ID FOREIGN KEY (OBJECT_TYPE_ID) REFERENCES OBJTYPE (OBJECT_TYPE_ID)
);

COMMENT ON TABLE OBJECTS IS 'Таблица описаний экземпляров объектов';

CREATE TABLE ATTRIBUTES
  (
    ATTR_ID    NUMBER(20) NOT NULL,
    OBJECT_ID  NUMBER(20) NOT NULL,
    VALUE      VARCHAR2(4000 BYTE),
    DATE_VALUE DATE,
	CONSTRAINT CON_ATTRIBUTES_PK PRIMARY KEY (ATTR_ID,OBJECT_ID),
    CONSTRAINT CON_AOBJECT_ID FOREIGN KEY (OBJECT_ID) REFERENCES OBJECTS (OBJECT_ID) ON DELETE CASCADE,
    CONSTRAINT CON_AATTR_ID FOREIGN KEY (ATTR_ID) REFERENCES ATTRTYPE (ATTR_ID) ON DELETE CASCADE
  );

COMMENT ON TABLE ATTRIBUTES IS 'Таблица описаний атрибутов экземпляров объектов';
COMMENT ON COLUMN ATTRIBUTES.VALUE IS 'Значение атрибута экземпляра объекта в виде строки или числа';
COMMENT ON COLUMN ATTRIBUTES.DATE_VALUE IS 'Значение атрибута экземпляра объекта в виде даты';

CREATE TABLE OBJREFERENCE
  (
    ATTR_ID   NUMBER(20) NOT NULL,
    REFERENCE NUMBER(20) NOT NULL,
    OBJECT_ID NUMBER(20) NOT NULL,
	CONSTRAINT CON_OBJREFERENCE_PK PRIMARY KEY (ATTR_ID,REFERENCE,OBJECT_ID),
    CONSTRAINT CON_REFERENCE FOREIGN KEY (REFERENCE) REFERENCES OBJECTS (OBJECT_ID) ON DELETE CASCADE,
    CONSTRAINT CON_ROBJECT_ID FOREIGN KEY (OBJECT_ID) REFERENCES OBJECTS (OBJECT_ID) ON DELETE CASCADE,
    CONSTRAINT CON_RATTR_ID FOREIGN KEY (ATTR_ID) REFERENCES ATTRTYPE (ATTR_ID) ON DELETE CASCADE
  );

COMMENT ON TABLE OBJREFERENCE IS 'Таблица описаний связей между экземплярами объектов';
COMMENT ON COLUMN OBJREFERENCE.ATTR_ID IS 'ссылка на атрибутный тип как ассоциативная связь между экземплярами объектов';
COMMENT ON COLUMN OBJREFERENCE.OBJECT_ID IS 'ссылка на экземпляр 1-го объекта ассоциативной связи с кратностью "много"';
COMMENT ON COLUMN OBJREFERENCE.REFERENCE IS 'ссылка на экземпляр 2-го объекта ассоциативной связи с кратностью "один"';