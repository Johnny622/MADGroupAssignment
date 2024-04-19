package my.edu.utar.groupassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.Filter;
import android.widget.Filterable;

public class ListAdapter extends ArrayAdapter<ListData> implements Filterable{

    private ArrayList<ListData> originalData;
    private ArrayList<ListData> filteredData;
    private Filter filter;

    public ListAdapter(@NonNull Context context, ArrayList<ListData> dataArrayList) {
        super(context, R.layout.subject_list, dataArrayList);

        this.originalData = dataArrayList;
        this.filteredData = dataArrayList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ListData listData = getItem(position);
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.subject_list, parent, false);
        }

        TextView listName = view.findViewById(R.id.listName);

        listName.setText(listData.name);
        return view;
    }

    @NonNull
    public Filter getFilter() {
        if (filter == null) {
            filter = new ListFilter();
        }
        return filter;
    }

    private class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<ListData> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalData);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ListData item : originalData) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<ListData>) results.values;
            notifyDataSetChanged();
        }
    }
}