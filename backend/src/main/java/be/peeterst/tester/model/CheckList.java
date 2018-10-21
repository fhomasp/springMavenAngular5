package be.peeterst.tester.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 29/12/2017
 * Time: 16:09
 */

@Table(name = "checklist")
@Entity
public class CheckList {

    @Id
    @Column(name = "creation_datestamp")
    private Long creationDatestamp;

    @Column
    private String title;

    //todo: vladmihalcea idea but use jooq (remove the oneToMany relation -- oneToFew should have been better)
    @OneToMany(mappedBy = "checkList", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<ChecklistItem> items = new ArrayList<>();


    @OneToOne(cascade = {CascadeType.ALL})
    @JsonManagedReference
    private CalendarItem calendarItem;

    /**
     * TODO: When this model object is created through json mapping from Spring, there's no constructor call when this object arrives through a REST call?!
     */
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

    public void setCreationDatestamp(Long creationDatestamp) {
        this.creationDatestamp = creationDatestamp;
    }

    public List<ChecklistItem> getItems() {
        return items;
    }

//    public void setItems(List<ChecklistItem> items) {
//        this.items = items;
//    }

    public CalendarItem getCalendarItem() {
        if(this.calendarItem == null) this.calendarItem = new CalendarItem();
        return calendarItem;
    }

    public void addItem(ChecklistItem item){
        this.items.add(item);
        item.setCheckList(this);
    }

    public void removeItem(ChecklistItem item){
        this.items.remove(item);
        item.setCheckList(null);
    }

    public void setCalendarItem(CalendarItem calendarItem) {
        this.calendarItem = calendarItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckList checkList = (CheckList) o;
        return this.creationDatestamp.equals(checkList.creationDatestamp);
    }

    @Override
    public int hashCode() {
        return creationDatestamp.hashCode();
    }

    @Override
    public String toString() {
        return "CheckList {" +
                "creationDatestamp = " + creationDatestamp +
                ", title = '" + title + '\'' +
                ", items = " + items +
                ", calendarItem = " + calendarItem +
                '}';
    }
}
