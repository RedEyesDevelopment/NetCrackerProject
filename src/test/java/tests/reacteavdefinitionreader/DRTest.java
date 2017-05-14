package tests.reacteavdefinitionreader;

import org.junit.Test;
import projectpackage.repository.reacdao.annotations.ReactAnnDefinitionReader;

/**
 * Created by Gvozd on 15.05.2017.
 */
public class DRTest {

    @Test
    public void doTestReader(){
        ReactAnnDefinitionReader reader = new ReactAnnDefinitionReader("projectpackage/model");
        reader.getClasses();
        reader.printClassesList();
    }
}
