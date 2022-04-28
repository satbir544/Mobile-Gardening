package com.example.plantreapp.myPlants;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.plantreapp.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
//import com.example.plantreapp.TimerService;
import com.example.plantreapp.logs.AddLogActivity;
import com.example.plantreapp.logs.LogsActivity;

public class AddPlantActivity extends AppCompatActivity {
    private Button addBtn, cancelBtn;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2;
    ConstraintLayout moistureLayout, hourLayout;
    SeekBar seekbar1, seekbar2, seekbar3, seekbar4, seekbar5, seekbar6, seekbar7, seekbar8, seekbar9;
    TextView Seed_Max, Seed_min, Seeding_max, Seeding_min, Mature_max, Mature_min, Seed_hour, Seeding_hour, Mature_hour;

    private EditText nameTxt, scifiNameTxt, descriptionTxt , timmer;

    private AutoCompleteTextView stageTxt;
    private float maxSeedMoisture, minSeedMoisture,maxSeedingMoisture, minSeedingMoisture, maxMatureMoisture, minMatureMoisture , hourRateSeed, hourRateSeeding, hourRateMature;

    private ArrayAdapter<String> adapterItems;
    private int pos;

    private final String[] stages = {"Seed", "Seedling", "Mature"};


    private ImageView plantPic;
    private Button selectImgBtn;

    //permission constant
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE = 300;
    private static final int IMAGE_FROM_CAMERA_CODE = 400;

    // string array of permission
    private String[] cameraPermission;
    private String[] storagePermission;

    //Image uri var
    private Uri imageUri;
    private PlantInfo plantInfo;
    private com.example.plantreapp.entities.PlantInfo dbPlantInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Plant");

        //init permission
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init view
        plantPic = findViewById(R.id.plantPic);

