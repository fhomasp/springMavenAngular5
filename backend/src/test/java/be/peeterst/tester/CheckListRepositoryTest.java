package be.peeterst.tester;

import be.peeterst.tester.builder.CalendarItemBuilder;
import be.peeterst.tester.builder.CheckListBuilder;
import be.peeterst.tester.builder.ChecklistItemBuilder;
import be.peeterst.tester.model.CalendarItem;
import be.peeterst.tester.model.CheckList;
import be.peeterst.tester.repository.CheckListRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static be.peeterst.tester.CheckListTestUtils.createOneChecklist;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 2/10/2018
 * Time: 21:22
 */


@Transactional("transactionManager")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/cleanup.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CheckListRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CheckListRepository checklistRepository;


    @Test
    public void getByCreationDateTest(){

        Date calendarStartDate = DateUtils.truncate(Date.from(Instant.now()),Calendar.SECOND);
        Date calendarEndDate = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(1))),Calendar.SECOND);
        CheckList checkListToPersist = createOneChecklist(calendarStartDate, calendarEndDate);
        testEntityManager.persistFlushFind(checkListToPersist);

        CheckList byCreationDatestamp = checklistRepository.findByCreationDatestamp(checkListToPersist.getCreationDatestamp());
        assertThat(byCreationDatestamp.getTitle()).isEqualTo("Work");
        CalendarItem calendarItem = byCreationDatestamp.getCalendarItem();
        assertThat(calendarItem).isNotNull();

        assertThat(byCreationDatestamp).isEqualToComparingFieldByFieldRecursively(checkListToPersist);

    }


    @Test
    public void updateCheckListTest() {

        Date calendarStartDate = DateUtils.truncate(Date.from(Instant.now()),Calendar.SECOND);
        Date calendarEndDate = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(1))),Calendar.SECOND);
        CheckList checkListToPersist = createOneChecklist(calendarStartDate, calendarEndDate);
        testEntityManager.persistFlushFind(checkListToPersist);

        CheckList checkListToUpdate = checklistRepository.findByCreationDatestamp(checkListToPersist.getCreationDatestamp());

        Date updatedStartDate = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(1))), Calendar.SECOND);
        checkListToUpdate.getCalendarItem().setStartDate(updatedStartDate);
        checkListToUpdate.getItems().remove(0);

        checklistRepository.save(checkListToUpdate);

        CheckList updatedCheckList = checklistRepository.findByCreationDatestamp(checkListToPersist.getCreationDatestamp());
        assertThat(updatedCheckList).isEqualToComparingFieldByFieldRecursively(checkListToUpdate);

    }

    @Test
    public void getAllTest() throws InterruptedException {

        Date calendarStartDate = DateUtils.truncate(Date.from(Instant.now()),Calendar.SECOND);
        Date calendarEndDate = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(1))),Calendar.SECOND);
        Date calendarStartDate2 = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(3))),Calendar.SECOND);
        Date calendarEndDate2 = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(10))),Calendar.SECOND);
        CheckList checkListToPersist = CheckListBuilder.aCheckList()
                .withTitle("Work")
                .withCreationDateStamp(System.currentTimeMillis())
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withTitle("CalendarTitle")
                        .withStartDate(calendarStartDate)
                        .withEndDate(calendarEndDate)
                        .withDescription("calendarDescription")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(true)
                        .withBulletName("bulletName1")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(false)
                        .withBulletName("bulletName2")
                        .build())
                .build();
        Thread.sleep(1);
        CheckList checkListToPersist2 = CheckListBuilder.aCheckList()
                .withTitle("Work")
                .withCreationDateStamp(System.currentTimeMillis())
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withTitle("CalendarTitle2")
                        .withStartDate(calendarStartDate2)
                        .withEndDate(calendarEndDate2)
                        .withDescription("calendarDescription2")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(true)
                        .withBulletName("bulletName3")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(false)
                        .withBulletName("bulletName4")
                        .build())
                .build();
        Thread.sleep(1);
        CheckList checkListToPersist3 = CheckListBuilder.aCheckList()
                .withTitle("WorkNot")
                .withCreationDateStamp(System.currentTimeMillis())
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withTitle("CalendarTitle3")
                        .withStartDate(calendarStartDate2)
                        .withEndDate(calendarEndDate2)
                        .withDescription("calendarDescription3")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(true)
                        .withBulletName("bulletName5")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(false)
                        .withBulletName("bulletName6")
                        .build())
                .build();
        testEntityManager.persistFlushFind(checkListToPersist);
        testEntityManager.persistFlushFind(checkListToPersist2);
        testEntityManager.persistFlushFind(checkListToPersist3);

        Iterable<CheckList> foundChecklists = checklistRepository.findAll();

        Iterator<CheckList> iterator = foundChecklists.iterator();
        CheckList one = iterator.next();
        assertThat(one).isEqualToComparingFieldByFieldRecursively(checkListToPersist);
        CheckList two = iterator.next();
        assertThat(two).isEqualToComparingFieldByFieldRecursively(checkListToPersist2);
        CheckList three = iterator.next();
        assertThat(three).isEqualToComparingFieldByFieldRecursively(checkListToPersist3);
    }

    @Test
    public void deleteTest() throws InterruptedException {

        Date calendarStartDate = DateUtils.truncate(Date.from(Instant.now()),Calendar.SECOND);
        Date calendarEndDate = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(1))),Calendar.SECOND);
        Date calendarStartDate2 = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(3))),Calendar.SECOND);
        Date calendarEndDate2 = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(10))),Calendar.SECOND);
        CheckList checkListToPersist = CheckListBuilder.aCheckList()
                .withTitle("Work")
                .withCreationDateStamp(System.currentTimeMillis())
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withTitle("CalendarTitle")
                        .withStartDate(calendarStartDate)
                        .withEndDate(calendarEndDate)
                        .withDescription("calendarDescription")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(true)
                        .withBulletName("bulletName1")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(false)
                        .withBulletName("bulletName2")
                        .build())
                .build();
        Thread.sleep(1);
        CheckList checkListToPersist2 = CheckListBuilder.aCheckList()
                .withTitle("Work")
                .withCreationDateStamp(System.currentTimeMillis())
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withTitle("CalendarTitle2")
                        .withStartDate(calendarStartDate2)
                        .withEndDate(calendarEndDate2)
                        .withDescription("calendarDescription2")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(true)
                        .withBulletName("bulletName3")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(false)
                        .withBulletName("bulletName4")
                        .build())
                .build();
        Thread.sleep(1);
        CheckList checkListToPersist3 = CheckListBuilder.aCheckList()
                .withTitle("WorkNot")
                .withCreationDateStamp(System.currentTimeMillis())
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withTitle("CalendarTitle3")
                        .withStartDate(calendarStartDate2)
                        .withEndDate(calendarEndDate2)
                        .withDescription("calendarDescription3")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(true)
                        .withBulletName("bulletName5")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(false)
                        .withBulletName("bulletName6")
                        .build())
                .build();
        testEntityManager.persistFlushFind(checkListToPersist);
        testEntityManager.persistFlushFind(checkListToPersist2);
        testEntityManager.persistFlushFind(checkListToPersist3);

        checklistRepository.delete(checkListToPersist2);

        Iterable<CheckList> foundChecklists = checklistRepository.findAll();
        List<CheckList> checkLists = new ArrayList<>();
        foundChecklists.forEach(checkLists::add);
        assertThat(checkLists.size()).isEqualTo(2);

    }

    @Test
    public void findByTitleTest() throws InterruptedException {

        Date calendarStartDate = DateUtils.truncate(Date.from(Instant.now()),Calendar.SECOND);
        Date calendarEndDate = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(1))),Calendar.SECOND);
        Date calendarStartDate2 = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(3))),Calendar.SECOND);
        Date calendarEndDate2 = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(10))),Calendar.SECOND);
        CheckList checkListToPersist = CheckListBuilder.aCheckList()
                .withTitle("Work")
                .withCreationDateStamp(System.currentTimeMillis())
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withTitle("CalendarTitle")
                        .withStartDate(calendarStartDate)
                        .withEndDate(calendarEndDate)
                        .withDescription("calendarDescription")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(true)
                        .withBulletName("bulletName1")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(false)
                        .withBulletName("bulletName2")
                        .build())
                .build();
        Thread.sleep(1);
        CheckList checkListToPersist2 = CheckListBuilder.aCheckList()
                .withTitle("Work")
                .withCreationDateStamp(System.currentTimeMillis())
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withTitle("CalendarTitle2")
                        .withStartDate(calendarStartDate2)
                        .withEndDate(calendarEndDate2)
                        .withDescription("calendarDescription2")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(true)
                        .withBulletName("bulletName3")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(false)
                        .withBulletName("bulletName4")
                        .build())
                .build();
        Thread.sleep(1);
        CheckList checkListToPersist3 = CheckListBuilder.aCheckList()
                .withTitle("WorkNot")
                .withCreationDateStamp(System.currentTimeMillis())
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withTitle("CalendarTitle3")
                        .withStartDate(calendarStartDate2)
                        .withEndDate(calendarEndDate2)
                        .withDescription("calendarDescription3")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(true)
                        .withBulletName("bulletName5")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withCheck(false)
                        .withBulletName("bulletName6")
                        .build())
                .build();
        testEntityManager.persistFlushFind(checkListToPersist);
        testEntityManager.persistFlushFind(checkListToPersist2);
        testEntityManager.persistFlushFind(checkListToPersist3);

        List<CheckList> foundChecklists = checklistRepository.findByTitle("work");

        assertThat(foundChecklists.size()).isEqualTo(2);
        Iterator<CheckList> iterator = foundChecklists.iterator();
        CheckList one = iterator.next();
        assertThat(one).isEqualToComparingFieldByFieldRecursively(checkListToPersist);
        CheckList two = iterator.next();
        assertThat(two).isEqualToComparingFieldByFieldRecursively(checkListToPersist2);
    }




}
