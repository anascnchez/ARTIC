package es.upm.etsiinf.artic;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class FullImageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_full_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        ImageView imageView = view.findViewById(R.id.full_image);

        Bundle args = getArguments();
        if (args != null) {
            String imageUrl = args.getString("image_url");

            if (imageUrl != null) {
                imageView.setImageURI(Uri.parse(imageUrl));
            }
        }
    }
}
