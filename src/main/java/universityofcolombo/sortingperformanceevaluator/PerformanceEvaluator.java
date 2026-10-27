/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package universityofcolombo.sortingperformanceevaluator;

/**
 *
 * @author faheema
 */
import java.util.LinkedHashMap;
import java.util.Map;

public class PerformanceEvaluator {

    public static Map<String, Long> evaluateAll(double[] originalData) {
        Map<String, Long> results = new LinkedHashMap<>();

        double[] d1 = originalData.clone();
        long start1 = System.nanoTime();
        SortingAlgorithms.insertionSort(d1);
        results.put("Insertion Sort", System.nanoTime() - start1);

        double[] d2 = originalData.clone();
        long start2 = System.nanoTime();
        SortingAlgorithms.shellSort(d2);
        results.put("Shell Sort", System.nanoTime() - start2);

        double[] d3 = originalData.clone();
        long start3 = System.nanoTime();
        SortingAlgorithms.mergeSort(d3, 0, d3.length - 1);
        results.put("Merge Sort", System.nanoTime() - start3);

        double[] d4 = originalData.clone();
        long start4 = System.nanoTime();
        SortingAlgorithms.quickSort(d4, 0, d4.length - 1);
        results.put("Quick Sort", System.nanoTime() - start4);

        double[] d5 = originalData.clone();
        long start5 = System.nanoTime();
        SortingAlgorithms.heapSort(d5);
        results.put("Heap Sort", System.nanoTime() - start5);

        return results;
    }
    
    public static String getBestAlgorithm(Map<String, Long> results) {
        String best = "";
        long minTime = Long.MAX_VALUE;
        for (Map.Entry<String, Long> entry : results.entrySet()) {
            if (entry.getValue() < minTime) {
                minTime = entry.getValue();
                best = entry.getKey();
            }
        }
        return best + " (" + String.format("%.4f ms", minTime / 1_000_000.0) + ")";
    }
}