package be.peeterst.tester.controller;

import be.peeterst.tester.builder.CalendarItemBuilder;
import be.peeterst.tester.builder.CheckListBuilder;
import be.peeterst.tester.builder.ChecklistItemBuilder;
import be.peeterst.tester.model.CheckList;
import be.peeterst.tester.repository.CheckListRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    private List<CheckList> checkLists;

    private final CheckListRepository checkListRepository;

    @Autowired
    public CheckListOverviewController(CheckListRepository checkListRepository) {
//        this.checkLists = buildChecklists();
        this.checkListRepository = checkListRepository;
        getCheckLists();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CheckList> getCheckLists() {
        this.checkLists = new ArrayList<>();
        checkListRepository.findAll().forEach(this.checkLists::add);
        System.out.println("get all: "+this.checkLists);
        return checkLists;
    }

    @RequestMapping(value = "/find/{title}",method = RequestMethod.GET)
    public List<CheckList> getCheckList(@PathVariable("title") String title){
        return checkListRepository.findByTitle(title);
    }

    @RequestMapping(value = "/{creationDatestamp}",method = RequestMethod.GET)
    public CheckList getCheckList(@PathVariable("creationDatestamp") long creationDateStamp){
        return checkListRepository.findByCreationDatestamp(creationDateStamp);
    }

    @RequestMapping(value = "/write",method = RequestMethod.POST)
    public CheckList saveCheckList(@RequestBody CheckList checkList){
        checkList.setCreationDatestamp(new Date().getTime());
        checkList.getItems().forEach(item -> item.setCheckList(checkList));
        checkList.getCalendarItem().setCheckList(checkList);
        System.out.println("save one: "+checkList);
        return this.checkListRepository.save(checkList);
    }

    @RequestMapping(value = "/write",method = RequestMethod.PUT)
    public CheckList updateCheckList(@RequestBody CheckList checkList){

        checkList.getItems().forEach(item -> item.setCheckList(checkList));
        checkList.getCalendarItem().setCheckList(checkList);
        return this.checkListRepository.save(checkList);
    }

    @RequestMapping(value = "/write/{creationDatestamp}",method = RequestMethod.DELETE)
    public int deleteCheckList(@PathVariable long creationDatestamp){
        getCheckLists();
        CheckList found = this.checkLists.stream().filter(checkList -> checkList.getCreationDatestamp() == creationDatestamp)
                .findFirst().orElse(null);
        if(found == null) return 0;
            this.checkLists.remove(found);
            this.checkListRepository.delete(found);
        return 1;
    }

    public void setCheckLists(List<CheckList> checkLists) {
        this.checkLists = checkLists;
    }

    private List<CheckList> buildChecklists() {
        List<CheckList> builtChecklists = new ArrayList<>();
        ZoneId zoneId = ZoneId.systemDefault();
        
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
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withBulletName("bullet 2")
                        .withCheck(false)
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
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withBulletName("bullet 1")
                        .withCheck(true)
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
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withBulletName("bullet 1")
                        .withCheck(true)
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withBulletName("bullet 2")
                        .withCheck(false)
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withBulletName("bullet 3")
                        .withCheck(false)
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