        selectImgBtn = findViewById(R.id.select_img_button);
        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });
        // Check if plant info is being passed in
        Intent intent = getIntent();
        dbPlantInfo = intent.getParcelableExtra("PLANTINFO");


        nameTxt = findViewById(R.id.editPlantName);
        timmer = findViewById(R.id.waterTimmer);
        scifiNameTxt = findViewById(R.id.editScifiName);
        descriptionTxt = findViewById(R.id.editDescription);

         //setting stage items
        stageTxt = findViewById(R.id.stage_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.dropdown_item, stages);
        stageTxt.setAdapter(adapterItems);

        radioGroup=(RadioGroup)findViewById(R.id.radio);

        seekbar1=(SeekBar)findViewById(R.id.seekBar);
        seekbar2=(SeekBar)findViewById(R.id.seekBar2);
        seekbar3=(SeekBar)findViewById(R.id.seekBar3);
        seekbar4=(SeekBar)findViewById(R.id.seekBar4);
        seekbar5=(SeekBar)findViewById(R.id.seekBar5);
        seekbar6=(SeekBar)findViewById(R.id.seekBar6);
        seekbar7=(SeekBar)findViewById(R.id.seekBar7);
        seekbar8=(SeekBar)findViewById(R.id.seekBar8);
        seekbar9=(SeekBar)findViewById(R.id.seekBar9);

        Seed_Max =(TextView)findViewById(R.id.max_mositure);
        Seed_min =(TextView)findViewById(R.id.min_mositure);
        Seeding_max=(TextView)findViewById(R.id.Seeding_max_mositure);
        Seeding_min = (TextView)findViewById(R.id.Seeding_min_mositure);
        Mature_max = (TextView)findViewById(R.id.Mature_max_mositure);
        Mature_min = (TextView)findViewById(R.id.Mature_min_mositure);
        Seed_hour =(TextView)findViewById(R.id.Seed_hour);
        Seeding_hour =(TextView)findViewById(R.id.Seeding_hour);
        Mature_hour =(TextView)findViewById(R.id.Mature_hour);
        radioButton1 = findViewById(R.id.radio_one);
        radioButton2 = findViewById(R.id.radio_two);

        moistureLayout = findViewById(R.id.moistureLayout);
        hourLayout = findViewById(R.id.hourLayout);
        hourLayout.setVisibility(View.INVISIBLE);

        Seed_Max.setVisibility(View.INVISIBLE);
        Seed_min.setVisibility(View.INVISIBLE);
        Seeding_max.setVisibility(View.INVISIBLE);
        Seeding_min.setVisibility(View.INVISIBLE);
        Mature_max.setVisibility(View.INVISIBLE);
        Mature_min.setVisibility(View.INVISIBLE);

        seekbar1.setVisibility(View.INVISIBLE);
        seekbar2.setVisibility(View.INVISIBLE);
        seekbar3.setVisibility(View.INVISIBLE);
        seekbar4.setVisibility(View.INVISIBLE);
        seekbar5.setVisibility(View.INVISIBLE);
        seekbar6.setVisibility(View.INVISIBLE);

        Seed_hour.setVisibility(View.INVISIBLE);
        Seeding_hour.setVisibility(View.INVISIBLE);
        Mature_hour.setVisibility(View.INVISIBLE);
        seekbar7.setVisibility(View.INVISIBLE);
        seekbar8.setVisibility(View.INVISIBLE);
        seekbar9.setVisibility(View.INVISIBLE);

        if (dbPlantInfo != null) {
            scifiNameTxt.setText(dbPlantInfo.getScientific_name());
            descriptionTxt.setHint(dbPlantInfo.getDescription());
        }


        stageTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(radioButton1.isChecked()) {
                    if (i == 0) {
                        Seed_Max.setVisibility(View.VISIBLE);
                        Seed_min.setVisibility(View.VISIBLE);
                        Seeding_max.setVisibility(View.INVISIBLE);
                        Seeding_min.setVisibility(View.INVISIBLE);
                        Mature_max.setVisibility(View.INVISIBLE);
                        Mature_min.setVisibility(View.INVISIBLE);

                        seekbar1.setVisibility(View.VISIBLE);
                        seekbar2.setVisibility(View.VISIBLE);
                        seekbar3.setVisibility(View.INVISIBLE);
                        seekbar4.setVisibility(View.INVISIBLE);
                        seekbar5.setVisibility(View.INVISIBLE);
                        seekbar6.setVisibility(View.INVISIBLE);
                    }
                    if (i == 1) {
                        Seed_Max.setVisibility(View.INVISIBLE);
                        Seed_min.setVisibility(View.INVISIBLE);
                        Seeding_max.setVisibility(View.VISIBLE);
                        Seeding_min.setVisibility(View.VISIBLE);
                        Mature_max.setVisibility(View.INVISIBLE);
                        Mature_min.setVisibility(View.INVISIBLE);

                        seekbar1.setVisibility(View.INVISIBLE);
                        seekbar2.setVisibility(View.INVISIBLE);
                        seekbar3.setVisibility(View.VISIBLE);
                        seekbar4.setVisibility(View.VISIBLE);
                        seekbar5.setVisibility(View.INVISIBLE);
                        seekbar6.setVisibility(View.INVISIBLE);
                    }
                    if (i == 2) {
                        Seed_Max.setVisibility(View.INVISIBLE);
                        Seed_min.setVisibility(View.INVISIBLE);
                        Seeding_max.setVisibility(View.INVISIBLE);
                        Seeding_min.setVisibility(View.INVISIBLE);
                        Mature_max.setVisibility(View.VISIBLE);
                        Mature_min.setVisibility(View.VISIBLE);

                        seekbar1.setVisibility(View.INVISIBLE);
                        seekbar2.setVisibility(View.INVISIBLE);
                        seekbar3.setVisibility(View.INVISIBLE);
                        seekbar4.setVisibility(View.INVISIBLE);
                        seekbar5.setVisibility(View.VISIBLE);
                        seekbar6.setVisibility(View.VISIBLE);
                    }
                }
                 if (radioButton2.isChecked())
                {
                    if(i==0)
                    {
                        Seed_hour.setVisibility(View.VISIBLE);
                        Seeding_hour.setVisibility(View.INVISIBLE);
                        Mature_hour.setVisibility(View.INVISIBLE);
                        seekbar7.setVisibility(View.VISIBLE);
                        seekbar8.setVisibility(View.INVISIBLE);
                        seekbar9.setVisibility(View.INVISIBLE);
                    }
                    if(i==1)
                    {
                        Seed_hour.setVisibility(View.INVISIBLE);
                        Seeding_hour.setVisibility(View.VISIBLE);
                        Mature_hour.setVisibility(View.INVISIBLE);
                        seekbar7.setVisibility(View.INVISIBLE);
                        seekbar8.setVisibility(View.VISIBLE);
                        seekbar9.setVisibility(View.INVISIBLE);
                    }
                    if(i==2)
                    {
                        Seed_hour.setVisibility(View.INVISIBLE);
                        Seeding_hour.setVisibility(View.INVISIBLE);
                        Mature_hour.setVisibility(View.VISIBLE);
                        seekbar7.setVisibility(View.INVISIBLE);
                        seekbar8.setVisibility(View.INVISIBLE);
                        seekbar9.setVisibility(View.VISIBLE);
                    }
                }


            }
        });
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Seed_Max.setText("Seed Max Soil Moisture : " + String.valueOf(progress));
                maxSeedMoisture = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Seed_min.setText("Seed Min Soil Moisture : " + String.valueOf(progress));
                minSeedMoisture= progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Seeding_max.setText("Seeding Max Soil Moisture : " + String.valueOf(progress));
                maxSeedingMoisture= progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Seeding_min.setText("Seeding Max Soil Moisture : " + String.valueOf(progress));
                minSeedingMoisture=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Mature_max.setText("Mature Max Soil Moisture : " + String.valueOf(progress));
                maxMatureMoisture= progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Mature_min.setText("Mature Min Soil Moisture : " + String.valueOf(progress));
                minMatureMoisture=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar7.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Seed_hour.setText("Seed Water Rate (/hr) : " + String.valueOf(progress));
                hourRateSeed=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar8.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Seeding_hour.setText("Seeding Water Rate (/hr) : " + String.valueOf(progress));
                hourRateSeeding=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar9.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Mature_hour.setText("Mature Water Rate (/hr) : " + String.valueOf(progress));
                hourRateMature=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // add btn click listener
        addBtn = findViewById(R.id.add_plant_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, scifiName, uri, description, stage,time;

                name = nameTxt.getText().toString();
                time = timmer.getText().toString();
                int wTime = 0;
                scifiName = scifiNameTxt.getText().toString();
                try {
                    uri = imageUri.toString();
                } catch (Exception e) {
                    Toast.makeText(AddPlantActivity.this, "Please select an image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                description = descriptionTxt.getText().toString();
                stage = stageTxt.getText().toString();

                if (name.length()==0 || scifiName.length()==0 || uri.length()==0 || description.length()==0 || stage.length()==0 )
                {
                    Toast.makeText(AddPlantActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    wTime = Integer.parseInt(time);
                } catch (Exception e) {
                    Toast.makeText(AddPlantActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                    return;
                }

                // validating user input
                if (minSeedMoisture > maxSeedMoisture) {
                    Toast.makeText(AddPlantActivity.this, "Min Seed Moisture can not be greater than Max Seed Moisture", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (minSeedingMoisture > maxSeedingMoisture) {
                    Toast.makeText(AddPlantActivity.this, "Min Seedling Moisture can not be greater than Max Seedling Moisture", Toast.LENGTH_SHORT).show();

                    return;
                }
                else if (minMatureMoisture > maxMatureMoisture) {
                    Toast.makeText(AddPlantActivity.this, "Min Mature-Plant Moisture can not be greater than Max Mature-Plant Moisture", Toast.LENGTH_SHORT).show();

                    return;
                }

                PlantInfo plantInfo = new PlantInfo(name, scifiName, uri, description, stage,hourRateSeed,
                        hourRateSeeding, hourRateMature, minSeedMoisture, maxSeedMoisture, minSeedingMoisture,
                        maxSeedingMoisture, minMatureMoisture, maxMatureMoisture, wTime);

                Intent intent = new Intent(AddPlantActivity.this, MyPlantsActivity.class);
                intent.putExtra("plantInfo", plantInfo);
                startActivity(intent);
            }
        });

        // cancel btn click listener
        cancelBtn = findViewById(R.id.add_plant_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPlantActivity.this, MyPlantsActivity.class);
                startActivity(intent);
            }
        });
    }



    private void showImagePickerDialog() {

        //option for dialog
        String[] options = {"Camera", "Gallery"};

        // Alert dialog builder
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);

        //setTitle
        builder.setTitle("Choose An Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item click
                if (which == 0){ //start from 0 index
                    //camera selected
                    if (!checkCameraPermission()){
                        //request camera permission
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                }else if (which == 1){
                    //Gallery selected
                    /*if (!checkStoragePermission()){
                        //request storage permission
                        requestStoragePermission();
                    } else {*/
                    pickFromGallery();
                    // }

                }
            }
        }).create().show();
    }

    private void pickFromGallery() {
        //intent for taking image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); // only Image

        startActivityForResult(galleryIntent,IMAGE_FROM_GALLERY_CODE);
    }

    private void pickFromCamera() {

//       ContentValues for image info
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION,"IMAGE_DETAIL");

        //save imageUri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to open camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);

        startActivityForResult(cameraIntent,IMAGE_FROM_CAMERA_CODE);
    }

    private void saveData() {
        Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
    }

    //back button click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //check camera permission
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result & result1;
    }

    //request for camera permission
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_PERMISSION_CODE); // handle request permission on override method
    }

    //check storage permission
    private boolean checkStoragePermission(){
        return ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    //request for storage permission
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_PERMISSION_CODE);
    }


    //handle request permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length >0){

                    //if all permission allowed return true , otherwise false
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted){
                        //both permission granted
                        pickFromCamera();
                    }else {
                        //permission not granted
                        Toast.makeText(getApplicationContext(), "Camera & Storage Permission needed...", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAGE_PERMISSION_CODE:
                if (grantResults.length >0){

                    //if all permission allowed return true , otherwise false
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted){
                        //permission granted
                        pickFromGallery();
                    }else {
                        //permission not granted
                        Toast.makeText(getApplicationContext(), "Storage Permission needed...", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_FROM_GALLERY_CODE){
                // picked image from gallery
                //crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(AddPlantActivity.this);

            }else if (requestCode == IMAGE_FROM_CAMERA_CODE){
                //picked image from camera
                //crop Image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(AddPlantActivity.this);
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                //cropped image received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                plantPic.setImageURI(imageUri);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //for error handling
                Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void checkbutton(View v){
        int radioId= radioGroup.getCheckedRadioButtonId();



        if (radioButton1.isChecked()) {
            moistureLayout.setVisibility(View.VISIBLE);
            hourLayout.setVisibility(View.INVISIBLE);
           // Toast.makeText(this,"Seed Max Moisture: "+ maxSeedMoisture, Toast.LENGTH_SHORT).show();
        } else if (radioButton2.isChecked()) {
            moistureLayout.setVisibility(View.INVISIBLE);
            hourLayout.setVisibility(View.VISIBLE);
        }

    }


}