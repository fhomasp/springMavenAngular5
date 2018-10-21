package be.peeterst.tester;

import be.peeterst.tester.builder.CalendarItemBuilder;
import be.peeterst.tester.builder.CheckListBuilder;
import be.peeterst.tester.builder.ChecklistItemBuilder;
import be.peeterst.tester.model.CheckList;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 11/10/2018
 * Time: 22:09
 */
public class CheckListTestUtils {

    public static CheckList createOneChecklist(Date calendarStartDate, Date calendarEndDate) {
        return CheckListBuilder.aCheckList()
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
    }

}
