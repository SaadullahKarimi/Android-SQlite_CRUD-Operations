package com.example.sqlite_curd_operations;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btn_add, btnupdate, showall, btnSearch;
    EditText ID, UserName, Password, searchID;
    ListView lstshowdata;
    DatabaseConnection databaseConnection;
    DataModel datamodel;
    ArrayAdapter<DataModel> CustomerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_add = findViewById(R.id.btnsave);
        showall = findViewById(R.id.DataShow);
        ID = findViewById(R.id.et_ID);
        UserName = findViewById(R.id.et_name);
        Password = findViewById(R.id.et_name2);
        searchID = findViewById(R.id.et_search_id);
        btnSearch = findViewById(R.id.btnSearch);
        lstshowdata = findViewById(R.id.lstview);
        btnupdate = findViewById(R.id.btnUpdate);
        databaseConnection = new DatabaseConnection(MainActivity.this);

        btn_add.setOnClickListener(v -> {
            try {
                datamodel = new DataModel(-1, UserName.getText().toString(), Integer.parseInt(Password.getText().toString()));
                Toast.makeText(MainActivity.this, datamodel.toString(), Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                datamodel = new DataModel(-1, "error", 0);
            }

            boolean success = databaseConnection.addOne(datamodel);
            Toast.makeText(MainActivity.this, "Success=" + success, Toast.LENGTH_SHORT).show();
        });

        btnupdate.setOnClickListener(v -> {
            try {
                datamodel = new DataModel(Integer.parseInt(ID.getText().toString()), UserName.getText().toString(), Integer.parseInt(Password.getText().toString()));
                Toast.makeText(MainActivity.this, datamodel.toString(), Toast.LENGTH_SHORT).show();
                boolean success = databaseConnection.updateOne(datamodel);
                Toast.makeText(MainActivity.this, "Success=" + success, Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Error" + ex, Toast.LENGTH_SHORT).show();
            }
        });

        showall.setOnClickListener(v -> {
            ArrayList<DataModel> allCustomers = (ArrayList<DataModel>) databaseConnection.getEveryOne();
            CustomerArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, allCustomers);
            lstshowdata.setAdapter(CustomerArrayAdapter);
        });

        lstshowdata.setOnItemClickListener((parent, view, position, id) -> {
            try {
                DataModel clickedCustomer = (DataModel) parent.getItemAtPosition(position);
                databaseConnection.deleteOne(clickedCustomer);
                Toast.makeText(MainActivity.this, "Data deleted " + clickedCustomer, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        });

        btnSearch.setOnClickListener(v -> {
            try {
                int searchId = Integer.parseInt(searchID.getText().toString());
                DataModel result = databaseConnection.getCustomerById(searchId);
                if (result != null) {
                    Toast.makeText(MainActivity.this, "Found: " + result.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Customer not found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Error: " + ex, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
