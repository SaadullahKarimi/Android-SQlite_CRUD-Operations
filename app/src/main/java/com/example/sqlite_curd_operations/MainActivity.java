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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_add, btnupdate, showall, btnSearch;
    EditText ID, UserName, Password, searchID;
    ListView listView;
    DatabaseConnection databaseConnection;
    DataModel dataModel;
    ArrayAdapter<DataModel> CustomerArrayAdpter;
    List<DataModel> allCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize view
        btn_add = findViewById(R.id.btnsave);
        btnupdate = findViewById(R.id.btnUpdate);
        showall = findViewById(R.id.DataShow);
        searchID = findViewById(R.id.et_search_id);
        btnSearch = findViewById(R.id.btnSearch);
        listView = findViewById(R.id.lstview);
        ID = findViewById(R.id.et_ID);
        UserName = findViewById(R.id.et_name);
        Password = findViewById(R.id.et_Name2);

        databaseConnection = new DatabaseConnection(MainActivity.this);

        // Add customer
        btn_add.setOnClickListener(v -> {
            try {
                dataModel = new DataModel(-1, UserName.getText().toString(),
                        Integer.parseInt(Password.getText().toString()));
                Toast.makeText(MainActivity.this, dataModel.toString(), Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                dataModel = new DataModel(-1, "error", 0);
            }

            boolean success = databaseConnection.addOne(dataModel);
            Toast.makeText(MainActivity.this, "Success=" + success, Toast.LENGTH_SHORT).show();
        });

        // Update customer
        btnupdate.setOnClickListener(v -> {
            try {
                dataModel = new DataModel(Integer.parseInt(ID.getText().toString()),
                        UserName.getText().toString(),
                        Integer.parseInt(Password.getText().toString()));
                boolean success = databaseConnection.updateOne(dataModel);
                Toast.makeText(MainActivity.this, "Success=" + success, Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Error=" + ex, Toast.LENGTH_SHORT).show();
            }
        });

        // Show all customers
        showall.setOnClickListener(v -> {
            allCustomers = databaseConnection.getEveryOne();
            CustomerArrayAdpter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, allCustomers);
            listView.setAdapter(CustomerArrayAdpter);
        });

        // Delete customer
        listView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                DataModel clickCustomer = (DataModel) parent.getItemAtPosition(position);
                databaseConnection.deleteOne(clickCustomer);
                Toast.makeText(MainActivity.this, "Data deleted: " + clickCustomer, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        });

        // Search for a customer
        btnSearch.setOnClickListener(v -> {
            String query = searchID.getText().toString().toLowerCase();
            filter(query);
        });
    }

    private void filter(String query) {
        // Clear the previous data in the filtered list
        List<DataModel> filteredList = new ArrayList<>();

        // Retrieve data from the database based on search query
        allCustomers = databaseConnection.getEveryOne();  // Get all data, or implement a search method in DB

        try {
            // Try to parse the query as an integer for ID search
            int searchId = Integer.parseInt(query);

            // Search by ID
            for (DataModel customer : allCustomers) {
                if (customer.getId() == searchId) {
                    filteredList.add(customer);
                    break;  // Exit loop once a match is found for the ID
                }
            }

        } catch (NumberFormatException e) {
            // If it's not a number, perform a search by name
            for (DataModel customer : allCustomers) {
                if (customer.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(customer);
                }
            }
        }


        // If nothing matches, show a message in the list
        if (filteredList.isEmpty()) {
            Toast.makeText(MainActivity.this, "Customer not found", Toast.LENGTH_SHORT).show();
        } else {
            // Update the listView with filtered data
            CustomerArrayAdpter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, filteredList);
            listView.setAdapter(CustomerArrayAdpter);
        }
    }
}

