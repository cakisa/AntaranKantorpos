package com.horirevens.antarankantorpos.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.horirevens.antarankantorpos.R;

/**
 * Created by horirevens on 12/21/16.
 */
public class Test extends Fragment {
    private View rootView;
    private EditText edtTest;
    private Button btnTest;

    AwesomeValidation awesomeValidation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.test, container, false);
        edtTest = (EditText) rootView.findViewById(R.id.editText);
        btnTest = (Button) rootView.findViewById(R.id.button);

        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        awesomeValidation.setContext(getContext());
        awesomeValidation.addValidation(edtTest, "[a-zA-Z\\s]+", "Error");
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awesomeValidation.validate();
            }
        });


        return rootView;
    }
}
