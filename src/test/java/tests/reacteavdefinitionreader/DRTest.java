package tests.reacteavdefinitionreader;

import org.junit.Test;
import projectpackage.repository.reacteav.relationsdata.EntityOuterRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityVariablesData;
import projectpackage.repository.reacteav.support.ReactAnnDefinitionReader;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Gvozd on 15.05.2017.
 */
public class DRTest {

    @Test
    public void doTestReader(){
        ReactAnnDefinitionReader reader = new ReactAnnDefinitionReader("projectpackage/model");
        Map<Class, LinkedHashMap<String, EntityVariablesData>> objectVariables = reader.makeObjectsVariablesMap();
        Map<Class, HashMap<Class, EntityOuterRelationshipsData>> outerRelations = reader.makeOuterRelationshipsMap();
        Map<Class, HashMap<Class, EntityReferenceRelationshipsData>> referenceRelations = reader.makeObjectsReferenceRelationsMap();
        System.out.println("CLASSES:");
        reader.printClassesList();
        System.out.println();
        System.out.println("VARIABLES:");
        for (Map.Entry<Class, LinkedHashMap<String, EntityVariablesData>> entry: objectVariables.entrySet()){
            System.out.println("CLASS KEY="+entry.getKey());
            for (Map.Entry<String, EntityVariablesData> entry1:entry.getValue().entrySet()){
                System.out.println("    STRING KEY="+entry1.getKey());
                System.out.println("        PARAMETER CLASS="+entry1.getValue().getParameterClass());
                System.out.println("        DATABASE ATTRTYPE CODE VALUE="+entry1.getValue().getDatabaseAttrtypeCodeValue());
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("OUTER RELATIONS:");
        for (Map.Entry<Class, HashMap<Class, EntityOuterRelationshipsData>> entry: outerRelations.entrySet()){
            System.out.println("INNER CLASS KEY="+entry.getKey());
            for (Map.Entry<Class, EntityOuterRelationshipsData> entry1:entry.getValue().entrySet()){
                System.out.println("    OUTER CLASS KEY="+entry1.getKey());
                System.out.println("        OUTER FIELD NAME="+entry1.getValue().getOuterFieldName());
                System.out.println("        OUTER FIELD KEY="+entry1.getValue().getOuterFieldKey());
                System.out.println("        INNER FIELD KEY="+entry1.getValue().getInnerFieldKey());
            }
        }
        System.out.println();
        System.out.println("REFERENCE RELATIONS:");
        for (Map.Entry<Class, HashMap<Class, EntityReferenceRelationshipsData>> entry: referenceRelations.entrySet()){
            System.out.println("INNER CLASS KEY="+entry.getKey());
            for (Map.Entry<Class, EntityReferenceRelationshipsData> entry1:entry.getValue().entrySet()){
                System.out.println("    OUTER CLASS KEY="+entry1.getKey());
                System.out.println("        OUTER FIELD NAME="+entry1.getValue().getOuterFieldName());
                System.out.println("        OUTER ID KEY="+entry1.getValue().getOuterIdKey());
                System.out.println("        INNER ID KEY="+entry1.getValue().getInnerIdKey());
            }
        }
        assertEquals(12, objectVariables.keySet().size());
        assertEquals(4, outerRelations.keySet().size());
        assertEquals(10, objectVariables.keySet().size());
    }

}
