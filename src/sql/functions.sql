SET SERVEROUTPUT ON;

CREATE OR REPLACE FUNCTION delete_object_by_id(obj_id IN NUMBER)
RETURN NUMBER
IS
ref_count NUMBER;
count_rows NUMBER;
e exception;
BEGIN
    count_rows := 0;
    SELECT COUNT(*) INTO ref_count FROM OBJREFERENCE WHERE REFERENCE = obj_id;
    IF ref_count > 0 THEN RAISE e;END IF;
    EXECUTE IMMEDIATE 'DELETE FROM OBJREFERENCE WHERE OBJECT_ID = ' || obj_id;
    count_rows := count_rows + SQL%ROWCOUNT;
    EXECUTE IMMEDIATE 'DELETE FROM ATTRIBUTES WHERE OBJECT_ID = ' || obj_id;
    count_rows := count_rows + SQL%ROWCOUNT;
    EXECUTE IMMEDIATE 'DELETE FROM OBJECTS WHERE OBJECT_ID = ' || obj_id;
    count_rows := count_rows + SQL%ROWCOUNT;
    RETURN count_rows;
EXCEPTION WHEN e THEN
RAISE_APPLICATION_ERROR(-20001, 'Cannot delete object with references on self!');
END;

BEGIN
DBMS_OUTPUT.PUT_LINE(delete_object_by_id(5));
END;