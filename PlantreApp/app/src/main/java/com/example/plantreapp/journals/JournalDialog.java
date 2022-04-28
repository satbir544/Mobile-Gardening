package com.example.plantreapp.journals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.plantreapp.R;

public class JournalDialog extends AppCompatDialogFragment {
    private EditText editTextName;
    private EditText editTextDescription;
    private JournalDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_journal_dialog, null);

        builder.setView(view)
                .setTitle("Journal Details")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editTextName.getText().toString();
                        String description = editTextDescription.getText().toString();

                        if (!name.equals("")) {
                            listener.applyTexts(name, description);
                        } else {
                            listener.applyTexts("My Journal", "");
                        }
                    }
                });

        editTextName = view.findViewById(R.id.edit_name);
        editTextDescription = view.findViewById(R.id.edit_description);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (JournalDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface JournalDialogListener {
        void applyTexts(String name, String description);
    }
}