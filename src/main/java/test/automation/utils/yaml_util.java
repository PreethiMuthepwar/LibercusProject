package test.automation.utils;
import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import static java.nio.file.Files.readString;
public class yaml_util
{
        public static Map<String, Object> loadData(String filePath)
        {
            try
            {
                String content = readString(Paths.get(filePath));
                Yaml yaml = new Yaml();
                return yaml.load(content);
            }
            catch (IOException e)
            {
                System.err.println("Error reading/parsing YAML file: " + filePath);
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
}