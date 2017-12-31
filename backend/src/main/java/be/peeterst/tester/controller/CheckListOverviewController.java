package be.peeterst.tester.controller;

import be.peeterst.tester.builder.CheckListBuilder;
import be.peeterst.tester.model.CheckList;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 29/12/2017
 * Time: 16:04
 */

@RestController
@RequestMapping(value = "/overview")
public class CheckListOverviewController {

    private List<CheckList> checkLists = new ArrayList<>();

    public CheckListOverviewController() {
        this.checkLists = buildChecklists();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CheckList> getCheckLists() {
        return checkLists;
    }

    @RequestMapping(value = "/{title}",method = RequestMethod.GET)
    public CheckList getCheckList(@PathVariable("title") String title){
        //todo: Should be List: not unique (use ID for that)
        return this.checkLists.stream().filter(checklist -> checklist.getTitle().equals(title)).findFirst().orElse(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    public CheckList saveCheckList(@RequestBody CheckList checkList){
         this.checkLists.add(checkList);
         return checkList;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public CheckList updateCheckList(@RequestBody CheckList checkList){
        CheckList checkListToModify = this.checkLists.stream().filter(checkListFromList ->
                checkListFromList.getCreationDatestamp().equals(checkList.getCreationDatestamp())).findFirst().orElse(null);
        if(checkListToModify != null){
            checkListToModify.setTitle(checkList.getTitle());
            checkListToModify.setTargetDatestamp(checkList.getTargetDatestamp());
            return checkListToModify;
        }
        return null;
    }

    @RequestMapping(value = "/{title}",method = RequestMethod.DELETE)
    public int deleteCheckList(@PathVariable String title){
        List<CheckList> found = this.checkLists.stream().filter(checkList -> checkList.getTitle().equals(title)).collect(Collectors.toList());
        if(found == null || found.size() == 0) return 0;
        for (CheckList checkList : found) {
            this.checkLists.remove(checkList);
        }
        return found.size();
    }

    public void setCheckLists(List<CheckList> checkLists) {
        this.checkLists = checkLists;
    }

    private List<CheckList> buildChecklists() {
        List<CheckList> builtChecklists = new ArrayList<>();
        ZoneId zoneId = ZoneId.systemDefault();
        builtChecklists.add(CheckListBuilder.aCheckList()
                .withTitle("Work")
                .withTargetDateStamp(LocalDate.of(2017,1,5).atStartOfDay(zoneId).toEpochSecond())
                .build());
        builtChecklists.add(CheckListBuilder.aCheckList()
                .withTitle("Play")
                .withTargetDateStamp(LocalDate.of(2017,2,6).atStartOfDay(zoneId).toEpochSecond())
                .build());
        builtChecklists.add(CheckListBuilder.aCheckList()
                .withTitle("Sleep")
                .withTargetDateStamp(LocalDate.of(2017,3,31).atStartOfDay(zoneId).toEpochSecond())
                .build());

        return builtChecklists;
    }
}
