package ic.doc.sgo.springframework.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import ic.doc.sgo.groupingstrategies.RandomGroupingStrategy;
import ic.doc.sgo.springframework.Service.GroupingService;
import ic.doc.sgo.studentadapters.JsonStudentAdapter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class GroupingController {
  @Resource GroupingService groupingService;

  @PostMapping(value = "/allocateGroups")
  public @ResponseBody String groupStudents(@RequestBody String json) {
    Gson gson = new Gson();
    JsonObject parsedJson = gson.fromJson(json, JsonObject.class);
    JsonArray studentsJsonArray = parsedJson.getAsJsonArray("students");
    JsonObject constraintJsonObject = parsedJson.getAsJsonObject("filters");

    Constraint constraint = gson.fromJson(constraintJsonObject, Constraint.class);

    List<Student> studentList = new ArrayList<>();
    for (int i = 0; i < studentsJsonArray.size(); i++) {
      Optional<Student> optionalStudent =
          new JsonStudentAdapter(new JSONObject(studentsJsonArray.get(i).toString())).toStudent();
      optionalStudent.ifPresent(studentList::add);
    }

    for (Student student : studentList) {
      System.out.println(student.getId());
    }

    List<Group> groupList = new RandomGroupingStrategy().apply(studentList, constraint);
    return groupListToJsonArray(groupList);
  }

  private String groupListToJsonArray(List<Group> groupList) {
    JsonArray array = new JsonArray();
    List<String> list =
        groupList
            .stream()
            .map(
                group ->
                    group
                        .getStudents()
                        .stream()
                        .map(Student::toString)
                        .collect(Collectors.toList()))
            .collect(Collectors.toList())
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
    return new Gson().toJson(array);
  }
}
