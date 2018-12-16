package jarno.oussama.com.examenmonitor.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jarno.oussama.com.examenmonitor.Activities.CheckInOutActivity;
import jarno.oussama.com.examenmonitor.FirebaseDB.Exam;
import jarno.oussama.com.examenmonitor.FirebaseDB.Student;
import jarno.oussama.com.examenmonitor.R;

public class ExamsListAdapter extends RecyclerView.Adapter<ExamsListAdapter.ViewHolder> {
    private List<Exam> Exams;
    public ExamsListAdapter(List<Exam> myExams) {
        this.Exams = myExams;
    }


    @NonNull
    @Override
    public ExamsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.examslist_item,parent,false);
        return new ExamsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamsListAdapter.ViewHolder holder, int position) {
        holder.ExamName.setText(Exams.get(position).getName());
        Calendar startTime = Calendar.getInstance();
        startTime.setTimeInMillis(Exams.get(position).getStartTime());
        holder.StartHour.setText(String.format(Locale.getDefault(),"%d : %d", startTime.get(Calendar.HOUR), startTime.get(Calendar.MINUTE)));
        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(Exams.get(position).getEndTime());
        holder.EndHour.setText(String.format(Locale.getDefault(),"%d : %d", endTime.get(Calendar.HOUR), endTime.get(Calendar.MINUTE)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CheckInOutActivity.class).putExtra("EXAM_ID", Exams.get(position).getExamId());
                v.getContext().startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return Exams.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView ExamName, StartHour,EndHour;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ExamName = itemView.findViewById(R.id.textViewExamName);
            StartHour = itemView.findViewById(R.id.textViewStartTime);
            EndHour = itemView.findViewById(R.id.textViewEndTime);
        }
    }
}
