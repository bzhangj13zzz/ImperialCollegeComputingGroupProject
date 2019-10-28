package ic.doc.sgo.springframework.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import ic.doc.sgo.springframework.Service.GroupingService;
import ic.doc.sgo.studentadapters.JsonStudentAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/")
public class GroupingController {
  @Resource private GroupingService groupingService;

  @PostMapping(value = "/allocateGroups")
  public @ResponseBody String groupStudents(@RequestBody String json) {
    Gson gson = new Gson();
    JsonObject parsedJson = gson.fromJson(json, JsonObject.class);
    JsonArray studentsJsonArray = parsedJson.getAsJsonArray("students");
    JsonObject constraintJsonObject = parsedJson.getAsJsonObject("filters");

    // Use Gson to convert json to object
    Constraint constraint = gson.fromJson(constraintJsonObject, Constraint.class);

    List<Student> studentList = new ArrayList<>();
    LocalDate now = LocalDate.now();
    for (int i = 0; i < studentsJsonArray.size(); i++) {
      Optional<Student> optionalStudent =
          new JsonStudentAdapter(studentsJsonArray.get(i).getAsJsonObject(), now).toStudent();
      optionalStudent.ifPresent(studentList::add);
    }

    List<Group> groupList = groupingService.groupStudent(studentList, constraint);
    // return a string which is in the form of a JsonObject
    return gson.toJson(getResultJsonObj(groupList));
  }

  private JsonObject getResultJsonObj(List<Group> groupList) {
    JsonObject result = new JsonObject();
    JsonArray studentsJsonArray = new JsonArray();
    for (Group group : groupList) {
      for (Student student : group.getStudents()) {
        JsonObject studentJsonObject = new JsonObject();
        studentJsonObject.addProperty("id", student.getId());
        // Add group id for each student json object
        studentJsonObject.addProperty("groupId", group.getId());
        studentsJsonArray.add(studentJsonObject);
      }
    }
    long numOfGroup = groupList.stream()
            .filter(group -> group.getId() != Group.UNKNOWN_ID && group.getId() != Group.UNALLOC_ID)
            .count();
    long numOfUnallocStu = groupList.stream()
            .filter(group -> group.getId() == Group.UNALLOC_ID)
            .map(Group::size).reduce(0, Integer::sum);
    result.add("students", studentsJsonArray);
    result.addProperty("numOfGroup", numOfGroup);
    result.addProperty("numOfUnalloc", numOfUnallocStu);
    return result;
  }
}
