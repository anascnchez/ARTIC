package es.upm.etsiinf.artic;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import androidx.appcompat.app.AlertDialog;

import es.upm.etsiinf.artic.db.MySQLiteHelper;

public class Add extends Fragment
{
    private ImageView imagePlaceholder;
    private EditText editTitle;
    private Button btnSave;
    private NotificationHandler handler;
    private Uri selectedImageUri;

    private ActivityResultLauncher<Uri> cameraLauncher;
    private Uri cameraImageUri;
    private MySQLiteHelper dbHelper;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        handler = new NotificationHandler( requireContext() );
        // Registro para pedir permisos de cámara
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        launchCamera();
                    } else {
                        Toast.makeText(getContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            try {
                                requireContext().getContentResolver()
                                        .takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }
                            selectedImageUri = uri;
                            imagePlaceholder.setImageURI(uri);
                            imagePlaceholder.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success) {
                        selectedImageUri = cameraImageUri;
                        imagePlaceholder.setImageURI(cameraImageUri);
                        imagePlaceholder.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        imagePlaceholder = view.findViewById(R.id.imagePlaceholder);
        editTitle = view.findViewById(R.id.editTitle);
        btnSave = view.findViewById(R.id.btnSave);
        dbHelper = new MySQLiteHelper(getContext());

        imagePlaceholder.setOnClickListener(v -> showImageMenu());
        btnSave.setOnClickListener(v -> saveArt());
        return view;
    }

    private void showImageMenu() {
        new AlertDialog.Builder(getContext())
                .setTitle("Select an image")
                .setItems(new String[]{"Camera", "Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        checkCameraPermission();
                    } else {
                        openImagePicker();
                    }
                })
                .show();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        cameraImageUri = createImageUri();
        if (cameraImageUri != null) {
            cameraLauncher.launch(cameraImageUri);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private Uri createImageUri() {
        File image = new File(requireContext().getCacheDir(), "photo_" + System.currentTimeMillis() + ".jpg");
        return FileProvider.getUriForFile(requireContext(), "es.upm.etsiinf.artic.fileprovider", image);
    }

    private void saveArt() {
        String title = editTitle.getText().toString().trim();
        if(selectedImageUri == null){
            Toast.makeText(getContext(),"Select an image first", Toast.LENGTH_SHORT).show();
            return;
        }
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Give your work a title", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", title);
        values.put("imagen", selectedImageUri.toString());
        db.insert("cuadros", null, values);
        db.close();

        Notification.Builder nBuilder = handler.createNotification("Collection updated", "Your collection has been updated successfully! Check it out!", true);
        handler.getManager().notify(1,nBuilder.build());
        Toast.makeText(getContext(), "Collection updated!", Toast.LENGTH_SHORT).show();


        // Reset formulario
        editTitle.setText("");
        imagePlaceholder.setImageResource(R.drawable.outline_wall_art_24);
        imagePlaceholder.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        selectedImageUri = null;
    }
}