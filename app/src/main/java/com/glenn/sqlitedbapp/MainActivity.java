package com.glenn.sqlitedbapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText etStdntID, etStdntName, etStdntProg;
    //Button btAdd, btDelete, btSearch, btView;
    SQLiteDatabase db;
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
        etStdntID = (EditText)findViewById(R.id.etStdntID);
        etStdntName = (EditText)findViewById(R.id.etStdntName);
        etStdntProg = (EditText)findViewById(R.id.etStdntProg);

        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(stdnt_id VARCHAR, stdnt_name VARCHAR, stdnt_prog VARCHAR);");
    }
    public void clearText() {
        etStdntName.setText(null);
        etStdntID.setText(null);
        etStdntProg.setText(null);
        etStdntID.requestFocus();
    }
    public void showMessage(String title, String message) {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void ViewAllOnClick(View view) {
        // Retrieving all records
        Cursor c=db.rawQuery("SELECT * FROM student", null);
        // Checking if no records found
        if(c.getCount() == 0) {
            showMessage("Error", "No records found.");
            return;
        }
        // Appending records to a string buffer
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()) {
            buffer.append("ID: " + c.getString(0) + "\n");
            buffer.append("Name: " + c.getString(1) + "\n");
            buffer.append("Program: " + c.getString(2) + "\n\n");
        }
        // Displaying all records
        showMessage("Student Details", buffer.toString());
    }

    public void DeleteOnClick(View view) {
        Cursor c=db.rawQuery("SELECT * FROM student WHERE stdnt_id='" + etStdntID.getText() + "'", null);
        if(c.moveToFirst())
        {
            db.execSQL("DELETE FROM student WHERE stdnt_id='" + etStdntID.getText() + "'");
            showMessage("Success", "Record Deleted");
            clearText();
        }
    }

    public void AddOnClick(View view) {
        db.execSQL("INSERT INTO student VALUES('" + etStdntID.getText() +  "','" + etStdntName.getText() + "','" + etStdntProg.getText() + "');");
        showMessage("Success", "Record Added");
        clearText();
    }

    public void SearchOnClick(View view) {
        Cursor c=db.rawQuery("SELECT * FROM student WHERE stdnt_id='" + etStdntID.getText() + "'", null);
        StringBuffer buffer = new StringBuffer();
        if(c.moveToFirst()) {
            buffer.append("Name: ").append(c.getString(1)).append("\n");
            buffer.append("Program: ").append(c.getString(2)).append("\n\n");
        }
        // Displaying all records
        showMessage("Student Details", buffer.toString());
    }
}