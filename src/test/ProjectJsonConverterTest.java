package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import model.Project;
import model.ProjectJsonConverter;

/**
 * Test class for {@link model.ProjectJsonConverter}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ProjectJsonConverterTest {

    public boolean projectJsonConverterTest() {
        // prepare data
        List<String> testListTitles = new ArrayList<>();
        testListTitles.add("üddddd((\\a;;}}]sd");
        testListTitles.add("üdd((}");
        testListTitles.add("üdddd((\\a;;}}]d");
        testListTitles.add("üözsb((\\a;;}}][d");
        testListTitles.add("üöäzsbd((a;;}}][sd");
        testListTitles.add("üddd((\\a}}][d");
        testListTitles.add("üözsddddda;;}][sd");
        List<String> testListFiles = new ArrayList<>();
        testListFiles.add("üdddddddddddd((\\a;;}");
        testListFiles.add("üdddddddddd((\\a;;}}][sd");
        testListFiles.add("üöäddddddddddddzsbd((\\a;;}}][sd");
        testListFiles.add("üödddddddddäzsbd((a;;}}][sd");
        testListFiles.add("üdddddddddddddd((\\a;;}}][sd");
        testListFiles.add("üdddddddddddd((\\a;;}");
        testListFiles.add("üddddddddddddddddddd((\\a;;}}][sd");
        Project project = new Project(testListTitles, testListFiles, "äöoe633", new Date());

        // execute tests
        String projectJsonString = ProjectJsonConverter.getInstance().projectToJsonString(project);
        Project projectConverted = ProjectJsonConverter.getInstance().jsonStringToProject(projectJsonString);

        // return result
        return project.equals(projectConverted);
    }
}
