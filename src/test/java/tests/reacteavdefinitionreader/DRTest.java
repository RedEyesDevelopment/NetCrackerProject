package tests.reacteavdefinitionreader;

import org.apache.log4j.Logger;
import org.junit.Test;
import projectpackage.repository.reacteav.relationsdata.EntityOuterRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityVariablesData;
import projectpackage.repository.reacteav.support.ReactAnnDefinitionReader;
import projectpackage.service.securityservice.SecurityServiceImpl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Gvozd on 15.05.2017.
 */
public class DRTest {

    private static final Logger LOGGER = Logger.getLogger(DRTest.class);


    @Test
    public void doTestReader(){
        ReactAnnDefinitionReader reader = new ReactAnnDefinitionReader("projectpackage/model");
        Map<Class, LinkedHashMap<String, EntityVariablesData>> objectVariables = reader.makeObjectsVariablesMap();
        Map<Class, HashMap<Class, EntityOuterRelationshipsData>> outerRelations = reader.makeOuterRelationshipsMap();
        Map<Class, HashMap<String, EntityReferenceRelationshipsData>> referenceRelations = reader.makeObjectsReferenceRelationsMap();
        LOGGER.info("CLASSES:");
        reader.printClassesList();
        LOGGER.info("VARIABLES:");
        for (Map.Entry<Class, LinkedHashMap<String, EntityVariablesData>> entry: objectVariables.entrySet()){
            LOGGER.info("CLASS KEY="+entry.getKey());
            for (Map.Entry<String, EntityVariablesData> entry1:entry.getValue().entrySet()){
                LOGGER.info("    STRING KEY="+entry1.getKey());
                LOGGER.info("        PARAMETER CLASS="+entry1.getValue().getParameterClass());
                LOGGER.info("        DATABASE NATIVE CODE VALUE="+entry1.getValue().getDatabaseNativeCodeValue());
                LOGGER.info("        DATABASE ATTRTYPE ID VALUE="+entry1.getValue().getDatabaseAttrtypeIdValue());
            }
        }
        LOGGER.info("OUTER RELATIONS:");
        for (Map.Entry<Class, HashMap<Class, EntityOuterRelationshipsData>> entry: outerRelations.entrySet()){
            LOGGER.info("INNER CLASS KEY="+entry.getKey());
            for (Map.Entry<Class, EntityOuterRelationshipsData> entry1:entry.getValue().entrySet()){
                LOGGER.info("    OUTER CLASS KEY="+entry1.getKey());
                LOGGER.info("        OUTER FIELD NAME="+entry1.getValue().getOuterFieldName());
                LOGGER.info("        OUTER FIELD KEY="+entry1.getValue().getOuterFieldKey());
                LOGGER.info("        INNER FIELD KEY="+entry1.getValue().getInnerFieldKey());
            }
        }
        LOGGER.info("REFERENCE RELATIONS:");
        for (Map.Entry<Class, HashMap<String, EntityReferenceRelationshipsData>> entry: referenceRelations.entrySet()){
            LOGGER.info("INNER CLASS KEY="+entry.getKey());
            for (Map.Entry<String, EntityReferenceRelationshipsData> entry1:entry.getValue().entrySet()){
                LOGGER.info("    REFERENCE KEY="+entry1.getKey());
                LOGGER.info("        OUTER CLASS:"+entry1.getValue().getOuterClass());
                LOGGER.info("        OUTER FIELD NAME="+entry1.getValue().getOuterFieldName());
                LOGGER.info("        OUTER ID KEY="+entry1.getValue().getOuterIdKey());
                LOGGER.info("        INNER ID KEY="+entry1.getValue().getInnerIdKey());
            }
        }
        assertEquals(16, objectVariables.keySet().size());
        assertEquals(6, outerRelations.keySet().size());
        assertEquals(8, referenceRelations.keySet().size());
        int per=0;
        for (HashMap<String, EntityReferenceRelationshipsData> rr: referenceRelations.values()){
            for (EntityReferenceRelationshipsData data:rr.values()){
                per++;
            }
        }
        assertEquals(15, per);
    }
}
