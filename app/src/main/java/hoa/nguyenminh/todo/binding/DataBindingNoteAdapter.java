package hoa.nguyenminh.todo.binding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import hoa.nguyenminh.todo.BR;
import hoa.nguyenminh.todo.Note;
import hoa.nguyenminh.todo.R;

/**
 * Created by Hoa Nguyen on Apr 09 2019.
 */
public class DataBindingNoteAdapter extends ListAdapter<Note, DataBindingNoteAdapter.NoteViewHolder> {
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

    public DataBindingNoteAdapter() {
        super(DIFF_CALLBACK);
    }

    @BindingAdapter("isOdd")
    public static void isOdd(CardView view, boolean isOdd) {
        view.setCardBackgroundColor(view.getResources().getColor(isOdd ? R.color.colorBlue50 : R.color.colorBlue100, null));
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.binding_note_item, parent, false);
        return new NoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getItem(position);
        holder.bind(note);
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
        private final ViewDataBinding binding;
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                if (mOnItemClickListener != null && pos != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(getItem(pos));
                }
            }
        };

        public NoteViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Note item) {
            binding.setVariable(BR.noteData, item);
            binding.setVariable(BR.itemClickListener, mOnClickListener);
            binding.setVariable(BR.pos, getLayoutPosition());
            binding.executePendingBindings();
        }
    }
}
