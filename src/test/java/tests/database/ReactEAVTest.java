package tests.database;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.model.orders.Category;
import projectpackage.model.orders.Order;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.conditions.PriceEqualsToRoomCondition;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Log4j
public class ReactEAVTest extends AbstractDatabaseTest {
    private final String SEPARATOR = "**********************************************************";

    @Autowired
    ReactEAVManager manager;

    //Получить список юзеров
    @Test
    public void queryTestOfUsers(){
        List<User> list = null;
        try {
            list = (List<User>) manager.createReactEAV(User.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
        }
        for (User user:list){
            System.out.println(user);
            assertNotNull(user);
        }
        System.out.println(SEPARATOR);
    }

    //Получить сортированный список юзеров
    @Test
    public void queryTestOfUsersOrderBy(){
        List<User> list = null;
        try {
            list = (List<User>) manager.createReactEAV(User.class).getEntityCollectionOrderByParameter("firstName", true);
        } catch (ResultEntityNullException e) {
        }
        for (User user:list){
            System.out.println(user);
            assertNotNull(user);
        }
        System.out.println(SEPARATOR);
    }

    //Получить одного юзера, ошибка в айди - словить ResultEntityNullException
    @Test
    public void errorQueryTestOfSingleUser(){
        int userId=127;
        User user = null;
        try {
            user = (User) manager.createReactEAV(User.class).getSingleEntityWithId(userId);
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }
        assertNull(user);
        System.out.println(SEPARATOR);
    }

    //Получить одного юзера с вставленной ролью
    @Test
    public void querySingleUserFetchRole(){
        int userId=901;
        User user = null;
        try {
            user = (User) manager.createReactEAV(User.class).fetchRootReference(Role.class, "RoleToUser").closeAllFetches().getSingleEntityWithId(userId);
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }
        assertNotNull(user);
        System.out.println(user);
        System.out.println(SEPARATOR);
    }

    //Получить список телефонов
    @Test
    public void queryTestOfPhones(){
        List<Phone> phones = null;
        try {
            phones = (List<Phone>) manager.createReactEAV(Phone.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
        }
        for (Phone phone:phones){
            System.out.println(phone);
            assertNotNull(phone);
        }
        System.out.println(SEPARATOR);
    }

    //Получить список ролей
    @Test
    public void queryTestOfRoles(){
        List<Role> roles = null;
        try {
            roles = (List<Role>) manager.createReactEAV(Role.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
        }
        for (Role role:roles){
            System.out.println(role);
            assertNotNull(role);
        }
        System.out.println(SEPARATOR);
    }

    //Получить сортированный список ролей
    @Test
    public void queryTestOfRolesOrderBy(){
        List<Role> roles = null;
        try {
            roles = (List<Role>) manager.createReactEAV(Role.class).getEntityCollectionOrderByParameter("objectId", true);
        } catch (ResultEntityNullException e) {
        }
        for (Role role:roles){
            System.out.println(role);
            assertNotNull(role);
        }
        System.out.println(SEPARATOR);
    }

    //Получить одного юзера с вставленным телефоном и ролью
//    @Test
//    public void queryTestOfSingeUserFetchPhonesFetchRoles(){
//        User user = null;
//        try {
//            user = (User) manager.createReactEAV(User.class).fetchInnerEntityCollection(Phone.class).closeFetch().fetchInnerEntityCollection(Role.class).closeFetch().getSingleEntityWithId(901);
//        } catch (ResultEntityNullException e) {
//        }
//        assertNotNull(user);
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
//            assertNotNull(user);
//                for (Phone phone:user.getPhones()){
//                    assertNotNull(phone);
//                }
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
//            assertNotNull(user);
//            Role role = user.getRole();
//            assertNotNull(role);
//        }
//        System.out.println(SEPARATOR);
//    }

    //Получить список юзеров с ролями и телефонами
    @Test
    public void queryTestOfUsersFetchRolesAndPhones(){
        List<User> list = null;
        try {
            list = (List<User>) manager.createReactEAV(User.class).fetchRootReference(Role.class, "RoleToUser").closeAllFetches().fetchRootChild(Phone.class).closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
        }
        for (User user:list){
            System.out.println(user);
            assertNotNull(user);
//            if (user.getObjectId()!=999) {
//                for (Phone phone : user.getPhones()) {
//                    assertNotNull(phone);
//                }
//            }
            Role role = user.getRole();
            assertNotNull(role);
        }
        System.out.println(SEPARATOR);
    }

    //Получить список юзеров с ролями и телефонами(порядок фетча наоборот)
    @Test
    public void queryTestOfUsersFetchRolesAndPhonesBackwards(){
        List<User> list = null;
            try {
                list = (List<User>) manager.createReactEAV(User.class).fetchRootChild(Phone.class).closeAllFetches().fetchRootReference(Role.class, "RoleToUser").closeAllFetches().getEntityCollection();
            } catch (ResultEntityNullException e) {
                e.printStackTrace();
            }
        for (User user:list){
            System.out.println(user);
            assertNotNull(user);
            if (user.getObjectId()!=999) {
                for (Phone phone : user.getPhones()) {
                    assertNotNull(phone);
                }
            }
            Role role = user.getRole();
            assertNotNull(role);
        }
        System.out.println(SEPARATOR);
    }

    @Test
    public void getNotifications(){
        List<Notification> nots = null;
        try {
            nots = manager.createReactEAV(Notification.class).fetchRootReference(User.class, "UserToNotificationAsAuthor").closeAllFetches().fetchRootReference(NotificationType.class, "NotificationTypeToNotification").closeAllFetches().fetchRootReference(User.class, "UserToNotificationAsExecutor").closeAllFetches().fetchRootReference(Order.class, "OrderToNotification").closeAllFetches().getEntityCollection();
            for (Notification not:nots){
                System.out.println(not);
            }
            System.out.println(SEPARATOR);
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getNotifications2(){
        Notification nots = null;
        try {
            nots = (Notification) manager.createReactEAV(Notification.class)
                    .fetchRootReference(User.class, "UserToNotificationAsAuthor")
                    .fetchInnerChild(Phone.class).closeFetch()
                    .fetchInnerReference(Role.class, "RoleToUser").closeAllFetches()
                    .fetchRootReference(NotificationType.class, "NotificationTypeToNotification")
                    .fetchInnerReference(Role.class, "RoleToNotificationType").closeAllFetches()
                    .fetchRootReference(Order.class, "OrderToNotification")
                    .fetchInnerChild(JournalRecord.class).fetchInnerReference(Maintenance.class, "MaintenanceToJournalRecord")
                    .closeFetch().closeFetch()
                    .fetchInnerReference(Room.class, "RoomToOrder")
                    .fetchInnerReference(RoomType.class, "RoomTypeToRoom").closeFetch().closeFetch()
                    .fetchInnerReference(Category.class, "OrderToCategory")
                    .fetchInnerChild(Complimentary.class)
                    .fetchInnerReference(Maintenance.class, "MaintenanceToComplimentary").closeAllFetches()
                    .fetchRootReference(User.class, "UserToNotificationAsExecutor")
                    .fetchInnerChild(Phone.class)
                    .closeAllFetches()
                    .getSingleEntityWithId(1400);
                System.out.println(nots);
            System.out.println(SEPARATOR);
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getRates2(){
        List<Rate> rates = null;
        try {
            rates = manager.createReactEAV(Rate.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }
        for (Rate rate:rates) System.out.println(rate);
    }

    @Test
    public void getRooms(){
        List<Room> rooms = null;
        try {
            rooms = manager.createReactEAV(Room.class).fetchRootReference(RoomType.class, "RoomTypeToRoom").fetchInnerChild(Rate.class).closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }
        for (Room room:rooms){
            System.out.println(room);
        }
    }

    @Test
    public void getRooms1(){
        List<Room> rooms = null;
        try {
            rooms = manager.createReactEAV(Room.class).fetchRootReference(RoomType.class, "RoomTypeToRoom").fetchInnerChild(Rate.class).fetchInnerChild(Price.class).closeAllFetches().addCondition(PriceEqualsToRoomCondition.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }
        for (Room room:rooms){
            System.out.println(room);
        }
        System.out.println(SEPARATOR);

    }

    @Test
    public void getOrders(){
        List<Order> orders = null;
        try {
            orders = manager.createReactEAV(Order.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }
        for (Order order:orders){
            System.out.println(order);
        }
    }

    @Test
    public void getRates(){
        List<Rate> rooms = null;
        try {
            rooms = manager.createReactEAV(Rate.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
            e.printStackTrace();
        }
        for (Rate room:rooms){
            System.out.println(room);
        }
        System.out.println(SEPARATOR);

    }
}
