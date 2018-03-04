package be.peeterst.tester.builder;

import be.peeterst.tester.model.ChecklistItem;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 4/03/2018
 * Time: 17:22
 */
public class ChecklistItemBuilder {

    private ChecklistItem checklistItem;

    private ChecklistItemBuilder() {
        checklistItem = new ChecklistItem();
    }

    public static ChecklistItemBuilder aChecklistItem() {
        return new ChecklistItemBuilder();
    }

    public ChecklistItem build() {
        return this.checklistItem;
    }

    public ChecklistItemBuilder withBulletName(String bulletName) {
        this.checklistItem.setBulletName(bulletName);
        return this;
    }

    public ChecklistItemBuilder withCheck(boolean checked) {
        this.checklistItem.setTaken(checked);
        return this;
    }
}
