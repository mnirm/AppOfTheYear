package jarno.oussama.com.examenmonitor.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import jarno.oussama.com.examenmonitor.FirebaseDB.Student;
import jarno.oussama.com.examenmonitor.R;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {
    private List<Student> students;

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
        Picasso.get().load(students.get(position).getProfilePictureUrl()).into(holder.ProfilePic);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }


     class ViewHolder extends RecyclerView.ViewHolder {
         TextView FirstName,LastName,studentNumber,studentCardId;
         ImageView ProfilePic;
         ViewHolder(View itemView) {
            super(itemView);
            FirstName = itemView.findViewById(R.id.textViewStudentName);
            LastName = itemView.findViewById(R.id.textViewStudentLastName);
            studentNumber = itemView.findViewById(R.id.textViewStudentNumber);
            ProfilePic = itemView.findViewById(R.id.profile_image);
        }
    }
}
