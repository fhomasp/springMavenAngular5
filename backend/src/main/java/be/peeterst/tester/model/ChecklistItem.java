package be.peeterst.tester.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 29/12/2017
 * Time: 16:15
 */

@Entity
public class ChecklistItem {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;

    @Column(name = "bullet_name")
    private String bulletName;

    @Column
    private boolean taken;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CheckList_id")
    @JsonBackReference
    private CheckList checkList;

    public String getBulletName() {
        return bulletName;
    }

    public void setBulletName(String bulletName) {
        this.bulletName = bulletName;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
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

        ChecklistItem that = (ChecklistItem) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return 66 * id;
    }
}
