package com.ale.sisenoroscuro;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

public class CardDialog extends Dialog {

    public CardDialog(@NonNull Context context, String imageName) {
        super(context, R.style.CardDialog);

        setCancelable(true);

        setContentView(R.layout.card_detail);

        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView imageView = findViewById(R.id.iv_card_detail);
        try {
            imageView.setImageDrawable(getDrawableFromAssets(imageName));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /*@NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(true)
                .create();

        ImageView imageView = view.findViewById(R.id.iv_card_detail);


        return dialog;
    }*/


    private Drawable getDrawableFromAssets(String name) throws IOException {
        InputStream ims = getContext().getAssets().open(name + ".png");
        Drawable d = Drawable.createFromStream(ims, null);
        ims.close();
        return d;
    }
}
