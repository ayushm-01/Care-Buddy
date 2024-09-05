package com.beffjezos.carebuddy.ui.activities;import android.app.ProgressDialog;import android.os.Bundle;import android.view.MenuItem;import android.view.View;import android.widget.AdapterView;import android.widget.ArrayAdapter;import android.widget.Button;import android.widget.Spinner;import android.widget.TextView;import android.widget.Toast;import androidx.annotation.NonNull;import androidx.appcompat.app.AppCompatActivity;import androidx.appcompat.widget.Toolbar;import com.afollestad.materialdialogs.DialogAction;import com.afollestad.materialdialogs.MaterialDialog;import com.beffjezos.carebuddy.R;import com.beffjezos.carebuddy.SqlDonor;import com.beffjezos.carebuddy.models.Donor;import com.google.android.gms.tasks.OnSuccessListener;import com.google.android.material.snackbar.Snackbar;import com.google.firebase.database.DatabaseReference;import com.google.firebase.database.FirebaseDatabase;import java.util.Objects;import static com.beffjezos.carebuddy.ui.activities.MainActivity.getRandomString;public class NewDonorReg extends AppCompatActivity implements AdapterView.OnItemSelectedListener {    String gender;    TextView nameTv, addressTv, numberTv, ageTv;    DatabaseReference mDb;    SqlDonor sqlDonor;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_new_donor_reg);        Toolbar toolbar = findViewById(R.id.toolbar);        setSupportActionBar(toolbar);        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);        getSupportActionBar().setDisplayShowHomeEnabled(true);        sqlDonor = new SqlDonor(this);        final Spinner spinner = findViewById(R.id.spinner);        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.genders));        arrayAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);        spinner.setAdapter(arrayAdapter);        spinner.setOnItemSelectedListener(this);        mDb = FirebaseDatabase.getInstance().getReference();        nameTv = findViewById(R.id.name);        addressTv = findViewById(R.id.address_name);        numberTv = findViewById(R.id.numeber_name);        ageTv = findViewById(R.id.age_name);        Button submit = findViewById(R.id.submit);        submit.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                String name = nameTv.getText().toString();                String address = addressTv.getText().toString();                String number = numberTv.getText().toString();                String age = ageTv.getText().toString();                if (name.length() < 3) {                    Toast.makeText(NewDonorReg.this, "Name is Empty", Toast.LENGTH_SHORT).show();                } else if (address.length() < 4) {                    Toast.makeText(NewDonorReg.this, "Address is Empty", Toast.LENGTH_SHORT).show();                } else if (number.length() < 11 || number.length() > 14) {                    Toast.makeText(NewDonorReg.this, "Numbers is error", Toast.LENGTH_SHORT).show();                } else {                    createNewDonor(name, address, number, gender, age);                }            }        });    }    private void createNewDonor(final String name, final String address, final String number, final String gender, final String age) {        final ProgressDialog mDialog;        mDialog = new ProgressDialog(this);        mDialog.setMessage("Please wait..");        mDialog.setIndeterminate(true);        mDialog.setCancelable(false);        mDialog.setCanceledOnTouchOutside(false);        mDialog.show();        String id = getRandomString();        Donor donor = new Donor(id, name, address, number, gender, age);        mDb.child("Donors").child(id).setValue(donor).addOnSuccessListener(new OnSuccessListener<Void>() {            @Override            public void onSuccess(Void aVoid) {                sqlDonor.insertContact(name, address, number, age, gender);                Toast.makeText(NewDonorReg.this, "Successfully submitted", Toast.LENGTH_SHORT).show();                mDialog.dismiss();                finish();            }        });    }    @Override    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {        gender = parent.getItemAtPosition(position).toString();    }    public void onNothingSelected(AdapterView<?> arg0) {        Snackbar.make(findViewById(R.id.layout), "Gender (optional)", Snackbar.LENGTH_SHORT).show();    }    @Override    public void onBackPressed() {        new MaterialDialog.Builder(this)                .title("Discard")                .content("Are you sure do you want to go back?")                .positiveText("Yes")                .canceledOnTouchOutside(false)                .cancelable(false)                .onPositive(new MaterialDialog.SingleButtonCallback() {                    @Override                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {                        NewDonorReg.this.finish();                    }                })                .negativeText("No")                .show();    }    @Override    public boolean onOptionsItemSelected(MenuItem item) {        switch (item.getItemId()) {            case android.R.id.home:                onBackPressed();                return true;        }        return super.onOptionsItemSelected(item);    }}