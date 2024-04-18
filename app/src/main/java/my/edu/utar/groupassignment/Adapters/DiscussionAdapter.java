package my.edu.utar.groupassignment.Adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.vanniktech.emoji.EmojiTextView;

import java.util.List;

import my.edu.utar.groupassignment.PaperDiscussion;
import my.edu.utar.groupassignment.R;

//public class DiscussionAdapter extends ArrayAdapter<PaperDiscussion> {
//    private Context context;
//    private int resource;

//    public DiscussionAdapter(Context context, int resource, List<PaperDiscussion> discussions) {
//        super(context, resource, discussions);
//        this.context = context;
//        this.resource = resource;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
//        }
//
////        TextView userTextView = convertView.findViewById(R.id.userTextView);
////        TextView messageTextView = convertView.findViewById(R.id.messageTextView);
//
//        EmojiTextView user = convertView.findViewById(R.id.userTextView);
//        EmojiTextView message = convertView.findViewById(R.id.messageTextView);
//        PaperDiscussion discussion = getItem(position);
//
//        if (discussion != null) {
////            userTextView.setText(discussion.getUser());
////            messageTextView.setText(discussion.getDiscussion());
//            user.setText(discussion.getDiscussion());
//            message.setText(discussion.getDiscussion());
//        }
//
//        return convertView;
//    }

//
//    }
//
//}
//public class DiscussionAdapter extends ArrayAdapter<PaperDiscussion> {
//    public DiscussionAdapter(Context context, int resource, List<PaperDiscussion> discussions) {
//        super(context, resource, discussions);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_past_year_discussion, parent, false);
//        }
//
//        PaperDiscussion item = getItem(position);
//        if (item != null) {
//            // Example of setting text
//            TextView textView = convertView.findViewById(R.id.messageTextView);
//            textView.setText(item.getDiscussion());  // Assuming 'getMessage()' method exists
//        }
//        return convertView;
//    }
//}

public class DiscussionAdapter extends ArrayAdapter<PaperDiscussion> {
    private int resource;

    public DiscussionAdapter(Context context, int resource, List<PaperDiscussion> discussions) {
        super(context, resource, discussions);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LinearLayout layout = new LinearLayout(getContext());

            TextView userView = new TextView(getContext());
            userView.setId(R.id.userTextView); // Ensure ID does not conflict with other IDs
            userView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            layout.addView(userView);

            // TextView for Message
            TextView messageView = new TextView(getContext());
            messageView.setId(R.id.messageTextView); // Ensure ID does not conflict with other IDs
            messageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            layout.addView(messageView);

            holder = new ViewHolder();
            holder.userTextView = userView;
            holder.messageTextView = messageView;
            convertView = layout;
            convertView.setTag(holder);


//            convertView = LayoutInflater.from(getContext()).inflate(this.resource, parent, false);
//            holder = new ViewHolder();
//            holder.messageTextView = convertView.findViewById(R.id.messageTextView);
//            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PaperDiscussion item = getItem(position);
        if (item != null) {
            //holder.userTextView.setText(item.getUser());
            holder.messageTextView.setText(item.getDiscussion());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView messageTextView;
        TextView userTextView;
    }
}
