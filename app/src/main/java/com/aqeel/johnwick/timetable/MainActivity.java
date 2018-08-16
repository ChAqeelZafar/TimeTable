package com.aqeel.johnwick.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    ChipGroup chipGroup;
    FirebaseFirestore firestore;
    List<String> coursesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.main_spinner_subjects);
        chipGroup = findViewById(R.id.main_chipGroup_subjects);

        chipGroup.setChipSpacing(5);


       getCoursesList();



        String items[] = {"aqeel", "zafar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, coursesList);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this, adapterView.getSelectedItem()+ "", Toast.LENGTH_LONG ).show();
                addChip(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    void addChip(String chipText){
        final Chip chip = new Chip(MainActivity.this );
        chip.setText(chipText);
        chip.setCloseIconEnabled(true);
        chip.setCloseIconResource(R.drawable.close);
        chip.setTextAppearanceResource(R.style.ChipTextStyle);



        chipGroup.addView(chip);


    }

    void getCoursesList(){
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("uni")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("name").equals("National University of Computer and Emerging Sciences")){
                                    //getCourses(document.getId());


                                    firestore.collection("uni").document(document.getId()).collection("courses")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            coursesList.add(document.get("name").toString());
                                                        }
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "Error getting documents: 2 ", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });

                                }

                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error getting documents: 1 ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
