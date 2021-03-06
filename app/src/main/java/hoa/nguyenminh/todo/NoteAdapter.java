package hoa.nguyenminh.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Hoa Nguyen on Apr 09 2019.
 */
public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {
    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.equals(newItem);
        }
    };
    private OnItemClickListener mOnItemClickListener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getItem(position);

        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
        holder.priority.setText(String.valueOf(note.getPriority()));
    }

    public Note getNoteAt(int pos) {
        return getItem(pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private TextView priority;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            priority = itemView.findViewById(R.id.priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (mOnItemClickListener != null && pos != RecyclerView.NO_POSITION) {
                        mOnItemClickListener.onItemClick(getItem(pos));
                    }
                }
            });
        }
    }
}
