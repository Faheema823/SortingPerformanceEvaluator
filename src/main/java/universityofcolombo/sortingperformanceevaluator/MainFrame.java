/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package universityofcolombo.sortingperformanceevaluator;

/**
 *
 * @author faheema
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map; // <-- ADDED THIS IMPORT TO FIX THE COMPILATION ERROR

public class MainFrame extends JFrame {
    private DatasetLoader loader = new DatasetLoader();
    private JComboBox<String> columnComboBox;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel, bestAlgoLabel;

    public MainFrame() {
        setTitle("Sorting Algorithm Performance Evaluation - University of Colombo");
        setSize(750, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout());
        JButton uploadBtn = new JButton("Upload CSV File");
        columnComboBox = new JComboBox<>();
        JButton runBtn = new JButton("Run Sorting & Evaluate");

        topPanel.add(uploadBtn);
        topPanel.add(new JLabel("Select Column:"));
        topPanel.add(columnComboBox);
        topPanel.add(runBtn);

        tableModel = new DefaultTableModel(new String[]{"Algorithm", "Execution Time (ms)"}, 0);
        resultsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultsTable);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        statusLabel = new JLabel(" Status: Ready. Please upload a CSV dataset.");
        bestAlgoLabel = new JLabel(" Best Performing Algorithm: N/A");
        bottomPanel.add(statusLabel);
        bottomPanel.add(bestAlgoLabel);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                try {
                    loader.loadCSV(file);
                    columnComboBox.removeAllItems();
                    for (String h : loader.getHeaders()) {
                        columnComboBox.addItem(h);
                    }
                    statusLabel.setText(" Status: Loaded " + file.getName() + " (" + loader.getRowCount() + " rows)");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error parsing CSV: " + ex.getMessage());
                }
            }
        });
        
        runBtn.addActionListener(e -> {
            if (loader.getRowCount() == 0 || columnComboBox.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "Please upload a valid CSV dataset first!");
                return;
            }

            int colIdx = columnComboBox.getSelectedIndex();
            double[] data = loader.getColumnData(colIdx);

            Map<String, Long> perfMap = PerformanceEvaluator.evaluateAll(data);
            tableModel.setRowCount(0);

            for (Map.Entry<String, Long> entry : perfMap.entrySet()) {
                double ms = entry.getValue() / 1_000_000.0;
                tableModel.addRow(new Object[]{entry.getKey(), String.format("%.4f ms", ms)});
            }

            String best = PerformanceEvaluator.getBestAlgorithm(perfMap);
            bestAlgoLabel.setText(" Best Performing Algorithm: " + best);
            statusLabel.setText(" Status: Evaluation completed successfully.");
        });
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}