package be.peeterst.tester.builder;

import be.peeterst.tester.model.CheckList;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 29/12/2017
 * Time: 16:20
 */
public class CheckListBuilder {

    private CheckList checkList;

    private CheckListBuilder() {
        this.checkList = new CheckList();
    }

    public static CheckListBuilder aCheckList() {
        return new CheckListBuilder();
    }

    public CheckList build() {
        return this.checkList;
    }

    public CheckListBuilder withTitle(String title) {
        this.checkList.setTitle(title);
        return this;
    }

    public CheckListBuilder withTargetDateStamp(long timestamp) {
        this.checkList.setTargetDatestamp(timestamp);
        return this;
    }
}
