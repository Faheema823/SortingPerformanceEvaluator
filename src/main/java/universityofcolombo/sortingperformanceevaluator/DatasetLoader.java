/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package universityofcolombo.sortingperformanceevaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DatasetLoader {
    private List<String> headers = new ArrayList<>();
    private List<double[]> dataRows = new ArrayList<>();

    public void loadCSV(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("Selected file does not exist or is invalid.");
        }
        
        headers.clear();
        dataRows.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line == null) {
                throw new IOException("The uploaded CSV file is empty.");
            }

            String[] headerTokens = line.split(",");
            for (String h : headerTokens) {
                headers.add(h.trim());
            }

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip blank lines safely
                }
                
                String[] tokens = line.split(",");
                double[] rowValues = new double[tokens.length];
                boolean valid = true;

                for (int i = 0; i < tokens.length; i++) {
                    try {
                        rowValues[i] = Double.parseDouble(tokens[i].trim());
                    } catch (NumberFormatException e) {
                        valid = false;
                        break;
                    }
                }
                if (valid && tokens.length == headers.size()) {
                    dataRows.add(rowValues);
                }
            }
        }
        
        if (dataRows.isEmpty()) {
            throw new IOException("No valid numeric rows found in the selected CSV file.");
        }
    }

    public List<String> getHeaders() { return headers; }
    public int getRowCount() { return dataRows.size(); }
    
    public double[] getColumnData(int columnIndex) {
        if (dataRows.isEmpty() || columnIndex < 0 || columnIndex >= headers.size()) {
            return new double[0]; // Return empty array safely instead of crashing
        }
        double[] columnData = new double[dataRows.size()];
        for (int i = 0; i < dataRows.size(); i++) {
            columnData[i] = dataRows.get(i)[columnIndex];
        }
        return columnData;
    }
}