package com.example.frauddetectorfinal.Models;

import android.util.Log;
import java.util.*;

public class Graph {
    private Map<String, List<String>> adjacencyList;
    private Set<String> visited;

    public Graph() {
        adjacencyList = new HashMap<>();
        visited = new HashSet<>();
    }

    public void addTransaction(String fromAccount, String toAccount) {
        if (!fromAccount.equals(toAccount)) {
            adjacencyList.putIfAbsent(fromAccount, new ArrayList<>());
            if (!adjacencyList.get(fromAccount).contains(toAccount)) {
                adjacencyList.get(fromAccount).add(toAccount);
                Log.d("Graph", "Added edge: " + fromAccount + " -> " + toAccount);

                // Check if the new transaction creates a cycle
                if (isFraudulentTransaction(fromAccount, toAccount)) {
                    Log.d("FraudDetection", "Fraud detected for transaction: " + fromAccount + " -> " + toAccount);
                } else {
                    Log.d("FraudDetection", "Transaction is safe: " + fromAccount + " -> " + toAccount);
                }
            }
        }
    }
    public boolean isFraudulentTransaction(String fromAccount, String toAccount) {
        // Ignore self-loops directly in the fraud detection logic
        if (fromAccount.equals(toAccount)) {
            Log.d("FraudDetection", "Self-loop allowed: " + fromAccount + " -> " + toAccount);
            return false; // Self-loop is not fraudulent
        }

        // Initialize visited set
        Set<String> visited = new HashSet<>();
        boolean hasCycle = isCyclic(fromAccount, visited, null);

        Log.d("FraudDetection", "Checking for cycle from: " + fromAccount + " -> " + toAccount);
        if (hasCycle) {
            Log.d("FraudDetection", "Cycle detected involving: " + fromAccount);
        } else {
            Log.d("FraudDetection", "No cycle detected for transaction: " + fromAccount + " -> " + toAccount);
        }

        return hasCycle;
    }


    // Cycle detection method in Graph class
    private boolean isCyclic(String currentNode, Set<String> visited, String parentNode) {
        // Mark the current node as visited
        visited.add(currentNode);

        // Iterate through adjacent nodes
        for (String neighbor : adjacencyList.getOrDefault(currentNode, new ArrayList<>())) {
            // Ignore self-loops
            if (currentNode.equals(neighbor)) {
                Log.d("Graph", "Ignoring self-loop: " + currentNode + " -> " + neighbor);
                continue;
            }

            // If the neighbor is not visited, recurse
            if (!visited.contains(neighbor)) {
                if (isCyclic(neighbor, visited, currentNode)) {
                    return true;
                }
            }
            // If the neighbor is visited and is not the parent, we found a cycle
            else if (!neighbor.equals(parentNode)) {
                return true;
            }
        }

        // Remove the current node from visited set after processing
        visited.remove(currentNode);
        return false;
    }


    public void clearGraph() {
        adjacencyList.clear();
        Log.d("Graph", "Graph cleared.");
    }

    public void printGraph() {
        Log.d("Graph", "Graph adjacency list: " + adjacencyList.toString());
    }
}
