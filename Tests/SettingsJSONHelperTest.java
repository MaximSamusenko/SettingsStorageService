import org.junit.jupiter.api.Test;
import org.json.*;
import static org.junit.jupiter.api.Assertions.*;

class SettingsJSONHelperTest {

    public final String FirstJSONObjectStr = "{\n" +
            "  \"LV1 setting 1\": \"setting 1\",\n" +
            "  \"LV1 setting 2\": 2,\n" +
            "  \"LV1 setting 3\": {\n" +
            "    \"LV2 setting 1\": \"setting 1\",\n" +
            "    \"LV2 setting 2\": 2,\n" +
            "    \"LV2 setting 3\": {\n" +
            "      \"LV3 setting 1\": \"setting 1\",\n" +
            "      \"LV3 setting 2\": 2,\n" +
            "      \"LV3 setting 3\": [\n" +
            "        {\"arraySetting1\": 1},\n" +
            "        {\"arraySetting2\": 2},\n" +
            "      ]\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public final String SecondJSONObjectStr = "{\n" +
            "  \"LV1 setting 1\": \"setting 1\",\n" +
            "  \"LV1 setting 2\": 2,\n" +
            "  \"LV1 setting 3\": {\n" +
            "    \"LV2 setting 1\": \"setting 1\",\n" +
            "    \"LV2 setting 2\": 2,\n" +
            "    \"LV2 setting 3\": {\n" +
            "      \"LV3 setting 1\": \"setting 1\",\n" +
            "      \"LV3 setting 2\": 2,\n" +
            "      \"LV3 setting 3\": [\n" +
            "1.25,\n" +
            "1.4,\n" +
            "      ]\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public final String SubstractFirstAndSecond = "{\"LV1 setting 3\":{\"LV2 setting 3\":{\"LV3 setting 3\":[{\"arraySetting1\":1},{\"arraySetting2\":2}]}}}";


    @Test
    void applyArray() throws JSONException {
        JSONObject awaitingResult = new JSONObject(FirstJSONObjectStr);
        JSONArray array = awaitingResult.getJSONObject("LV1 setting 3").getJSONObject("LV2 setting 3").getJSONArray("LV3 setting 3");
        array.put(new JSONObject("{\"arraySetting1\":1}"));
        array.put(new JSONObject("{\"arraySetting2\":2}"));
        String result = SettingsJSONHelper.Apply(FirstJSONObjectStr, FirstJSONObjectStr);
        assertEquals(awaitingResult.toString(), result);
    }

    @Test
    void applyChageValue() throws JSONException {
        JSONObject awaitingResult = new JSONObject(FirstJSONObjectStr);
        awaitingResult.getJSONObject("LV1 setting 3").getJSONObject("LV2 setting 3").remove("LV3 setting 3");
        awaitingResult.put("LV1 setting 1", "test");

        String result = SettingsJSONHelper.Apply(FirstJSONObjectStr, awaitingResult.toString());

        JSONArray array = new JSONArray();
        array.put(new JSONObject("{\"arraySetting1\":1}"));
        array.put(new JSONObject("{\"arraySetting2\":2}"));
        awaitingResult.getJSONObject("LV1 setting 3").getJSONObject("LV2 setting 3").put("LV3 setting 3", array);

        assertEquals(awaitingResult.toString(), result);
    }

    @Test
    void applyAddValue() throws JSONException {
        JSONObject awaitingResult = new JSONObject(FirstJSONObjectStr);
        awaitingResult.getJSONObject("LV1 setting 3").getJSONObject("LV2 setting 3").remove("LV3 setting 3");
        awaitingResult.getJSONObject("LV1 setting 3").put("LV1 setting 4", "test");

        String result = SettingsJSONHelper.Apply(FirstJSONObjectStr, awaitingResult.toString());

        JSONArray array = new JSONArray();
        array.put(new JSONObject("{\"arraySetting1\":1}"));
        array.put(new JSONObject("{\"arraySetting2\":2}"));
        awaitingResult.getJSONObject("LV1 setting 3").getJSONObject("LV2 setting 3").put("LV3 setting 3", array);

        assertEquals(awaitingResult.toString(), result);
    }

    @Test
    void subtract() throws JSONException {
        assertEquals("", SettingsJSONHelper.Subtract(FirstJSONObjectStr, FirstJSONObjectStr));
        assertEquals("", SettingsJSONHelper.Subtract(SecondJSONObjectStr, SecondJSONObjectStr));
        assertEquals(SubstractFirstAndSecond, SettingsJSONHelper.Subtract(FirstJSONObjectStr, SecondJSONObjectStr));
    }

}