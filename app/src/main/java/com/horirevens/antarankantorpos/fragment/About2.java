package com.horirevens.antarankantorpos.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.horirevens.antarankantorpos.R;

/**
 * Created by horirevens on 1/23/17.
 */
public class About2 extends Fragment {
    private ImageView ivEmail, ivPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_about2, container, false);
        ivEmail = (ImageView) rootView.findViewById(R.id.iv_email2);
        ivPhone = (ImageView) rootView.findViewById(R.id.iv_phone2);

        ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                //intent.setType("plain/text");
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ "isa@posindonesia.co.id" });
                //intent.putExtra(Intent.EXTRA_CC, new String[] { "" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri phoneNumber = Uri.parse("tel:085331731155");
                Intent intent = new Intent(Intent.ACTION_DIAL, phoneNumber);
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        return rootView;
        //return inflater.inflate(R.layout.content_about2, container, false);
    }
}
