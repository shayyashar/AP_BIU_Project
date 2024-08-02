package test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class GenericConfig implements Config{
    public String configFile;
    public List<ParallelAgent> agents;

    public void setConfFile(String filePath) {
        this.configFile = filePath;
    }

    @Override
    public void create() {
        this.agents = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(this.configFile));
            if (lines.size() %3 != 0) {
                throw new RuntimeException("Config file is not valid");
            }
            int lines_num = 0;
            while (lines_num < lines.size()) {
                Class<?> agentClass = Class.forName(lines.get(lines_num));
                String[] agentSubs = lines.get(lines_num + 1).split(",");
                String[] agentPubs = lines.get(lines_num + 2).split(",");
                Constructor<?>[] constructors = agentClass.getConstructors();

                Constructor<?> constructor = agentClass.getConstructor(String[].class, String[].class);
                Agent a = (Agent) constructor.newInstance(agentSubs, agentPubs);
                this.agents.add(new ParallelAgent(a, 10));

                lines_num = lines_num + 3;

            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException |
                 InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void close() {
        for (ParallelAgent a : this.agents) {
            a.close();
        }
    }
}
