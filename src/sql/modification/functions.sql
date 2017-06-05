CREATE OR REPLACE FUNCTION delete_object_by_id(obj_id IN NUMBER)
RETURN NUMBER
IS
ref_count NUMBER;
CURSOR References_list IS
        SELECT Object_id
        FROM ObjReference
        WHERE Reference = obj_id;
reference_rec References_list%ROWTYPE;

reference_entity VARCHAR(20);
error_message VARCHAR(200);

CURSOR Childs IS
        SELECT Object_id
        FROM Objects
        WHERE Parent_id = obj_id;
child Childs%ROWTYPE;

deleted_rows NUMBER;
BEGIN

    ref_count := 0;
    SELECT COUNT(*) INTO ref_count FROM OBJREFERENCE WHERE REFERENCE = obj_id;

    IF (ref_count > 0) THEN
        FOR reference_rec in References_list LOOP
            SELECT DISTINCT Code INTO reference_entity
                FROM Objects, ObjType
                WHERE   reference_rec.Object_id = Objects.Object_id
                    AND Objects.Object_Type_Id = ObjType.Object_Type_Id;
            error_message := error_message || reference_entity || ' and ';
        END LOOP;
        error_message := SUBSTR(error_message, 0, LENGTH(error_message) - 5); -- убираем последний пробел
        RAISE_APPLICATION_ERROR(-20001, error_message);
    END IF;

    deleted_rows := 0;

    FOR child in Childs LOOP
        deleted_rows := deleted_rows + delete_object_by_id(child.Object_id);
    END LOOP;

    EXECUTE IMMEDIATE 'DELETE FROM OBJREFERENCE WHERE OBJECT_ID = ' || obj_id;
    deleted_rows := deleted_rows + SQL%ROWCOUNT;
    EXECUTE IMMEDIATE 'DELETE FROM ATTRIBUTES WHERE OBJECT_ID = ' || obj_id;
    deleted_rows := deleted_rows + SQL%ROWCOUNT;
    EXECUTE IMMEDIATE 'DELETE FROM OBJECTS WHERE OBJECT_ID = ' || obj_id;
    deleted_rows := deleted_rows + SQL%ROWCOUNT;
    RETURN deleted_rows;

END;
/