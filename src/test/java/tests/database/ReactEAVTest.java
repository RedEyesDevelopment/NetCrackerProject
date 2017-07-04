package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
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
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.PriceEqualsToRoomCondition;
import projectpackage.repository.reacteav.conditions.StringWhereCondition;
import projectpackage.repository.reacteav.conditions.VariableWhereCondition;
import projectpackage.repository.support.ParentsDAO;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Log4j
public class ReactEAVTest extends AbstractDatabaseTest {

    private static final Logger LOGGER = Logger.getLogger(ReactEAVTest.class);

    private final String SEPARATOR = "**********************************************************";

    @Autowired
    ReactEAVManager manager;

    @Autowired
    ParentsDAO parentsDAO;

    //Получить список юзеров
    @Test
    public void queryTestOfUsers(){
        List<User> list = (List<User>) manager.createReactEAV(User.class).getEntityCollection();
        for (User user:list){
            LOGGER.info(user);
            assertNotNull(user);
        }
        LOGGER.info(SEPARATOR);
    }

    //Получить сортированный список юзеров
    @Test
    public void queryTestOfUsersOrderBy(){
        List<User> list = (List<User>) manager.createReactEAV(User.class).getEntityCollectionOrderByParameter("firstName", true);

        for (User user:list){
            LOGGER.info(user);
            assertNotNull(user);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    public void errorQueryTestOfSingleUser(){
        int userId=127;
        User user = (User) manager.createReactEAV(User.class).getSingleEntityWithId(userId);

        assertNull(user);
        LOGGER.info(SEPARATOR);
    }

    //Получить одного юзера с вставленной ролью
    @Test
    public void querySingleUserFetchRole(){
        int userId=901;
        User user = (User) manager.createReactEAV(User.class).fetchRootReference(Role.class, "RoleToUser").closeAllFetches().getSingleEntityWithId(userId);

        assertNotNull(user);
        LOGGER.info(user);
        LOGGER.info(SEPARATOR);
    }

    //Получить список телефонов
    @Test
    public void queryTestOfPhones(){
        List<Phone> phones = (List<Phone>) manager.createReactEAV(Phone.class).getEntityCollection();

        for (Phone phone:phones){
            LOGGER.info(phone);
            assertNotNull(phone);
        }
        LOGGER.info(SEPARATOR);
    }

    //Получить список ролей
    @Test
    public void queryTestOfRoles(){
        List<Role> roles = (List<Role>) manager.createReactEAV(Role.class).getEntityCollection();

        for (Role role:roles){
            LOGGER.info(role);
            assertNotNull(role);
        }
        LOGGER.info(SEPARATOR);
    }

    //Получить сортированный список ролей
    @Test
    public void queryTestOfRolesOrderBy(){
        List<Role> roles = manager.createReactEAV(Role.class).getEntityCollectionOrderByParameter("objectId", true);
        for (Role role:roles){
            LOGGER.info(role);
            assertNotNull(role);
        }
        LOGGER.info(SEPARATOR);
    }

    //Получить одного юзера с вставленным телефоном и ролью
    @Test
    public void queryTestOfSingeUserFetchPhonesFetchRoles(){
        User user = (User) manager.createReactEAV(User.class).fetchRootChild(Phone.class).closeAllFetches().fetchRootReference(Role.class, "RoleToUser").closeAllFetches().getSingleEntityWithId(901);

        assertNotNull(user);
        LOGGER.info(user.toString());
        LOGGER.info(SEPARATOR);
    }
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
//            LOGGER.info(user);
//            assertNotNull(user);
//                for (Phone phone:user.getPhones()){
//                    assertNotNull(phone);
//                }
//        }
//        LOGGER.info(SEPARATOR);
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
//            LOGGER.info(user);
//            assertNotNull(user);
//            Role role = user.getRole();
//            assertNotNull(role);
//        }
//        LOGGER.info(SEPARATOR);
//    }

    //Получить список юзеров с ролями и телефонами
    @Test
    public void queryTestOfUsersFetchRolesAndPhones(){
        List<User> list = (List<User>) manager.createReactEAV(User.class).fetchRootReference(Role.class, "RoleToUser").closeAllFetches().fetchRootChild(Phone.class).closeAllFetches().getEntityCollection();

        for (User user:list){
            LOGGER.info(user);
            assertNotNull(user);
//            if (user.getObjectId()!=999) {
//                for (Phone phone : user.getPhones()) {
//                    assertNotNull(phone);
//                }
//            }
            Role role = user.getRole();
            assertNotNull(role);
        }
        LOGGER.info(SEPARATOR);
    }

    //Получить список юзеров с ролями и телефонами(порядок фетча наоборот)
    @Test
    public void queryTestOfUsersFetchRolesAndPhonesBackwards(){
        List<User> list = (List<User>) manager.createReactEAV(User.class).fetchRootChild(Phone.class).closeAllFetches().fetchRootReference(Role.class, "RoleToUser").closeAllFetches().getEntityCollection();

        for (User user:list){
            LOGGER.info(user);
            assertNotNull(user);
            Role role = user.getRole();
            assertNotNull(role);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    public void getNotifications(){
        List<Notification> nots = manager.createReactEAV(Notification.class).fetchRootReference(User.class, "UserToNotificationAsAuthor").closeAllFetches().fetchRootReference(NotificationType.class, "NotificationTypeToNotification").closeAllFetches().fetchRootReference(User.class, "UserToNotificationAsExecutor").closeAllFetches().fetchRootReference(Order.class, "OrderToNotification").closeAllFetches().getEntityCollection();
            for (Notification not:nots){
                LOGGER.info(not);
            }
            LOGGER.info(SEPARATOR);
    }

    @Test
    public void getNotificationsWithUserRole(){
        List<Notification> nots = manager.createReactEAV(Notification.class).fetchRootReference(User.class, "UserToNotificationAsAuthor").fetchInnerReference(Role.class, "RoleToUser").closeAllFetches().fetchRootReference(NotificationType.class, "NotificationTypeToNotification").closeAllFetches().fetchRootReference(User.class, "UserToNotificationAsExecutor").closeAllFetches().fetchRootReference(Order.class, "OrderToNotification").closeAllFetches().getEntityCollection();
            for (Notification not:nots){
                LOGGER.info(not);
            }
            LOGGER.info(SEPARATOR);

    }

    @Test
    public void getNotifications2(){
        Notification nots = null;
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
                    .fetchInnerReference(Category.class, "CategoryToOrder")
                    .fetchInnerChild(Complimentary.class)
                    .fetchInnerReference(Maintenance.class, "MaintenanceToComplimentary").closeAllFetches()
                    .fetchRootReference(User.class, "UserToNotificationAsExecutor")
                    .fetchInnerChild(Phone.class)
                    .closeAllFetches()
                    .getSingleEntityWithId(1400);
                LOGGER.info(nots);
            LOGGER.info(SEPARATOR);
    }

    @Test
    public void getRates2(){
        List<Rate> rates = manager.createReactEAV(Rate.class).getEntityCollection();
        for (Rate rate:rates) LOGGER.info(rate);
    }

    @Test
    public void getRooms(){
        List<Room> rooms = manager.createReactEAV(Room.class).fetchRootReference(RoomType.class, "RoomTypeToRoom").fetchInnerChild(Rate.class).closeAllFetches().getEntityCollection();

        for (Room room:rooms){
            LOGGER.info(room);
        }
    }

    @Test
    public void getRooms1(){
        List<Room> rooms = manager.createReactEAV(Room.class).addCondition(new PriceEqualsToRoomCondition(), ConditionExecutionMoment.AFTER_QUERY).fetchRootReference(RoomType.class, "RoomTypeToRoom").fetchInnerChild(Rate.class).fetchInnerChild(Price.class).closeAllFetches().getEntityCollection();

        for (Room room:rooms){
            LOGGER.info(room);
        }
        LOGGER.info(SEPARATOR);

    }

    @Test
    public void getOrders(){
        List<Order> orders = manager.createReactEAV(Order.class).getEntityCollection();

        for (Order order:orders){
            LOGGER.info(order);
        }
    }

    @Test
    public void getRates(){
        List<Rate> rooms = manager.createReactEAV(Rate.class).getEntityCollection();

        for (Rate room:rooms){
            LOGGER.info(room);
        }
        LOGGER.info(SEPARATOR);

    }

    @Test
    public void getUsersWithCondition(){
        List<User> users = manager.createReactEAV(User.class).addCondition(new StringWhereCondition("ROOTABLE.OBJECT_ID=901"), ConditionExecutionMoment.AFTER_APPENDING_WHERE).getEntityCollection();

        for (User user:users){
            LOGGER.info(user);
        }
        LOGGER.info(SEPARATOR);

    }

    @Test
    public void getUsersWithVariablesCondition(){
        List<User> users = manager.createReactEAV(User.class).addCondition(new VariableWhereCondition("email", "stephenking@mail.ru"), ConditionExecutionMoment.AFTER_APPENDING_WHERE).getEntityCollection();

        for (User user:users){
            LOGGER.info(user);
        }
        LOGGER.info(SEPARATOR);

    }

    @Test
    public void getUsersWithReferenceCondition(){
        List<User> users = manager.createReactEAV(User.class).addCondition(new StringWhereCondition("R_REFOB1.REFERENCE=3"), ConditionExecutionMoment.AFTER_APPENDING_WHERE).fetchRootReference(Role.class, "RoleToUser").addCondition(new VariableWhereCondition("roleName", "CLIENT"), ConditionExecutionMoment.AFTER_APPENDING_WHERE).closeAllFetches().getEntityCollection();

        for (User user:users){
            LOGGER.info(user);
        }
        LOGGER.info(SEPARATOR);

    }

    @Test
    public void getParentId(){
        Integer parentId = parentsDAO.getParentId(1101);
        LOGGER.info("PARENTID="+parentId);
        LOGGER.info(SEPARATOR);
    }

    @Test
    public void getUserForPhone(){
        Integer parentId = parentsDAO.getParentId(1101);
        User user = (User) manager.createReactEAV(User.class).fetchRootChild(Phone.class).closeAllFetches().getSingleEntityWithId(parentId);

        LOGGER.info(user);
        LOGGER.info(SEPARATOR);
    }


    @Test
    public void getNotExecutedNotifications(){
        List<Notification> nots = manager.createReactEAV(Notification.class).addCondition(new StringWhereCondition("ATTRS3.DATE_VALUE IS NULL"), ConditionExecutionMoment.AFTER_APPENDING_WHERE).fetchRootReference(NotificationType.class, "NotificationTypeToNotification").fetchInnerReference(Role.class, "RoleToNotificationType").closeAllFetches().getEntityCollection();
        LOGGER.info(SEPARATOR);
        for (Notification not: nots){
            LOGGER.info(not);
        }
    }

    @Test
    public void getAllNotifications(){
        List<Notification> nots = manager.createReactEAV(Notification.class).fetchRootReference(NotificationType.class, "NotificationTypeToNotification").fetchInnerReference(Role.class, "RoleToNotificationType").closeAllFetches().getEntityCollection();
        LOGGER.info(SEPARATOR);
        for (Notification not: nots){
            LOGGER.info(not);
        }
    }
}
