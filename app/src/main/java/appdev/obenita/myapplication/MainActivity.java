package appdev.obenita.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private OpenHelper dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_medicines);

        dbHelper = new OpenHelper(this);

        dbHelper.deleteAllMedicine();
        dbHelper.insertMedicine();

        displayView();

    }

    public void displayView(){
        Cursor cursor = dbHelper.getAllMedicine();

        String[] columns = new String[] {
                dbHelper.KEY_MEDICINE_NAME,
                dbHelper.KEY_BRAND_NAME,
                dbHelper.KEY_DRUG_PURPOSE
        };

        int[] to = new int[] {
                R.id.gNameTxt,
                R.id.bNameTxt,
                R.id.purposeTxt
        };

        dataAdapter = new SimpleCursorAdapter(this, R.layout.list_layout, cursor, columns, to, 0);

        ListView listView = (ListView) findViewById(R.id.listView1);

        listView.setAdapter(dataAdapter);

        EditText myFilter = (EditText) findViewById(R.id.drugFilter);

        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.getMedicineByName(constraint.toString());
            }
        });

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this, Main2Activity.class));
        finish();
    }


}
