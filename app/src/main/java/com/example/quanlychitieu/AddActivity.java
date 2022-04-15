package com.example.quanlychitieu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlychitieu.dal.SQLiteHelper;
import com.example.quanlychitieu.model.Item;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{

    public Spinner spCategory;
    private EditText eTitle, ePrice, eDate;
    private Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        eDate.setOnClickListener(this);
    }

    private void initView() {
        spCategory = findViewById(R.id.spCategory);
        eTitle = findViewById(R.id.eTitle);
        ePrice = findViewById(R.id.ePrice);
        eDate = findViewById(R.id.eDate);

        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);

        spCategory.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner, getResources().getStringArray(R.array.category)));

    }

    @Override
    public void onClick(View view) {
        if(view == eDate)
        {
            final Calendar c= Calendar.getInstance(); // lay ngay hien thoi
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date = "";
                    if(m > 8)
                    {
                        if(d > 9)
                        {
                            date = d + "/" + (m + 1) + "/" + y;
                        }
                        else{
                            date = "0" + d + "/" + (m + 1) + "/" + y;
                        }

                    }
                    else{
                        if(d > 9){
                            date = d + "/0" + (m + 1) + "/" + y;
                        }
                        else {
                            date = "0" + d + "/0" + (m + 1) + "/" + y;
                        }
                    }
                    eDate.setText(date);
                }
            }, year, month, day);
            dialog.show();
        }
        if(view == btnCancel)
        {
            finish();
        }
        if(view == btnAdd)
        {
            String t = eTitle.getText().toString();
            String p = ePrice.getText().toString();
            String c = spCategory.getSelectedItem().toString();
            String d = eDate.getText().toString();
            if(!t.isEmpty() && p.matches("\\d+")){
                Item i = new Item(t, c, p ,d);
                SQLiteHelper db = new SQLiteHelper(this);
                long result = db.addItem(i);
                if (result == -1) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }

            }
        }
    }
}