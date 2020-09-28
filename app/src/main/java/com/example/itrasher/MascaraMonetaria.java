package com.example.itrasher;

import java.text.NumberFormat;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MascaraMonetaria implements TextWatcher{

        final EditText campo;

        public MascaraMonetaria(EditText campo) {
            super();
            this.campo = campo;
        }

        private boolean isUpdating = false;
        private NumberFormat nf = NumberFormat.getCurrencyInstance();

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int after) {
            if (isUpdating) {
                isUpdating = false;
                return;
            }

            isUpdating = true;
            String mystr = s.toString();
            mystr = mystr.replaceAll("[^\\d]", "");

            try {
                mystr = nf.format(Double.parseDouble(mystr) / 100);
                campo.setText(mystr);
                campo.setSelection(campo.getText().length());
            } catch (NumberFormatException e) {
                s = "";
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


