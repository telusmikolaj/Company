package pl.com.company.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.company.model.Employee;
import pl.com.company.model.EmployeeSalaryData;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CacheService {

    @Autowired
    ObjectMapper objectMapper;
    private static FileWriter fileWriter;

    public <T> void saveToFile(String filePath, List<T> listToSave) throws IOException {
        fileWriter = new FileWriter(filePath);
        fileWriter.write(convertToJSONString(listToSave));
        fileWriter.close();
    }

    public List<?> load(String path, String className) throws IOException {

        if (checkIfFileExists(path)) {
            String jsonString = new String(getDataFromFile(path));
            return new ArrayList<>(convertToList(jsonString, className));
        }

        return new ArrayList<>();
    }


    public boolean checkIfFileExists(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }

    public <T> String convertToJSONString(List<T> dataList) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dataList);
    }

    private byte[] getDataFromFile(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    public List<?> convertToList(String jsonString, String className) throws JsonProcessingException {
        if (className.equals("Employees")) {
            return Arrays.asList(objectMapper.readValue(jsonString, Employee[].class));
        }

        return Arrays.asList(objectMapper.readValue(jsonString, EmployeeSalaryData[].class));
    }

}
