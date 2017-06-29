package com.shiva.moneybank;

import android.content.res.Resources;
import android.widget.EditText;

import java.util.InputMismatchException;

/**
 * Created by Visweswaran on 24-06-2017.
 */

public class Handler
{
    String enclose(String string)
    {
        return "\""+string+"\"";
    }

    // This is used to check the edit text Double only
    boolean checkEditText(EditText editText)
    {
        try
        {
            if(editText.getText().toString().equals(""))
            {
                editText.setError(Resources.getSystem().getString(R.string.nonEmpty));
                return false;
            }
        }
        catch (InputMismatchException e)
        {
            return false;
        }
        return true;
    }
}
