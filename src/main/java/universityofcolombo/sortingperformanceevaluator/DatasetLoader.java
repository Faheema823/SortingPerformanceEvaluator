package com.sorting.app;

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
        headers.clear();
        dataRows.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line == null) return;

            String[] headerTokens = line.split(",");
            for (String h : headerTokens) {
                headers.add(h.trim());
            }

            while ((line = br.readLine()) != null) {
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
    }

    public List<String> getHeaders() { return headers; }
    public int getRowCount() { return dataRows.size(); }
}