package jarno.oussama.com.examenmonitor;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jarno.oussama.com.examenmonitor.FirebaseDB.Student;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {
    List<Student> students;

    public StudentListAdapter(List<Student> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public StudentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.studentlist_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAdapter.ViewHolder holder, int position) {
        holder.FirstName.setText(students.get(position).getFirstName());
        holder.LastName.setText(students.get(position).getLastName());
        holder.studentNumber.setText(Integer.toString(students.get(position).getStudentNumber()));
        holder.studentCardId.setText(students.get(position).getCardIdNumber());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView FirstName;
        public TextView LastName;
        public TextView studentNumber;
        public TextView studentCardId;

        public ViewHolder(View itemView) {
            super(itemView);
            FirstName = itemView.findViewById(R.id.textViewStudentName);
            LastName = itemView.findViewById(R.id.textViewStudentLastName);
            studentNumber = itemView.findViewById(R.id.textViewStudentNumber);
            studentCardId = itemView.findViewById(R.id.textViewStudentCardId);
        }

    }
}
