package appdev.obenita.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class medicineList extends AppCompatActivity{

    Button add;
    MedicineModel medicineModel;
    OpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_list);

        add = (Button)findViewById(R.id.addBtn);

        helper = new OpenHelper(this);
        medicineModel = new MedicineModel();

        display();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    public void add(){
        startActivity(new Intent(medicineList.this, medicineDetails.class));
        finish();
    }

    private void display (){
        Cursor data = helper.getData();

        /*while (data.moveToNext()){
            med.setText(data.getString(1));
            dosage.setText(data.getString(2));
            often.setText(data.getString(3));
            take.setText(data.getString(4));
            duration.setText(data.getString(5));
        }*/

        String[] columns = new String[]{
                helper.COLUMN_MEDICINE_NAME,
                helper.COLUMN_DOSAGE,
                helper.COLUMN_OFTEN,
                helper.COLUMN_TAKE,
                helper.COLUMN_DURATION,
                helper.COLUMN_DATE
        };

        int[] to = new int[]{
                R.id.txtMedicine,
                R.id.dosageTxt,
                R.id.takeTxt,
                R.id.oftenTxt,
                R.id.durationTxt,
                R.id.dateTxt
        };


        SimpleCursorAdapter dataAdapt = new SimpleCursorAdapter(this, R.layout.prescription_layout, data, columns, to, 0);

        ListView listView = (ListView)findViewById(R.id.listView1);
        listView.setAdapter(dataAdapt);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(medicineList.this, Main2Activity.class));
        finish();
    }

}
