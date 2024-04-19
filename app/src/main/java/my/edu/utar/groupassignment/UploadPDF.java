package my.edu.utar.groupassignment;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UploadPDF extends AppCompatActivity {

    Button upload_btn;
    EditText subject_comments, otherSubjectName;

    Spinner subjectSpinner, yearSpinner, trimesterSpinner;
    TextView display_SelectedFile;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    String selectedYear, selectedSubject, selectedTrimester;

    private final OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

        upload_btn = findViewById(R.id.upload_btn);
        subject_comments = findViewById(R.id.subject_comment_text);
        display_SelectedFile = findViewById(R.id.display_upload_file);
        subjectSpinner = findViewById(R.id.subject_name_spinner);
        yearSpinner = findViewById(R.id.subject_year_spinner);
        trimesterSpinner = findViewById(R.id.subject_tri_spinner);
        otherSubjectName = findViewById(R.id.other_subject_edit_text);

        //*Start of Subject Spinner*
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.subjects_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedSubject = parentView.getItemAtPosition(position).toString();
                // Do something with the selected subject
                if (selectedSubject.equals("Other")) {
                    otherSubjectName.setVisibility(View.VISIBLE);
                } else {
                    otherSubjectName.setVisibility(View.GONE);
                }
                // For example, you can display it or use it in further processing
                Log.d("SelectedSubject", selectedSubject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing when nothing is selected
            }

        });
        //*End of Subject Name Spinner*

        //*Start of Subject Year Spinner*
        // Get the current year
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        // Populate spinner with the latest 10 years
        List<String> years = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            years.add(String.valueOf(currentYear - i));
        }

        // Create adapter and set it to the spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedYear = parentView.getItemAtPosition(position).toString();
                // Do something with the selected year
                // For example, you can display it or use it in further processing
                Log.d("SelectedYear", selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing when nothing is selected
            }
        });
        //*End of subject year spinner*

        //*Start of subject trimester spinner*
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> Triadapter = ArrayAdapter.createFromResource(this,
                R.array.trimesters_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        Triadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        trimesterSpinner.setAdapter(Triadapter);

        trimesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedTrimester = parentView.getItemAtPosition(position).toString();
                // Do something with the selected year
                // For example, you can display it or use it in further processing
                Log.d("SelectedTrimester", selectedTrimester);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing when nothing is selected
            }
        });
        //*End of subject trimester spinner*

        //Database
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFiles();
            }
        });

        onBackPressedDispatcher.addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(UploadPDF.this, home_page.class);
                startActivity(intent);

                finish();
            }
        });

    }

    private void selectFiles() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF files... "), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedFileUri = data.getData();

            String filename = getFileName(selectedFileUri);

            display_SelectedFile.setText("Selected filename : " + filename);

            UploadFiles(data.getData());
        }

    }

    private void UploadFiles(Uri data) {

        if (otherSubjectName.getVisibility() == View.VISIBLE) {
            selectedSubject = otherSubjectName.getText().toString();
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        String pastyearpaper = "Past Year Paper";
        String subject = selectedSubject;
        String year = selectedYear;
        String month = selectedTrimester;


        //Check path exist
        databaseReference.child(pastyearpaper).child(subject).child(String.valueOf(year)).child(month)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Path already exists, show an error message
                            Toast.makeText(UploadPDF.this, "Past Year Paper Exist!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            StorageReference reference = storageReference.child("Uploads/" + System.currentTimeMillis() + ".pdf");
                            reference.putFile(data)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                            while (!uriTask.isComplete()) ;
                                            Uri url = uriTask.getResult();

                                            String comment = subject_comments.getText().toString();
                                            pdfClass pdfClass = new pdfClass(comment, url.toString());

                                            String uniqueKey = databaseReference.child(pastyearpaper).child(subject).child(String.valueOf(year)).child(month).push().getKey();

                                            // Set the value in the database with the unique key
                                            databaseReference.child(pastyearpaper).child(subject).child(String.valueOf(year)).child(month).child(uniqueKey).setValue(pdfClass);

                                            Toast.makeText(UploadPDF.this, "File Uploaded!!!", Toast.LENGTH_SHORT).show();

                                            progressDialog.dismiss();
                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                            progressDialog.setMessage("Uploaded:" + (int) progress + "%");


                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event
                        progressDialog.dismiss();
                    }
                });
    }

    // Method to get the file name from the URI
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
