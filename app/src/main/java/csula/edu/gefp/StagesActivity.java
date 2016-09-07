package csula.edu.gefp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import csula.edu.gefp.model.FlightPlan;
import csula.edu.gefp.model.FlightPlanData;
import csula.edu.gefp.model.Stage;
import csula.edu.gefp.model.UserData;


public class StagesActivity extends AppCompatActivity {

    private StagesAdapter stagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stages);

        RecyclerView stagesRecyclerView = (RecyclerView) findViewById(R.id.stagesRecyclerView);
        stagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stagesAdapter = new StagesAdapter();
        stagesRecyclerView.setAdapter(stagesAdapter);

        (new FlightPlanTask()).execute(UserData.getInstance(this).getUser().getId().toString());
    }

    private class StageHolder extends RecyclerView.ViewHolder {
        public TextView stageNameText;

        public StageHolder(View itemView) {
            super(itemView);
            stageNameText = (TextView) itemView.findViewById(R.id.stageNameText);
        }
    }

    private class StagesAdapter extends RecyclerView.Adapter<StageHolder> {
        private List<Stage> stages;

        public StagesAdapter() {
            stages = new ArrayList<>();
        }

        public List<Stage> getStages() {
            return stages;
        }

        public void setStages(List<Stage> stages) {
            this.stages = stages;
        }

        @Override
        public StageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater()
                    .inflate(R.layout.list_item_stage, parent, false);
            return new StageHolder(view);
        }

        @Override
        public void onBindViewHolder(StageHolder holder, int position) {
            Stage stage = stages.get(position);
            holder.stageNameText.setText(stage.getName());
        }

        @Override
        public int getItemCount() {
            return stages.size();
        }
    }

    private class FlightPlanTask extends AsyncTask<String, Void, FlightPlan> {

        @Override
        protected FlightPlan doInBackground(String... params) {
            return WebServiceClient.getFlightPlan(params[0]);
        }

        @Override
        protected void onPostExecute(FlightPlan flightPlan) {
            if (flightPlan != null) {
                FlightPlanData.getInstance().setFlightPlan(flightPlan);
                stagesAdapter.setStages(flightPlan.getStages());
                stagesAdapter.notifyDataSetChanged();
            }
        }
    }

}
