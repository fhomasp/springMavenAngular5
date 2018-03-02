package be.peeterst.tester.builder;

import be.peeterst.tester.model.CalendarItem;
import be.peeterst.tester.model.CheckList;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 2/03/2018
 * Time: 12:42
 */
public class CalendarItemBuilder {


    private final CalendarItem calendarItem;

    private CalendarItemBuilder(){
        this.calendarItem = new CalendarItem();
    }

    public static CalendarItemBuilder aCalendarItem() {
        return new CalendarItemBuilder();
    }


    public CalendarItem build() {
        return this.calendarItem;
    }

    public CalendarItemBuilder withStartDate(Date startDate) {
        this.calendarItem.setStartDate(startDate);
        return this;
    }

    public CalendarItemBuilder withEndDate(Date endDate) {
        this.calendarItem.setEndDate(endDate);
        return this;
    }

    public CalendarItemBuilder withTitle(String title) {
        this.calendarItem.setTitle(title);
        return this;
    }

    public CalendarItemBuilder withDescription(String calendarDescription) {
        this.calendarItem.setDescription(calendarDescription);
        return this;
    }
}
