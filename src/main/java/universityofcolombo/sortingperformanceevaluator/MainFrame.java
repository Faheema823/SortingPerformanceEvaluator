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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.Map;

public class MainFrame extends JFrame {
    private DatasetLoader loader = new DatasetLoader();
    private JComboBox<String> columnComboBox;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JLabel bestAlgoValueLabel;
    private JLabel bestTimeValueLabel;

    public MainFrame() {
        setTitle("Sorting Algorithm Performance Evaluation - University of Colombo");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Root panel with padding
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(rootPanel);

        // 1. Top Control Panel (File Upload & Column Selection)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Dataset Configuration"));
        JButton uploadBtn = new JButton("Upload CSV File");
        columnComboBox = new JComboBox<>();
        columnComboBox.setPreferredSize(new Dimension(180, 25));
        JButton runBtn = new JButton("Run Sorting & Evaluate");

        topPanel.add(uploadBtn);
        topPanel.add(new JLabel("Select Column:"));
        topPanel.add(columnComboBox);
        topPanel.add(runBtn);
        rootPanel.add(topPanel, BorderLayout.NORTH);

        // 2. Center Panel: Split into Best Algorithm Highlight Card & Results Table
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Highlight Card for Best Performing Algorithm (Centered prominently)
        JPanel highlightPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        highlightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Best Performing Algorithm"),
            new EmptyBorder(10, 15, 10, 15)
        ));
        highlightPanel.setBackground(new Color(245, 247, 250));

        bestAlgoValueLabel = new JLabel("Algorithm: N/A", JLabel.CENTER);
        bestAlgoValueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bestAlgoValueLabel.setForeground(new Color(20, 90, 50));

        bestTimeValueLabel = new JLabel("Execution Time: N/A", JLabel.CENTER);
        bestTimeValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bestTimeValueLabel.setForeground(new Color(40, 40, 40));

        highlightPanel.add(bestAlgoValueLabel);
        highlightPanel.add(bestTimeValueLabel);
        centerPanel.add(highlightPanel, BorderLayout.NORTH);

        // Results Table
        tableModel = new DefaultTableModel(new String[]{"Algorithm", "Execution Time (ms)"}, 0);
        resultsTable = new JTable(tableModel);
        resultsTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Performance Benchmark Results"));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        rootPanel.add(centerPanel, BorderLayout.CENTER);

        // 3. Bottom Status Bar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Status: Ready. Please upload a CSV dataset.");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        bottomPanel.add(statusLabel);
        rootPanel.add(bottomPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    loader.loadCSV(file);
                    columnComboBox.removeAllItems();
                    for (String h : loader.getHeaders()) {
                        columnComboBox.addItem(h);
                    }
                    statusLabel.setText("Status: Loaded " + file.getName() + " successfully (" + loader.getRowCount() + " rows).");
                    bestAlgoValueLabel.setText("Algorithm: N/A");
                    bestTimeValueLabel.setText("Execution Time: N/A");
                    tableModel.setRowCount(0);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error parsing CSV: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        runBtn.addActionListener(e -> {
            if (loader.getRowCount() == 0 || columnComboBox.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "Please upload a valid CSV dataset and select a numeric column first!", "Validation Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int colIdx = columnComboBox.getSelectedIndex();
            double[] data = loader.getColumnData(colIdx);

            Map<String, Long> perfMap = PerformanceEvaluator.evaluateAll(data);
            tableModel.setRowCount(0);

            String fastestAlgo = "";
            long minTime = Long.MAX_VALUE;

            for (Map.Entry<String, Long> entry : perfMap.entrySet()) {
                long nanoTime = entry.getValue();
                double ms = nanoTime / 1_000_000.0;
                tableModel.addRow(new Object[]{entry.getKey(), String.format("%.4f ms", ms)});

                if (nanoTime < minTime) {
                    minTime = nanoTime;
                    fastestAlgo = entry.getKey();
                }
            }

            double bestMs = minTime / 1_000_000.0;
            bestAlgoValueLabel.setText("Algorithm: " + fastestAlgo);
            bestTimeValueLabel.setText(String.format("Execution Time: %.4f ms", bestMs));
            statusLabel.setText("Status: Sorting performance evaluation completed successfully.");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}