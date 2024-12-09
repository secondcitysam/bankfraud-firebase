package com.example.frauddetectorfinal.Models;

public class BST {
    private static BST instance; // Singleton instance
    private Node root;

    private class Node {
        String transactionID;
        Node left, right;

        Node(String transactionID) {
            this.transactionID = transactionID;
            this.left = this.right = null;
        }
    }

    private BST() {
        // Private constructor
    }

    public static BST getInstance() {
        if (instance == null) {
            instance = new BST();
        }
        return instance;
    }

    public void insert(String transactionID) {
        root = insertRec(root, transactionID);
    }

    private Node insertRec(Node root, String transactionID) {
        if (root == null) {
            root = new Node(transactionID);
            return root;
        }

        if (transactionID.compareTo(root.transactionID) < 0)
            root.left = insertRec(root.left, transactionID);
        else if (transactionID.compareTo(root.transactionID) > 0)
            root.right = insertRec(root.right, transactionID);

        return root;
    }

    public boolean search(String transactionID) {
        return searchRec(root, transactionID);
    }

    private boolean searchRec(Node root, String transactionID) {
        if (root == null) {
            return false;
        }
        if (root.transactionID.equals(transactionID)) {
            return true;
        }
        return transactionID.compareTo(root.transactionID) < 0
                ? searchRec(root.left, transactionID)
                : searchRec(root.right, transactionID);
    }

    public void printInOrder() {
        printInOrderRec(root);
    }

    private void printInOrderRec(Node root) {
        if (root != null) {
            printInOrderRec(root.left);
            System.out.println("BST Transaction ID: " + root.transactionID);
            printInOrderRec(root.right);
        }
    }
}
