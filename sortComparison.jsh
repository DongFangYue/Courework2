import java.util.*;
import java.io.*;
import java.nio.file.*;

int convertCardValue(String num) {
    switch (num) {
        case "A": return 1;
        case "J": return 11;
        case "Q": return 12;
        case "K": return 13;
        default: return Integer.parseInt(num);
    }
}

int cardCompare(String card1, String card2) {
    char suit1 = card1.charAt(card1.length() - 1);
    char suit2 = card2.charAt(card2.length() - 1);
    
    if (suit1 != suit2) {
        String suits = "HCDS";
        return suits.indexOf(suit1) - suits.indexOf(suit2);
    }
    
    String num1 = card1.substring(0, card1.length() - 1);
    String num2 = card2.substring(0, card2.length() - 1);
    
    int val1 = convertCardValue(num1);
    int val2 = convertCardValue (num2);
    
    return Integer.compare(val1, val2);
}

ArrayList<String> bubbleSort(ArrayList<String> array) {
    ArrayList<String> result = new ArrayList<>(array);
    int n = result.size();
    
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (cardCompare(result.get(j), result.get(j + 1)) > 0) {
                String temp = result.get(j);
                result.set(j, result.get(j + 1));
                result.set(j + 1, temp);
            }
        }
    }
    
    return result;
}

ArrayList<String> merge(ArrayList<String> left, ArrayList<String> right) {
    ArrayList<String> result = new ArrayList<>();
    int leftIndex = 0, rightIndex = 0;
    
    while (leftIndex < left.size() && rightIndex < right.size()) {
        if (cardCompare(left.get(leftIndex), right.get(rightIndex)) <= 0) {
            result.add(left.get(leftIndex));
            leftIndex++;
        } else {
            result.add(right.get(rightIndex));
            rightIndex++;
        }
    }
    
    result.addAll(left.subList(leftIndex, left.size()));
    result.addAll(right.subList(rightIndex, right.size()));
    
    return result;
}

ArrayList<String> mergeSort(ArrayList<String> array) {
    if (array.size() <= 1) {
        return new ArrayList<>(array);
    }
    
    int mid = array.size() / 2;
    ArrayList<String> left = new ArrayList<>(array.subList(0, mid));
    ArrayList<String> right = new ArrayList<>(array.subList(mid, array.size()));
    
    left = mergeSort(left);
    right = mergeSort(right);
    
    return merge(left, right);
}

long measureBubbleSort(String filename) throws IOException {
    ArrayList<String> cards = new ArrayList<>(Files.readAllLines(Paths.get(filename)));
    
    long startTime = System.currentTimeMillis();
    bubbleSort(cards);
    long endTime = System.currentTimeMillis();
    
    return endTime - startTime;
}

long measureMergeSort(String filename) throws IOException {
    ArrayList<String> cards = new ArrayList<>(Files.readAllLines(Paths.get(filename)));
    
    long startTime = System.currentTimeMillis();
    mergeSort(cards);
    long endTime = System.currentTimeMillis();
    
    return endTime - startTime;
}

void sortComparison(String[] filenames) throws IOException {
    PrintWriter writer = new PrintWriter("sortComparison.csv");
    
    writer.print(filenames.length);
    for (String filename : filenames) {
        int size = Files.readAllLines(Paths.get(filename)).size();
        writer.print(", " + size);
    }
    writer.println();
    
    writer.print("bubbleSort");
    for (String filename : filenames) {
        writer.print(", " + measureBubbleSort(filename));
    }
    writer.println();
    
    writer.print("mergeSort");
    for (String filename : filenames) {
        writer.print(", " + measureMergeSort(filename));
    }
    writer.println();
    
    writer.close();
}
