package projectpackage.repository.reacdao;

import projectpackage.model.ReacEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ReacDAO{
    private final static String OBJECTSTABLENAME = "OBJECTS";
    private final static String OBJTYPESTABLENAME = "OBJTYPE";
    private final static String ATTRIBUTESTABLENAME = "ATTRIBUTES";
    private final static String ATTRTYPESTABLENAME = "ATTRTYPE";
    private final static String OBJREFSTABLENAME = "OBJREFERENCE";
    private final static String ATTRIBUTESPERMANENTTABLENAME = "ATTRS";
    private final static String ROOTTABLENAME = "ROOTABLE";
    private final static String ATTRTYPESPERMANENTTABLENAME = "ATTYPES";
    private final static String OTID = "OBJECT_TYPE_ID";
    private final static String OID = "OBJECT_ID";
    private final static String AID = "ATTR_ID";
    private final static String CD = "CODE";
    private FetchNode rootNode;

    public ReacDAO(Class<? extends ReacEntity> entityClass) {
        if (null!=entityClass){
            rootNode = new FetchNode(this, ReacEntityContainer.SINGLE_OBJECT);
            rootNode.setObjectClass(entityClass);
        }
    }

    public FetchNode fetchInnerEntity(Class<? extends ReacEntity> innerEntityClass){
       return fetchingOrderCreation(innerEntityClass, ReacEntityContainer.SINGLE_OBJECT);
    }

    public FetchNode fetchInnerEntityCollectionClass(Class<? extends ReacEntity> innerEntityClass){
        return fetchingOrderCreation(innerEntityClass, ReacEntityContainer.OBJECTS_LIST);
    }

    private FetchNode fetchingOrderCreation(Class<? extends ReacEntity> innerEntityClass, ReacEntityContainer entityContainer){
        FetchNode childNode = new FetchNode(this,entityContainer);
        childNode.setObjectClass(innerEntityClass);
        rootNode.addFetchedNode(childNode);
        return childNode;
    }

    public ReacEntity getSingleEntity(){
        return null;
    }

    public ReacEntity getSingleEntityWithId(int id){
        return null;
    }

    public List<? extends ReacEntity> getEntityCollection(){
        rootNode.setContainer(ReacEntityContainer.OBJECTS_LIST);
        return null;
    }

    public List<? extends ReacEntity> getEntityCollectionOrderByColumn(String orderingColumn) {
        rootNode.setContainer(ReacEntityContainer.OBJECTS_LIST);
        rootNode.returnOrderedBy(orderingColumn);
        return null;
    }
    public List<? extends ReacEntity> getEntityCollectionOrderByComparator(Comparator<? extends ReacEntity> entityComparator){
        rootNode.setContainer(ReacEntityContainer.OBJECTS_LIST);
        return null;
    }

    public void doGet() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        StringBuilder queryBuilder = new StringBuilder();
        ObjectTableNameGenerator attrTables = new ObjectTableNameGenerator(ATTRIBUTESPERMANENTTABLENAME);
        Map<String, String> rootNodeVariables;
        Class objectClass = rootNode.getObjectClass();
        ReacEntity reacEntity = (ReacEntity) objectClass.newInstance();
        int entityObjectType=reacEntity.getEntityObjectTypeForEav();
        Method method = objectClass.getMethod("getEntityFields");
        rootNodeVariables = (Map<String, String>) method.invoke(reacEntity);
        Iterator mapEntries = rootNodeVariables.entrySet().iterator();
        LinkedHashMap<String, String> attributesMap = new LinkedHashMap<>();
        queryBuilder.append("SELECT");
        boolean firstKeyFlag=false;
        while (mapEntries.hasNext()) {
                Map.Entry<String, String> objectPropertiesMapIteratorEntry = (Map.Entry) mapEntries.next();
            String objectParameterKey = objectPropertiesMapIteratorEntry.getKey();
            String databaseColumnValue = objectPropertiesMapIteratorEntry.getValue();
            if (!firstKeyFlag) {
                firstKeyFlag = true;
            } else queryBuilder.append(", ");
            if (databaseColumnValue.startsWith("%")){
                queryBuilder.append(" "+ROOTTABLENAME+"."+databaseColumnValue.substring(1)+" \""+objectParameterKey+"\"");
            } else {
                queryBuilder.append(attrTables.getNextTableName()+".VALUE \""+objectParameterKey+"\", ");
                queryBuilder.append(attrTables.getCurrentTableName()+".DATE_VALUE \""+objectParameterKey+"_DATE\"");
                attributesMap.put(objectParameterKey,databaseColumnValue);
            }
        }
        queryBuilder.append("\nFROM "+OBJECTSTABLENAME+ " "+ROOTTABLENAME);
        for (int i=1;i<=attrTables.getTablesCounter();i++){
            queryBuilder.append(", "+ATTRIBUTESTABLENAME+" "+ATTRIBUTESPERMANENTTABLENAME+i);
            queryBuilder.append(", "+ATTRTYPESTABLENAME+" "+ATTRTYPESPERMANENTTABLENAME+i);
        }
        queryBuilder.append("\nWHERE ");
        queryBuilder.append(ROOTTABLENAME+"."+OTID+"="+entityObjectType);
        mapEntries = attributesMap.entrySet().iterator();
        for (int i=1;i<=attrTables.getTablesCounter();i++){
            queryBuilder.append("\nAND "+ROOTTABLENAME+"."+OTID+"="+ATTRIBUTESPERMANENTTABLENAME+i+"."+OTID);
            queryBuilder.append("\nAND "+ROOTTABLENAME+"."+OTID+"="+ATTRTYPESPERMANENTTABLENAME+i+"."+OTID);
            queryBuilder.append("\nAND "+ATTRIBUTESPERMANENTTABLENAME+i+"."+AID+"="+ATTRTYPESPERMANENTTABLENAME+i+"."+AID);
            Map.Entry<String, String> objectPropertiesMapIteratorEntry = (Map.Entry) mapEntries.next();
            String databaseColumnValue = objectPropertiesMapIteratorEntry.getValue();
            if (databaseColumnValue.startsWith("%")){
                queryBuilder.append("\nAND "+ATTRTYPESPERMANENTTABLENAME+i+"."+CD+"='"+databaseColumnValue.substring(1)+"'");
            } else {
                queryBuilder.append("\nAND "+ATTRTYPESPERMANENTTABLENAME+i+"."+CD+"='"+databaseColumnValue+"'");

            }
        }
        queryBuilder.append(";");
        System.out.println(queryBuilder.toString());
    }



}
