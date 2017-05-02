sqlplus '/ as sysdba'
EXEC DBMS_XDB.SETHTTPPORT(0);
commit;