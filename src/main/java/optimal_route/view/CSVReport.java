package optimal_route.view;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class CSVReport{

    public static String[] convertToCSV(String data,char separator) throws CsvValidationException, IOException {
        StringReader stringReader = new StringReader(data);
        CSVReader csvReader = new CSVReaderBuilder(stringReader)
                .withCSVParser(new CSVParserBuilder().withSeparator(separator).build())
                .build();
        return csvReader.readNext();
    }

    public static void writeLine(String filePath, String[] line, char separator)
    {
        File file = new File(filePath);
        try {
            FileWriter fileWriter = new FileWriter(file,true);
            CSVWriter writer = new CSVWriter(fileWriter, separator,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            writer.writeNext(line);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeLines(String filePath, List<String[]> lines, char separator)
    {
        File file = new File(filePath);
        try {
            FileWriter fileWriter = new FileWriter(file);
            CSVWriter writer = new CSVWriter(fileWriter, separator,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            writer.writeAll(lines);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String[] readLine(String file,char separator,int skip)
    {
        List<String> result = new ArrayList<>();
        try {
            FileReader filereader = new FileReader(file);
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(separator).build())
                    .withSkipLines(skip)
                    .build();
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    result.add(cell);
                }
                System.out.println();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (String[])result.toArray();
    }

    public static void displayContentToConsole(String file,char separator,int skip)
    {
        try {
            FileReader filereader = new FileReader(file);
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(separator).build())
                    .withSkipLines(skip)
                    .build();
            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                for (String cell : row) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<String[]> readLines(String file) {
        List<String[]> allData = new ArrayList<>();
        try {
            FileReader filereader = new FileReader(file);
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            allData = csvReader.readAll();
        } catch(Exception e){
            e.printStackTrace();
        }
        return allData;
    }


}
