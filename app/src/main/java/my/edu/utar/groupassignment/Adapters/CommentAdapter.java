package my.edu.utar.groupassignment.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import my.edu.utar.groupassignment.Model.Comment;
import my.edu.utar.groupassignment.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {


    private List<Comment> commentList;

//    public CommentAdapter(List<Comment> comments) {
//        this.commentList = commentList;
//    }


    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }

    public CommentAdapter(List<Comment> comments) {
        if (comments == null) {
            Log.e("CommentAdapter", "Received a null list");
        }
        this.commentList = comments != null ? comments : new ArrayList<>();
    }


    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.commentText.setText(comment.getContent());
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView commentText;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentListView);
        }
    }
}
