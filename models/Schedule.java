package models;
public class Schedule {
    private int startTime; 
    private int endTime; // Assuming time is represented in 24-hour format as an integer (e.g., 900 for 9:00 AM, 1700 for 5:00 PM)

    public Schedule(int startTime,  int endTime) throws Exception {
        if (startTime < 0 || startTime > 2359 || endTime < 0 || endTime > 2359) {
            throw new Exception("Error: Time must be between 0000 and 2359.");
        }
        if ( endTime <= startTime) {
            throw new Exception("Error: End time cannot be before start time.");
        }
        else{
        this.startTime = startTime;
        this.endTime = endTime;
        }
    }
   

    public String getWorkinghours()  {
        
        return startTime + " to " + endTime;
    }

   

    @Override
    public String toString() {
        return startTime + " to " + endTime;
    }
}