package model;

import console.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 * Converter Files <-> JSON.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ProjectJsonConverter {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static ProjectJsonConverter instance;

    private ProjectJsonConverter() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static ProjectJsonConverter getInstance() {
        if (instance == null) {
            instance = new ProjectJsonConverter();
        }
        return instance;
    }

    public String projectToJsonString(Project project) {
        JSONObject objToReturn = new JSONObject();

        objToReturn.put("jaPyVersion", project.getJaPyVersion());
        objToReturn.put("modificationDate", sdf.format(project.getModificationDate()));
        JSONArray titlesJSONArray = new JSONArray();
        for (String t : project.getTitles()) {
            titlesJSONArray.add(t);
        }
        objToReturn.put("titles", titlesJSONArray);
        JSONArray filesJSONArray = new JSONArray();
        for (String f : project.getFiles()) {
            filesJSONArray.add(f);
        }
        objToReturn.put("files", filesJSONArray);

        return objToReturn.toJSONString();
    }

    public Project jsonStringToProject(String jsonString) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);

            String jaPyVersion = (String) jsonObject.get("jaPyVersion");
            Date modificationDate = sdf.parse((String) jsonObject.get("modificationDate"));
            List<String> titles = new ArrayList<>();
            for (Object titleJSONObject : (JSONArray) jsonObject.get("titles")) {
                titles.add((String) titleJSONObject.toString());
            }
            List<String> files = new ArrayList<>();
            for (Object fileJSONObject : (JSONArray) jsonObject.get("files")) {
                files.add((String) fileJSONObject.toString());
            }
            return new Project(titles, files, jaPyVersion, modificationDate);
        } catch (Exception e) {
            Log.error(ProjectJsonConverter.class, "Could not parse project! " + e.getMessage());
        }
        return null;
    }
}
