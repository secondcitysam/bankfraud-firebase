package com.example.frauddetectorfinal.Fragments;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.example.frauddetectorfinal.Models.BST;
import com.example.frauddetectorfinal.Models.Transaction;
import com.example.frauddetectorfinal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AddTransaction extends Fragment {

    private EditText transactionIDEditText, fromAccountEditText, toAccountEditText, amountEditText;
    private AppCompatButton btn;
    private FirebaseDatabase database;
    private DatabaseReference transactionsRef;
    private BST bst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        transactionIDEditText = v.findViewById(R.id.tid);
        fromAccountEditText = v.findViewById(R.id.accid);
        toAccountEditText = v.findViewById(R.id.accid2);
        amountEditText = v.findViewById(R.id.amount);
        btn = v.findViewById(R.id.addbtn);

        database = FirebaseDatabase.getInstance();
        transactionsRef = database.getReference("transactions");

        bst = BST.getInstance();
        initializeBSTFromFirebase();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction();
            }
        });

        return v;
    }

    private void initializeBSTFromFirebase() {
        transactionsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String transactionID = snapshot.getKey();
                    bst.insert(transactionID);
                }
            }
        });
    }

    private void addTransaction() {
        try {
            String transactionID = transactionIDEditText.getText().toString();
            String fromAccount = fromAccountEditText.getText().toString();
            String toAccount = toAccountEditText.getText().toString();
            double amount = Double.parseDouble(amountEditText.getText().toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            String timestamp = sdf.format(new Date());

            Transaction transaction = new Transaction(transactionID, fromAccount, toAccount, amount, timestamp);
            transactionsRef.child(transactionID).setValue(transaction);

            bst.insert(transactionID);

            transactionIDEditText.setText("");
            fromAccountEditText.setText("");
            toAccountEditText.setText("");
            amountEditText.setText("");

            Toast.makeText(getContext(), "Transaction Added Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error adding transaction: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
