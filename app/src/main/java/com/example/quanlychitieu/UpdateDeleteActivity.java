package com.example.quanlychitieu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class UpdateDeleteActivity extends AppCompatActivity implements View.OnClickListener{

    public Spinner spCategory;
    private EditText eTitle, ePrice, eDate;
    private Button btnUpdate, btnBack, btnDelete;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        initView();
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        eDate.setOnClickListener(this);
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        eTitle.setText(item.getTitle());
        ePrice.setText(item.getPrice());
        eDate.setText(item.getDate());
        int p = 0;
        for(int i = 0; i < spCategory.getCount(); i++)
        {
            if(spCategory.getItemAtPosition(i).toString().equalsIgnoreCase(item.getCategory()))
            {
                p = i;
                break;
            }
        }
        spCategory.setSelection(p);
    }

    private void initView() {
        spCategory = findViewById(R.id.spCategory);
        eTitle = findViewById(R.id.eTitle);
        ePrice = findViewById(R.id.ePrice);
        eDate = findViewById(R.id.eDate);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);

        spCategory.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner, getResources().getStringArray(R.array.category)));

    }

    @Override
    public void onClick(View view) {
        SQLiteHelper db = new SQLiteHelper(this);
        if(view == eDate)
        {
            if(view == eDate) {
                final Calendar c = Calendar.getInstance(); // lay ngay hien thoi
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(UpdateDeleteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        String date = "";
                        if (m > 8) {
                            if (d > 9) {
                                date = d + "/" + (m + 1) + "/" + y;
                            } else {
                                date = "0" + d + "/" + (m + 1) + "/" + y;
                            }

                        } else {
                            if (d > 9) {
                                date = d + "/0" + (m + 1) + "/" + y;
                            } else {
                                date = "0" + d + "/0" + (m + 1) + "/" + y;
                            }
                        }
                        eDate.setText(date);
                    }
                }, year, month, day);
                dialog.show();
            }
        }
        if(view == btnBack)
        {
            finish();
        }
        if(view == btnUpdate)
        {
            String t = eTitle.getText().toString();
            String p = ePrice.getText().toString();
            String c = spCategory.getSelectedItem().toString();
            String d = eDate.getText().toString();
            if(!t.isEmpty() && p.matches("\\d+")){
                int id = item.getId();
                Item i = new Item(id ,t, c, p ,d);
                int result = db.update(i);
                if (result == -1) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
            }
        }
        if(view == btnDelete)
        {
            int id = item.getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Notify Delete");
            builder.setMessage("Are you sure delete "+ item.getTitle() +" ?");
            builder.setIcon(R.drawable.ic_remove);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db.delete(id);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}