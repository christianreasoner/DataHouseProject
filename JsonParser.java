import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {
    public static void main(String[] args) {
        try {
            // Create JSONParser instance to parse JSON file
            JSONParser parser = new JSONParser();
            // Read JSON file and parse into JSONObject
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("input.json"));
            // Extract values associated with key "applicants"
            JSONArray applicantsArray = (JSONArray) jsonObject.get("applicants");
            // Extract values associated with key "team"
            JSONArray teamArray = (JSONArray) jsonObject.get("team");
            // Create JSON object for the output file that holds the names and compatibility scores
            JSONObject JSONOutput = new JSONObject();
            // Create jsonArray that holds the individual objects for the names and compatibility scores
            JSONArray jsonArray = new JSONArray();
            double attributesScore = 0;
            double numAtt = 0;
            // HashMap that maps attribute names to the average score from the team members
            // Keys: attribute names
            // Values: an array that contains attribute value at index 0 and the amount of occurrences of the attribute at index 1 (used to calculate average)
            HashMap<String, double[]> teamAverages = new HashMap<String, double[]>();

            // Iterate over team members
            for (Object team : teamArray) {
                JSONObject teamMemberObject = (JSONObject) team;
                JSONObject attributesObject = (JSONObject) teamMemberObject.get("attributes");

                // Iterate over each members attributes
                for (Object key : attributesObject.keySet()) {
                    // Add the attribute to the hashmap if not already added, else add the attribute value to value at index 0 and increment value at index 1
                    if (!teamAverages.containsKey(key)) {
                        teamAverages.put((String) key, new double[] {((Number) attributesObject.get(key)).doubleValue(), 1});
                    } else {
                        double[] attrArray = teamAverages.get(key);
                        attrArray[0] = attrArray[0] + ((Number) attributesObject.get(key)).doubleValue();
                        attrArray[1]++;
                        teamAverages.put((String) key, attrArray);
                    }
                }
            }

            // Iterate over hashmap and compute the average
            for (String key : teamAverages.keySet()) {
                double[] attrArray = teamAverages.get(key);
                attrArray[0] = attrArray[0]/attrArray[1];
                teamAverages.put(key, attrArray);
            }

            // Iterate over array of applicants
            for (Object applicant : applicantsArray) {
                JSONObject applicantObject = (JSONObject) applicant;
                JSONObject attributesObject = (JSONObject) applicantObject.get("attributes");
                // Iterate over each applicants attribute and determine a compatibility score
                for (Object key : attributesObject.keySet()) {
                    double applicantScore = ((Number) attributesObject.get(key)).doubleValue();
                    double teamAvgScore = teamAverages.get(key)[0];
                    attributesScore += .5*(applicantScore/teamAvgScore);
                    numAtt++;
                }
                // Average the compatibility score
                attributesScore /= numAtt;
                // Create a JSONObject that has the applicant name and final compatibility score
                JSONObject postScored = new JSONObject();
                postScored.put("name",applicantObject.get("name"));
                postScored.put("score",attributesScore);
                // Adds each JSON object to an array and resets counters
                jsonArray.add(postScored);
                attributesScore = 0;
                numAtt = 0;
            }
            // Maps the finished array to scoredApplicants
            JSONOutput.put("scoredApplicants", jsonArray);

            try (FileWriter file = new FileWriter("output.json")) {
                file.write(JSONOutput.toJSONString());
                file.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}