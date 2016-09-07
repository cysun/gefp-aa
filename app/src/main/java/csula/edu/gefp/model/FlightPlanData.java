package csula.edu.gefp.model;

import java.util.List;

public class FlightPlanData {

    private static FlightPlanData flightPlanData;

    private FlightPlan flightPlan;

    private FlightPlanData() {
    }

    public static FlightPlanData getInstance() {
        if (flightPlanData == null) flightPlanData = new FlightPlanData();
        return flightPlanData;
    }

    public FlightPlan getFlightPlan() {
        return flightPlan;
    }

    public void setFlightPlan(FlightPlan flightPlan) {
        this.flightPlan = flightPlan;
    }

    public List<Stage> getStages() {
        return flightPlan.getStages();
    }

    public List<Runway> getRunways() {
        return flightPlan.getRunways();
    }

}
