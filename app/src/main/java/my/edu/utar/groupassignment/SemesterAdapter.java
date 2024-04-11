package my.edu.utar.groupassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SemesterAdapter extends ArrayAdapter<SemesterData> {

    public SemesterAdapter(@NonNull Context context, ArrayList<SemesterData> dataArrayList) {
        super(context, R.layout.semester_list, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        SemesterData semesterData = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.semester_list, parent, false);
        }

        TextView listName = view.findViewById(R.id.listName);
        listName.setText(semesterData.semester+ " " + semesterData.year);
        return view;
    }
}