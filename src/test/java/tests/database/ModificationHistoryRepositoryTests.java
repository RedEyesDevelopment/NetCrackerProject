package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.User;
import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.dto.IUDAnswer;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.service.orderservice.ModificationHistoryService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
public class ModificationHistoryRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(ModificationHistoryRepositoryTests.class);

    @Autowired
    ModificationHistoryService modificationHistoryService;

    @Test
    @Rollback(true)
    public void crudModificationHistoryTest() {
        Room room = new Room();
        room.setObjectId(127);
        User user = new User();
        user.setObjectId(900);
        Order order = new Order();
        order.setObjectId(300);
        order.setRegistrationDate(new Date());
        order.setIsPaidFor(false);
        order.setIsConfirmed(false);
        order.setLivingStartDate(new Date());
        order.setLivingFinishDate(new Date());
        order.setSum(7478L);
        order.setComment("Comment");
        order.setRoom(room);
        order.setClient(user);
        order.setLastModificator(user);


        Room room1 = new Room();
        room.setObjectId(128);
        User user1 = new User();
        user1.setObjectId(901);
        Order order1 = new Order();
        order1.setObjectId(300);
        order1.setRegistrationDate(new Date());
        order1.setIsPaidFor(true);
        order1.setIsConfirmed(true);
        order1.setLivingStartDate(new Date());
        order1.setLivingFinishDate(new Date());
        order1.setSum(10000L);
        order1.setComment("new Comment");
        order1.setRoom(room1);
        order1.setClient(user1);
        order1.setLastModificator(user1);

        IUDAnswer insertAnswer = modificationHistoryService.insertModificationHistory(order1, order);
        assertTrue(insertAnswer.isSuccessful());
        LOGGER.info("Update modificationHistory result = " + insertAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        int modificationHistoryId = insertAnswer.getObjectId();
        IUDAnswer deleteAnswer = modificationHistoryService.deleteModificationHistory(modificationHistoryId);
        assertTrue(deleteAnswer.isSuccessful());
        LOGGER.info("Delete modificationHistory result = " + deleteAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        ModificationHistory deleteModificationHistory = modificationHistoryService.getSingleModificationHistoryById(modificationHistoryId);
        assertNull(deleteModificationHistory);
    }

    @Test
    @Rollback(true)
    public void getAllModificationHistory() {
        List<ModificationHistory> modificationHistories = modificationHistoryService.getAllModificationHistory();
        for (ModificationHistory m : modificationHistories) {
            LOGGER.info(m);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getAllModificationHistoryByOrder(){

    }

    @Test
    @Rollback(true)
    public void getSingleModificationHistoryById(){
        ModificationHistory modificationHistory = modificationHistoryService.getSingleModificationHistoryById(2181);
        LOGGER.info(modificationHistory);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void deleteModificationHistory() throws ReferenceBreakException {
        int modificationHistoryId = 2058;
        IUDAnswer iudAnswer = modificationHistoryService.deleteModificationHistory(modificationHistoryId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete modificationHistory result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void insertModificationHistory() {
        Room room = new Room();
        room.setObjectId(127);
        User user = new User();
        user.setObjectId(900);
        Order order = new Order();
        order.setObjectId(300);
        order.setRegistrationDate(new Date());
        order.setIsPaidFor(false);
        order.setIsConfirmed(false);
        order.setLivingStartDate(new Date());
        order.setLivingFinishDate(new Date());
        order.setSum(7478L);
        order.setComment("Comment");
        order.setRoom(room);
        order.setClient(user);
        order.setLastModificator(user);


        Room room1 = new Room();
        room.setObjectId(128);
        User user1 = new User();
        user1.setObjectId(901);
        Order order1 = new Order();
        order1.setObjectId(300);
        order1.setRegistrationDate(new Date());
        order1.setIsPaidFor(true);
        order1.setIsConfirmed(true);
        order1.setLivingStartDate(new Date());
        order1.setLivingFinishDate(new Date());
        order1.setSum(10000L);
        order1.setComment("new Comment");
        order1.setRoom(room1);
        order1.setClient(user1);
        order1.setLastModificator(user1);

        IUDAnswer iudAnswer = modificationHistoryService.insertModificationHistory(order1, order);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update modificationHistory result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }


}
