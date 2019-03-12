package com.example.restaurantlocator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddRestaurantActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button addNewrestaurant;
    private EditText inputRestaurantname, inputRestaurantDescription;
    private ImageView restaurantImage;
    private Spinner catagorySpinner;
    private Uri ImageUri;
    private String Rname, Rdescription, downloadImageUrl,Rcatagory, productRandomKey,saveCurrentDate, saveCurrentTime;
    private StorageReference restaurantImageRef;
    private DatabaseReference resrurantDatabaseRef;
    private ProgressDialog loadingBar;

        private static final int GalleryPick =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_restaurant);

        restaurantImageRef = FirebaseStorage.getInstance().getReference().child("Restaurant Images");
        resrurantDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Restaurants");


        inputRestaurantname = findViewById(R.id.restaurant_name);
        catagorySpinner = findViewById(R.id.restaurant_catagory);
        restaurantImage = findViewById(R.id.restaurant_image);
        inputRestaurantDescription = findViewById(R.id.restaurant_description);
        addNewrestaurant =  findViewById(R.id.buttonRegister);
        loadingBar = new ProgressDialog(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.RestaurantCatagories,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catagorySpinner.setAdapter(adapter);
        catagorySpinner.setOnItemSelectedListener(this);

        restaurantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Opengallery();

            }
        });

        addNewrestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ValidateRestaurant();

            }
        });

    }

    private void ValidateRestaurant()
    {
        Rname = inputRestaurantname.getText().toString();
        Rdescription = inputRestaurantDescription.getText().toString();
        Rcatagory= catagorySpinner.getSelectedItem().toString();
        //Rlocation = inputRestaurantDescription.getText().toString();

        if (ImageUri == null)
        {
            Toast.makeText(this, "Restaurant Image is Required", Toast.LENGTH_SHORT).show();

        }

        if (Rname.isEmpty()) {
            inputRestaurantname.setError("Restaurant name is required");
            inputRestaurantname.requestFocus();

        }

        if (Rdescription.isEmpty())
        {
            inputRestaurantDescription.setError("Restaurant description is required");
            inputRestaurantDescription.requestFocus();

        }
        else
        {
            storedProductInformation();

        }


    }

    private void storedProductInformation()
    {
        loadingBar.setTitle("Add New Restaurant");
        loadingBar.setMessage("Dear Admin, please wait while your restaurant is being registered");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        productRandomKey = saveCurrentDate + saveCurrentTime;
        final StorageReference filepath = restaurantImageRef.child(ImageUri.getLastPathSegment()+productRandomKey+".jpg");
        final UploadTask uploadTask = filepath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message =e.toString();
                Toast.makeText(AdminAddRestaurantActivity.this,"Error:" +message,Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminAddRestaurantActivity.this,"Image Uploaded Sucessfully",Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddRestaurantActivity.this,"Got Image Url Sucessfully",Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }

                    }
                });

            }
        });
    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String,Object> restaurantMap = new HashMap<>();
        restaurantMap.put("pid", productRandomKey);
        restaurantMap.put("Rname", Rname);
        restaurantMap.put("Rdetails",Rdescription);
        restaurantMap.put("Catagory",Rcatagory);
        restaurantMap.put("Image",downloadImageUrl);


        resrurantDatabaseRef.child(productRandomKey).updateChildren(restaurantMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(AdminAddRestaurantActivity.this, NavigationDrawer.class);
                    startActivity(intent);

                    loadingBar.dismiss();
                    Toast.makeText(AdminAddRestaurantActivity.this, "Restaurant is added successfully..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddRestaurantActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();

                }


            }
        });



    }

    private void Opengallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPick && resultCode == RESULT_OK && data != null)
        {
            ImageUri = data.getData();
            restaurantImage.setImageURI(ImageUri);


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Rcatagory= catagorySpinner.getSelectedItem().toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
