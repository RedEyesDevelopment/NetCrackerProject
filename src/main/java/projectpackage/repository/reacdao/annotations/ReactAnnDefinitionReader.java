package projectpackage.repository.reacdao.annotations;

import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Gvozd on 15.05.2017.
 */
@Component
public class ReactAnnDefinitionReader {
    private String packageName;
    private Set<Class> classList;
    private static final Class ENTITYANNOTATION = ReactEntity.class;
    private static final Class REFERENCEANNOTATION = ReactReference.class;
    private static final Class CHILDANNOTATION = ReactChild.class;
    private static final Class FIELDANNOTATION = ReactField.class;

    public ReactAnnDefinitionReader(String packageName) {
        this.packageName = packageName;
        this.classList = new HashSet<>();
    }

    public void getClasses() {
        Reflections reflections = new Reflections(packageName);
        Set<Class> allClasses =
                reflections.getTypesAnnotatedWith(ENTITYANNOTATION);
        classList = allClasses;
    }

    public void printPackageName(){
        System.out.println(packageName);
    }

    public void printClassesList(){
        System.out.println("ClassList size is "+classList.size());
        for (Class clazz:classList){
            System.out.println("Class="+clazz.getName());
        }
    }

//    public void parseClasses() {
//        try{
//            for (Class clazz: this.getClass().getClassLoader()){
//
//            }
//        }
//
//
//        try {
//            for (Method method : AnnotationParsing.class.getClassLoader()
//                    .loadClass(("com.journaldev.annotations.AnnotationExample")).getMethods()) {
//                // checks if MethodInfo annotation is present for the method
//                if (method.isAnnotationPresent(com.journaldev.annotations.MethodInfo.class)) {
//                    try {
//                        // iterates all the annotations available in the method
//                        for (Annotation anno : method.getDeclaredAnnotations()) {
//                            System.out.println("Annotation in Method '" + method + "' : " + anno);
//                        }
//                        MethodInfo methodAnno = method.getAnnotation(MethodInfo.class);
//                        if (methodAnno.revision() == 1) {
//                            System.out.println("Method with revision no 1 = " + method);
//                        }
//
//                    } catch (Throwable ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        } catch (SecurityException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

}
