//package tests.database;
//
//import lombok.extern.log4j.Log4j;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//import projectpackage.model.auth.Phone;
//import projectpackage.model.auth.Role;
//import projectpackage.model.auth.User;
//import projectpackage.repository.reacdao.ReactEAVManager;
//import projectpackage.repository.reacdao.exceptions.CyclicEntityQueryException;
//import projectpackage.repository.reacdao.exceptions.ResultEntityNullException;
//
//import java.util.List;
//
//@Log4j
//@Transactional
//public class ReactEAVTest extends AbstractDatabaseTest {
//    private final String SEPARATOR = "**********************************************************";
//
//    @Autowired
//    ReactEAVManager manager;
//
//    //Получить список юзеров
//    @Test
//    public void queryTestOfUsers(){
//        List<User> list = null;
//        try {
//            list = (List<User>) manager.createReactEAV(User.class).getEntityCollection();
//        } catch (ResultEntityNullException e) {
//            System.out.println("RESULT IS NULL");
//        }
//        for (User user:list){
//            System.out.println(user);
//        }
//        System.out.println(SEPARATOR);
//    }
//
//    //Получить сортированный список юзеров
//    @Test
//    public void queryTestOfUsersOrderBy(){
//        List<User> list = null;
//        try {
//            list = (List<User>) manager.createReactEAV(User.class).getEntityCollectionOrderByParameter("firstName", true);
//        } catch (ResultEntityNullException e) {
//            System.out.println("RESULT IS NULL");
//        }
//        for (User user:list){
//            System.out.println(user);
//        }
//        System.out.println(SEPARATOR);
//    }
//
//    //Получить одного юзера, ошибка в айди - словить ResultEntityNullException
//    @Test
//    public void errorQueryTestOfSingleUser(){
//        int userId=127;
//        User user = null;
//        try {
//            user = (User) manager.createReactEAV(User.class).getSingleEntityWithId(userId);
//        } catch (ResultEntityNullException e) {
//            e.printStackTrace();
//        }
//        System.out.println("ENTITY IS NULL(expected yes)? - "+user);
//        System.out.println(SEPARATOR);
//    }
//
//    //Получить одного юзера с вставленной ролью
//    @Test
//    public void querySingleUserFetchRole(){
//        int userId=901;
//        User user = null;
//        try {
//            user = (User) manager.createReactEAV(User.class).fetchInnerEntityCollection(Role.class).closeFetch().getSingleEntityWithId(userId);
//        } catch (ResultEntityNullException e) {
//            e.printStackTrace();
//        }
//        System.out.println(user);
//        System.out.println(SEPARATOR);
//    }
//
//    //Получить список телефонов
//    @Test
//    public void queryTestOfPhones(){
//        List<Phone> phones = null;
//        try {
//            phones = (List<Phone>) manager.createReactEAV(Phone.class).getEntityCollection();
//        } catch (ResultEntityNullException e) {
//        }
//        for (Phone phone:phones){
//            System.out.println(phone);
//        }
//        System.out.println(SEPARATOR);
//    }
//
//    //Получить список ролей
//    @Test
//    public void queryTestOfRoles(){
//        List<Role> roles = null;
//        try {
//            roles = (List<Role>) manager.createReactEAV(Role.class).getEntityCollection();
//        } catch (ResultEntityNullException e) {
//        }
//        for (Role role:roles){
//            System.out.println(role);
//        }
//        System.out.println(SEPARATOR);
//    }
//
//    //Получить сортированный список ролей
//    @Test
//    public void queryTestOfRolesOrderBy(){
//        List<Role> roles = null;
//        try {
//            roles = (List<Role>) manager.createReactEAV(Role.class).getEntityCollectionOrderByParameter("objectId", true);
//        } catch (ResultEntityNullException e) {
//        }
//        for (Role role:roles){
//            System.out.println(role);
//        }
//        System.out.println(SEPARATOR);
//    }
//
//    //Получить одного юзера с вставленным телефоном и ролью
//    @Test
//    public void queryTestOfSingeUserFetchPhonesFetchRoles(){
//        User user = null;
//        try {
//            user = (User) manager.createReactEAV(User.class).fetchInnerEntityCollection(Phone.class).closeFetch().fetchInnerEntityCollection(Role.class).closeFetch().getSingleEntityWithId(901);
//        } catch (ResultEntityNullException e) {
//        }
//        System.out.println(user.toString());
//        System.out.println(SEPARATOR);
//    }
//
//    //Получить список юзеров со вставленными телефонами
//    @Test
//    public void queryTestOfUsersFetchPhones(){
//        List<User> list = null;
//        try {
//            list = (List<User>) manager.createReactEAV(User.class).fetchInnerEntityCollection(Phone.class).closeFetch().getEntityCollection();
//        } catch (ResultEntityNullException e) {
//        }
//        for (User user:list){
//            System.out.println(user);
//        }
//        System.out.println(SEPARATOR);
//    }
//
//    //Получить список юзеров со вставленными ролями
//    @Test
//    public void queryTestOfUsersFetchRoles(){
//        List<User> list = null;
//        try {
//            list = (List<User>) manager.createReactEAV(User.class).fetchInnerEntityCollection(Role.class).closeFetch().getEntityCollection();
//        } catch (ResultEntityNullException e) {
//        }
//        for (User user:list){
//            System.out.println(user);
//        }
//        System.out.println(SEPARATOR);
//    }
//
//    //Получить список юзеров с ролями и телефонами
//    @Test
//    public void queryTestOfUsersFetchRolesAndPhones(){
//        List<User> list = null;
//        try {
//            list = (List<User>) manager.createReactEAV(User.class).fetchInnerEntityCollection(Role.class).closeFetch().fetchInnerEntityCollection(Phone.class).closeFetch().getEntityCollection();
//        } catch (ResultEntityNullException e) {
//        }
//        for (User user:list){
//            System.out.println(user);
//        }
//        System.out.println(SEPARATOR);
//    }
//
//    //Получить список юзеров с ролями и телефонами(порядок фетча наоборот)
//    @Test
//    public void queryTestOfUsersFetchRolesAndPhonesBackwards(){
//        List<User> list = null;
//        try {
//            list = (List<User>) manager.createReactEAV(User.class).fetchInnerEntityCollection(Phone.class).closeFetch().fetchInnerEntityCollection(Role.class).closeFetch().getEntityCollection();
//        } catch (ResultEntityNullException e) {
//        }
//        for (User user:list){
//            System.out.println(user);
//        }
//        System.out.println(SEPARATOR);
//    }
//
//    //Получить ошибку по циклическому графу
//    @Test(expected = CyclicEntityQueryException.class)
//    public void errorWithCyclicGraphs(){
//        List<User> list = null;
//        try {
//            list = (List<User>) manager.createReactEAV(User.class).fetchInnerEntityCollection(Role.class).closeFetch().fetchInnerEntityCollection(Phone.class).closeFetch().fetchInnerEntityCollection(Role.class).closeFetch().getEntityCollection();
//        } catch (ResultEntityNullException e) {
//        }
//        for (User user:list){
//            System.out.println(user);
//        }
//        System.out.println(SEPARATOR);
//    }
//}
