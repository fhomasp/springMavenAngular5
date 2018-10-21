package be.peeterst.tester.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 29/12/2017
 * Time: 16:17
 */
@Entity
public class CalendarItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int calendarId;

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @OneToOne(mappedBy = "calendarItem",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonBackReference
    private CheckList checkList;


    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public CheckList getCheckList() {
        return checkList;
    }

    public void setCheckList(CheckList checkList) {
        this.checkList = checkList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarItem that = (CalendarItem) o;

        return this.calendarId == that.calendarId;
    }

    @Override
    public int hashCode() {
        return 99 * calendarId;
    }

    @Override
    public String toString() {
        return "CalendarItem{" +
                "calendarId=" + calendarId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
