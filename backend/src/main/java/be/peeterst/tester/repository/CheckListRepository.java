package be.peeterst.tester.repository;

import be.peeterst.tester.model.CheckList;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 2/10/2018
 * Time: 21:30
 */

public interface CheckListRepository extends CrudRepository<CheckList,Integer> {

    CheckList findByCreationDatestamp(Long creationDatestamp);

    List<CheckList> findByTitle(String title);
}
