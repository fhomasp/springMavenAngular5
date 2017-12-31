package be.peeterst.tester.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 29/12/2017
 * Time: 16:09
 */
public class CheckList {

    private Long creationDatestamp;

    private String title;

    private long targetDatestamp;

    private List<ChecklistItem> items;

    private CalendarItem calendarItem;

    public CheckList() {
        if(this.creationDatestamp == null || this.creationDatestamp == 0L){
            this.creationDatestamp = System.currentTimeMillis();
        }
    }

    public Long getCreationDatestamp() {
        return creationDatestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTargetDatestamp() {
        return targetDatestamp;
    }

    public void setTargetDatestamp(long targetDatestamp) {
        this.targetDatestamp = targetDatestamp;
    }

    public List<ChecklistItem> getItems() {
        return items;
    }

    public void setItems(List<ChecklistItem> items) {
        this.items = items;
    }

    public CalendarItem getCalendarItem() {
        return calendarItem;
    }

    public void setCalendarItem(CalendarItem calendarItem) {
        this.calendarItem = calendarItem;
    }

    @Override
    public String toString() {
        return "CheckList {" +
                "creationDatestamp = " + creationDatestamp +
                ", title = '" + title + '\'' +
                ", targetDatestamp = " + targetDatestamp +
                ", items = " + items +
                ", calendarItem = " + calendarItem +
                '}';
    }
}
