package com.example.frauddetectorfinal.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.frauddetectorfinal.Models.BST;
import com.example.frauddetectorfinal.Models.Transaction;
import com.example.frauddetectorfinal.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchTransaction extends Fragment {

    private EditText searchTransactionIDEditText;
    private TextView resultTextView;
    private BST bst;
    private DatabaseReference transactionsRef;
    private ImageButton btn;

    public SearchTransaction() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_transaction, container, false);

        searchTransactionIDEditText = v.findViewById(R.id.sid);
        resultTextView = v.findViewById(R.id.rid);
        btn = v.findViewById(R.id.imgbtn);

        bst = BST.getInstance();
        transactionsRef = FirebaseDatabase.getInstance().getReference("transactions");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchT();
            }
        });

        return v;
    }

    private void searchT() {
        String transactionID = searchTransactionIDEditText.getText().toString().trim();

        boolean exists = bst.search(transactionID);
        if (exists) {
            transactionsRef.child(transactionID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Transaction transaction = task.getResult().getValue(Transaction.class);
                    if (transaction != null) {
                        resultTextView.setText(transaction.toString());
                    } else {
                        resultTextView.setText("Transaction data is null.");
                    }
                } else {
                    resultTextView.setText("Failed to fetch data.");
                }
            });
        } else {
            resultTextView.setText("Transaction not found.");
            Toast.makeText(getContext(), "Transaction not found in database.", Toast.LENGTH_SHORT).show();
        }
    }
}
