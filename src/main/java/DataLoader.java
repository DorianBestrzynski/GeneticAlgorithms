import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DataLoader {

    private File prepareFile(String path){
        String solPath = "src/main/resources/flo_dane_v1.1/";
        String fullPath = solPath + path;
        return new File(fullPath);
    }

    public List<Cost> mapCostToObject(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file  = prepareFile(path);
        List<Cost> costList =  objectMapper.readValue(file, new TypeReference<List<Cost>>() {
        });

        Comparator<Cost> compare = Comparator
                .comparing(Cost::getSource)
                .thenComparing(Cost::getDest);

        return costList.stream()
                .sorted(compare)
                .collect(Collectors.toList());

    }
    public List<Flow> mapFlowToObject(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file  = prepareFile(path);
        List<Flow> flowList = objectMapper.readValue(file, new TypeReference<List<Flow>>() {
        });

        Comparator<Flow> compare= Comparator
                .comparing(Flow::getSource)
                .thenComparing(Flow::getDest);

        return flowList.stream()
                .sorted(compare)
                .collect(Collectors.toList());
    }
}
