package my.edu.utar.groupassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IntakeAdapter extends ArrayAdapter<IntakeData> {


    public IntakeAdapter(@NonNull Context context, ArrayList<IntakeData> intakeArrayList) {
        super(context, R.layout.semester_list,intakeArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        IntakeData intakeData = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.semester_list, parent, false);
        }

        TextView listName = view.findViewById(R.id.listName);

        listName.setText(intakeData.month);
        return view;
    }
}
