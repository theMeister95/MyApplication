package appdev.obenita.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class medicineDetails extends AppCompatActivity {

    Button save;
    EditText medicine, dosage, often, take, duration;
    MedicineModel medicineModel;
    OpenHelper helper;
    Time today = new Time(Time.getCurrentTimezone());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_details);

        save = (Button)findViewById(R.id.saveBtn);
        medicine = (EditText)findViewById(R.id.etMedName);
        dosage = (EditText)findViewById(R.id.etDosage);
        often = (EditText)findViewById(R.id.etOften);
        take = (EditText)findViewById(R.id.etTake);
        duration = (EditText)findViewById(R.id.etDuration);

        helper = new OpenHelper(this);
        medicineModel = new MedicineModel();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                today.setToNow();
                String timestamp = today.format("%Y-%m-%d %H:%M:%S");

                if(TextUtils.isEmpty(medicine.getText().toString()) || TextUtils.isEmpty(dosage.getText().toString())
                        || TextUtils.isEmpty(often.getText().toString()) || TextUtils.isEmpty(take.getText().toString())
                        || TextUtils.isEmpty(duration.getText().toString())) {
                    Toast.makeText(medicineDetails.this, "Fill up all the details!", Toast.LENGTH_SHORT).show();
                }else{
                    medicineModel.setMedicine(medicine.getText().toString());
                    medicineModel.setDosage(dosage.getText().toString());
                    medicineModel.setOften(often.getText().toString());
                    medicineModel.setTake(take.getText().toString());
                    medicineModel.setDuration(duration.getText().toString());
                    medicineModel.setDate(timestamp);

                    boolean insert = helper.addPrescription(medicineModel);

                    if (insert) {
                        Toast.makeText(medicineDetails.this, "ADDED", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(medicineDetails.this, "NOT SUCCESS", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(medicineDetails.this, medicineList.class));
                    finish();

                }
            }
        });
    }

    public void onBackPressed() {
        startActivity(new Intent(medicineDetails.this, medicineList.class));
        finish();
    }


}
