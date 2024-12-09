package com.example.frauddetectorfinal.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frauddetectorfinal.Models.Graph;
import com.example.frauddetectorfinal.Models.Transaction;
import com.example.frauddetectorfinal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FraudDetection extends Fragment {

    private ListView transactionsListView;
    private TextView resultTextView;
    private DatabaseReference transactionsRef;
    private Graph graph;
    private List<String> transactionIDs;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fraud_detection, container, false);

        transactionsListView = v.findViewById(R.id.transactionsListView);
        resultTextView = v.findViewById(R.id.resultTextView);

        transactionsRef = FirebaseDatabase.getInstance().getReference("transactions");
        graph = new Graph();
        transactionIDs = new ArrayList<>();

        loadTransactions();
        return v;
    }

    private void loadTransactions() {
        transactionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                transactionIDs.clear();

                // Loop through the transactions and add their IDs to the list
                for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                    String transactionID = transactionSnapshot.getKey();
                    transactionIDs.add(transactionID);
                }

                // Set up the adapter for the ListView
                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, transactionIDs);
                transactionsListView.setAdapter(adapter);

                // Set the item click listener
                transactionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the transaction ID clicked in the ListView
                        String transactionID = transactionIDs.get(position);
                        // Pass the selected transaction ID to the fraud detection function
                        checkForFraud(transactionID, dataSnapshot);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load transactions.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkForFraud(String transactionID, DataSnapshot dataSnapshot) {
        graph.clearGraph(); // Clear the graph before each fraud check

        // Add all transactions to the graph
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Transaction transaction = snapshot.getValue(Transaction.class);
            if (transaction != null) {
                Log.d("FraudDetection", "Adding transaction: " + transaction.getFromAccount() + " -> " + transaction.getToAccount());

                // Add the transaction to the graph if it's not a self-loop
                if (!transaction.getFromAccount().equals(transaction.getToAccount())) {
                    graph.addTransaction(transaction.getFromAccount(), transaction.getToAccount());
                }
            }
        }

        graph.printGraph(); // Debug log to print the current graph

        // Get the selected transaction object using transactionID
        Transaction selectedTransaction = getTransactionByID(transactionID, dataSnapshot);

        if (selectedTransaction != null) {
            // Check for fraud using the selected transaction's accounts
            boolean isFraudulent = graph.isFraudulentTransaction(selectedTransaction.getFromAccount(), selectedTransaction.getToAccount());

            // Update the UI based on the fraud check result
            if (isFraudulent) {
                resultTextView.setText("Fraud detected from account " + selectedTransaction.getFromAccount() + " to account " + selectedTransaction.getToAccount());
                Toast.makeText(getContext(), "Fraud detected from " + selectedTransaction.getFromAccount() + " to " + selectedTransaction.getToAccount(), Toast.LENGTH_LONG).show();
            } else {
                resultTextView.setText("No fraud detected for transaction: " + transactionID);
                Toast.makeText(getContext(), "No fraud detected.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Transaction getTransactionByID(String transactionID, DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Transaction transaction = snapshot.getValue(Transaction.class);
            if (transaction != null && transaction.getTransactionID().equals(transactionID)) {
                return transaction;
            }
        }
        return null;
    }
}
