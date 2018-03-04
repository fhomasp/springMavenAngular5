package be.peeterst.tester.controller;

import be.peeterst.tester.builder.CalendarItemBuilder;
import be.peeterst.tester.builder.CheckListBuilder;
import be.peeterst.tester.builder.ChecklistItemBuilder;
import be.peeterst.tester.model.CheckList;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 29/12/2017
 * Time: 16:04
 * TODO: Integration tests
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

    @RequestMapping(value = "/find/{title}",method = RequestMethod.GET)
    public List<CheckList> getCheckList(@PathVariable("title") String title){
        return this.checkLists.stream().filter(checklist -> checklist.getTitle().contains(title)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{creationDatestamp}",method = RequestMethod.GET)
    public CheckList getCheckList(@PathVariable("creationDatestamp") long creationDateStamp){
        return this.checkLists.stream().filter(checklist -> checklist.getCreationDatestamp().equals(creationDateStamp)).findFirst().orElse(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    public CheckList saveCheckList(@RequestBody CheckList checkList){
        //todo: only for saving, not updating: dao(service) should be responsible for this
        if(checkList.getCreationDatestamp() != null){
            throw new IllegalStateException("creation date at checklist save should be null");
        }
        checkList.setCreationDatestamp(new Date().getTime());

        this.checkLists.add(checkList);
        return checkList;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public CheckList updateCheckList(@RequestBody CheckList checkList){
        CheckList checkListToModify = this.checkLists.stream().filter(checkListFromList ->
                checkListFromList.getCreationDatestamp().equals(checkList.getCreationDatestamp())).findFirst().orElse(null);
        if(checkListToModify != null){
            checkListToModify.setTitle(checkList.getTitle());
            checkListToModify.getCalendarItem().setStartDate(checkList.getCalendarItem().getStartDate());
            return checkListToModify;
        }
        return null;
    }

    @RequestMapping(value = "/{creationDatestamp}",method = RequestMethod.DELETE)
    public int deleteCheckList(@PathVariable long creationDatestamp){
        CheckList found = this.checkLists.stream().filter(checkList -> checkList.getCreationDatestamp() == creationDatestamp)
                .findFirst().orElse(null);
        if(found == null) return 0;
            this.checkLists.remove(found);
        return 1;
    }

    public void setCheckLists(List<CheckList> checkLists) {
        this.checkLists = checkLists;
    }

    private List<CheckList> buildChecklists() {
        List<CheckList> builtChecklists = new ArrayList<>();
        ZoneId zoneId = ZoneId.systemDefault();
        
//        Date d = new Date();

        builtChecklists.add(CheckListBuilder.aCheckList()
                .withTitle("Work")
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withStartDate(Date.from(LocalDate.of(2017,1,5).atTime(8,17).atZone(zoneId).toInstant()))
                        .withEndDate(Date.from(LocalDate.of(2017,1,5).atTime(9,37).toInstant(ZoneOffset.MAX)))
                        .withTitle("caltitle1")
                        .withDescription("caldescription1")
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withBulletName("bullet 1")
                        .withCheck(true)
                        .build())
                .build());
        waitATinyMoment();
        builtChecklists.add(CheckListBuilder.aCheckList()
                .withTitle("Play")
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withStartDate(Date.from(LocalDate.of(2017,2,6).atStartOfDay(zoneId).toInstant()))
                        .withEndDate(Date.from(LocalDate.of(2017,2,6).atTime(9,37).toInstant(ZoneOffset.MAX)))
                        .withTitle("caltitle1")
                        .withDescription("caldescription1")
                        .build())
                .build());
         waitATinyMoment();
        builtChecklists.add(CheckListBuilder.aCheckList()
                .withTitle("Sleep")
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withStartDate(Date.from(LocalDate.of(2017,3,31).atStartOfDay(zoneId).toInstant()))
                        .withEndDate(Date.from(LocalDate.of(2017,4,1).atTime(9,37).toInstant(ZoneOffset.MAX)))
                        .withTitle("caltitle1")
                        .withDescription("caldescription1")
                        .build())
                .build());

        return builtChecklists;
    }

    private void waitATinyMoment() {
        try {
            Thread.sleep(1L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
