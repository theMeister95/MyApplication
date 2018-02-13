package appdev.obenita.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class doctorDetails extends AppCompatActivity{

    Button add;
    MedicineModel medicineModel;
    OpenHelper helper;
    EditText name, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        add = (Button)findViewById(R.id.addDoctorBtn);
        name = (EditText)findViewById(R.id.etPhysiciansName);
        number = (EditText)findViewById(R.id.etPhoneNumber);

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

    private void add(){

        medicineModel.setDoctorName(name.getText().toString());
        medicineModel.setNumber(number.getText().toString());

        if(TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(doctorDetails.this, "Fill up the details!", Toast.LENGTH_SHORT).show();
        }else{

            boolean insert = helper.addDoctorDetails(medicineModel);

            if(insert){
                Toast.makeText(doctorDetails.this, "ADDED", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(doctorDetails.this, "NOT SUCCESS", Toast.LENGTH_SHORT).show();
            }
        }
        display();

    }

    private void display (){
        Cursor data = helper.getDoctorData();

        /*while (data.moveToNext()){
            med.setText(data.getString(1));
            dosage.setText(data.getString(2));
            often.setText(data.getString(3));
            take.setText(data.getString(4));
            duration.setText(data.getString(5));
        }*/

        String[] columns = new String[]{
                helper.COLUMN_DOCTOR_NAME,
                helper.COLUMN_CONTACT_NUMBER
        };

        int[] to = new int[]{
                R.id.txtDoctor,
                R.id.txtNumber
        };

        SimpleCursorAdapter dataAdapt = new SimpleCursorAdapter(this, R.layout.doctor_details_layout, data, columns, to, 0);

        ListView listView = (ListView)findViewById(R.id.listViewDoctor);
        listView.setAdapter(dataAdapt);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                //medicineModel.setDoctorID(cursor.getString(cursor.getColumnIndexOrThrow("_id")));

                startActivity(new Intent(doctorDetails.this, medicineList.class));
            }
        });

    }

}
