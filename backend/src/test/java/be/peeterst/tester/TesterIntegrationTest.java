package be.peeterst.tester;

import be.peeterst.tester.builder.CalendarItemBuilder;
import be.peeterst.tester.builder.CheckListBuilder;
import be.peeterst.tester.builder.ChecklistItemBuilder;
import be.peeterst.tester.model.CheckList;
import be.peeterst.tester.repository.CheckListRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.NotAuthorizedException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

//import org.jooq.DSLContext;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 9/09/2018
 * Time: 16:43
 */


@Profile("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource({"classpath:application-test.properties"})
@Sql(scripts = "/sql/cleanup.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TesterIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

//    @Autowired
//    private DSLContext dsl;

    @Autowired
    private CheckListRepository checkListRepository;

    @Autowired
    private KeycloakSecurityContextClientRequestInterceptor keycloakSecurityContextClientRequestInterceptor;

    @Before
    public void setUp() {
        testRestTemplate.getRestTemplate().getInterceptors().add(keycloakSecurityContextClientRequestInterceptor);
        Date calendarStartDate = DateUtils.truncate(Date.from(Instant.now()), Calendar.SECOND);
        Date calendarEndDate = DateUtils.truncate(Date.from(Instant.now().plus(Duration.ofDays(1))),Calendar.SECOND);

        CheckList oneChecklist = CheckListTestUtils.createOneChecklist(calendarStartDate, calendarEndDate);
        checkListRepository.save(oneChecklist);
    }

    @Test
    public void overviewTest(){

        ResponseEntity<CheckList[]> checkListResponse = testRestTemplate.getForEntity("/overview",CheckList[].class);
        assertThat(checkListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<CheckList> checkLists = Arrays.stream(checkListResponse.getBody()).collect(Collectors.toList());
        assertThat(checkLists.size()).isEqualTo(1);
    }

    @Test
    public void overviewTest_one(){

        ResponseEntity<CheckList[]> checkListResponse = testRestTemplate.getForEntity("/overview/find/Work",CheckList[].class);

        assertThat(checkListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        CheckList[] checkLists = checkListResponse.getBody();
        assertThat(checkLists).isNotNull();
        assertThat(checkLists.length).isEqualTo(1);
        Optional<CheckList> checkListOptional = Arrays.stream(checkLists).findFirst();
        CheckList checkList = checkListOptional.orElseThrow(IllegalStateException::new);
        assertThat(checkList.getTitle()).isEqualTo("Work");
    }

    @Test
    public void checkListAddTest(){

        String checkListTitle = "ADDED";
        Date calendarStartDate = new Date();
        String calendarDescription = "calendarDescription";
        Date calendarEndDate = new Date();

        String calendarTitle = "calendarTitle";
        String itemBulletName = "bulletName";
        CheckList checkList = CheckListBuilder.aCheckList()
                .withTitle(checkListTitle)
//                .withCreationDateStamp(System.currentTimeMillis())
                .withCalendarItem(CalendarItemBuilder.aCalendarItem()
                        .withDescription(calendarDescription)
                        .withStartDate(calendarStartDate)
                        .withEndDate(calendarEndDate)
                        .withTitle(calendarTitle)
                        .build())
                .addChecklistItem(ChecklistItemBuilder.aChecklistItem()
                        .withBulletName(itemBulletName)
                        .withCheck(true)
                        .build())
                .build();

        keycloakSecurityContextClientRequestInterceptor.setKeycloakTestUser("moduser");
        keycloakSecurityContextClientRequestInterceptor.setKeycloakTestPassword("moduser");

        ResponseEntity<Void> addResponseEntity = testRestTemplate.postForEntity("/overview/write", checkList, Void.class);
        assertThat(addResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<CheckList[]> checkListResponse = testRestTemplate.getForEntity("/overview",CheckList[].class);
        assertThat(checkListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<CheckList> checkLists = Arrays.stream(checkListResponse.getBody()).collect(Collectors.toList());
        assertThat(checkLists.size()).isEqualTo(2);
        CheckList checkListToCheck = checkLists.stream().filter(checkListFound -> checkListFound.getTitle().equals(checkListTitle)).findFirst().orElse(null);
        assertThat(checkListToCheck).isNotNull();

        keycloakSecurityContextClientRequestInterceptor.setKeycloakTestUser("user");
        try {
            addResponseEntity = testRestTemplate.postForEntity("/overview/write", checkList, Void.class);
            fail("Should not reach this");
        }catch (NotAuthorizedException fourOhOne){
            assertThat(fourOhOne.getResponse().getStatusInfo().getReasonPhrase()).isEqualTo(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        keycloakSecurityContextClientRequestInterceptor.setKeycloakTestPassword("useruser");
        addResponseEntity = testRestTemplate.postForEntity("/overview/write", checkList, Void.class);
        assertThat(addResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    public void checkListUpdateTest(){

        keycloakSecurityContextClientRequestInterceptor.setKeycloakTestUser("moduser");
        keycloakSecurityContextClientRequestInterceptor.setKeycloakTestPassword("moduser");

        String updated_description = "updated description";
        String bulletName = "added-bullet-name";

        ResponseEntity<CheckList[]> checkListResponse = testRestTemplate.getForEntity("/overview",CheckList[].class);
        assertThat(checkListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<CheckList> checkLists = Arrays.stream(checkListResponse.getBody()).collect(Collectors.toList());
        CheckList checkList = checkLists.get(0);
        checkList.getCalendarItem().setDescription(updated_description);
        checkList.getItems().add(ChecklistItemBuilder.aChecklistItem()
                .withBulletName(bulletName)
                .withCheck(true)
                .build());

        HttpEntity<CheckList> entity = new HttpEntity<>(checkList);

        ResponseEntity<CheckList> updateResponse = testRestTemplate.exchange("/overview/write", HttpMethod.PUT, entity, CheckList.class, "");
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);


        ResponseEntity<CheckList> updatedEntityResponse = testRestTemplate.getForEntity("/overview/" + checkList.getCreationDatestamp(), CheckList.class);
        assertThat(updatedEntityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        CheckList updatedCheckListFromService = updatedEntityResponse.getBody();
        assertThat(updatedCheckListFromService).isEqualToComparingFieldByField(checkList);

        keycloakSecurityContextClientRequestInterceptor.setKeycloakTestUser("user");
        try {
            updateResponse = testRestTemplate.exchange("/overview/write", HttpMethod.PUT, entity, CheckList.class, "");
            fail("Should not reach this");
        }catch (NotAuthorizedException fourOhOne){
            assertThat(fourOhOne.getResponse().getStatusInfo().getReasonPhrase()).isEqualTo(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        keycloakSecurityContextClientRequestInterceptor.setKeycloakTestPassword("useruser");
        updateResponse = testRestTemplate.exchange("/overview/write", HttpMethod.PUT, entity, CheckList.class, "");
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    public void deleteTest(){

        keycloakSecurityContextClientRequestInterceptor.setKeycloakTestUser("moduser");
        keycloakSecurityContextClientRequestInterceptor.setKeycloakTestPassword("moduser");

        ResponseEntity<CheckList[]> checkListResponse = testRestTemplate.getForEntity("/overview/find/Work",CheckList[].class);

        assertThat(checkListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        CheckList[] checkLists = checkListResponse.getBody();
        assertThat(checkLists).isNotNull();
        assertThat(checkLists.length).isEqualTo(1);
        Optional<CheckList> checkListOptional = Arrays.stream(checkLists).findFirst();
        CheckList checkList = checkListOptional.orElseThrow(IllegalStateException::new);


        ResponseEntity<Integer> deleteResponse = testRestTemplate.exchange("/overview/write/" + checkList.getCreationDatestamp(),
                HttpMethod.DELETE, null, Integer.class, "");
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        checkListResponse = testRestTemplate.getForEntity("/overview/find/Work",CheckList[].class);
        assertThat(checkListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        checkLists = checkListResponse.getBody();
        assertThat(checkLists).isNotNull();
        assertThat(checkLists.length).isEqualTo(0);


        keycloakSecurityContextClientRequestInterceptor.setKeycloakTestUser("user");
        try {
            testRestTemplate.exchange("/overview/write/" + checkList.getCreationDatestamp(),
                    HttpMethod.DELETE, null, Integer.class, "");
            fail("Should not reach this");
        }catch (NotAuthorizedException fourOhOne){
            assertThat(fourOhOne.getResponse().getStatusInfo().getReasonPhrase()).isEqualTo(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        keycloakSecurityContextClientRequestInterceptor.setKeycloakTestPassword("useruser");
        ResponseEntity<Void> deleteAttempt = testRestTemplate.exchange("/overview/write/" + checkList.getCreationDatestamp(),
                HttpMethod.DELETE, null, Void.class, "");
        assertThat(deleteAttempt.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }



}
