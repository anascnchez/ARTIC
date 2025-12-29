package es.upm.etsiinf.artic;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
    private static final int NOTIFICATION_PERMISSION_CODE = 101;
    NotificationHandler handler;
    private ImageView imagePlaceholder;
    private EditText editTitle;
    private Button btnSave;

    private Uri selectedImageUri;

    private ActivityResultLauncher<Uri> cameraLauncher;
    private Uri cameraImageUri;
    private MySQLiteHelper dbHelper;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                        Uri uri = result.getData().getData();

                        if (uri != null) {
                            requireContext().getContentResolver()
                                    .takePersistableUriPermission(
                                            uri,
                                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    );

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
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

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
                        cameraImageUri = createImageUri();
                        cameraLauncher.launch(cameraImageUri);
                    } else {
                        openImagePicker();
                    }
                })
                .show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        imagePickerLauncher.launch(
                Intent.createChooser(intent, "Select an image")
        );
    }

    private Uri createImageUri() {
        File image = new File(
                requireContext().getCacheDir(),
                "photo_" + System.currentTimeMillis() + ".jpg"
        );

        return FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".fileprovider",
                image
        );
    }

    private void saveArt() {
        String title = editTitle.getText().toString().trim();

        if(selectedImageUri ==null){
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

        Toast.makeText(getContext(), "Collection updated!", Toast.LENGTH_SHORT).show();

        // Notificación al subir el cuadro
        Notification.Builder nBuilder = handler.createNotification("Cuadro subido correctamente", "Tu cuadro se ha guardado en el apartado Collection", true);
        handler.getManager().notify(1,nBuilder.build());

        // Reset formulario
        editTitle.setText("");
        imagePlaceholder.setImageResource(R.drawable.outline_add_photo_alternate_24);
        imagePlaceholder.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        selectedImageUri = null;
    }
}