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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}