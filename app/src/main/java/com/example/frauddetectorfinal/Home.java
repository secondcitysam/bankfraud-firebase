package com.example.frauddetectorfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.frauddetectorfinal.Fragments.AddTransaction;
import com.example.frauddetectorfinal.Fragments.FraudDetection;
import com.example.frauddetectorfinal.Fragments.SearchTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Home extends AppCompatActivity {


    private FirebaseAuth mAuth;

    private ChipNavigationBar cnb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mAuth = FirebaseAuth.getInstance();

        cnb = findViewById(R.id.cnb);

        cnb.setItemSelected(R.id.view,true);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new SearchTransaction()).commit();


        bottomMenu();
    }
    private void bottomMenu() {

        cnb.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;

                if(i==R.id.view)
                {
                    fragment =new SearchTransaction();

                }
                else if(i==R.id.add)
                {
                    fragment =new AddTransaction() ;

                } else if (i==R.id.record) {

                    fragment = new FraudDetection();

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();


            }
        });
    }
}