package strategie;

import robot.Robot;

public class DateLiberation implements Comparable<DateLiberation>{
    private Robot bot;
    private Long date;

    public DateLiberation(Robot bot, Long date){
        this.bot = bot;
        this.date = date;
    }

    public Robot getRobot(){
        return bot;
    }

    public Long getDate(){
        return date;
    }

    @Override 
    public String toString(){
        String s = "Libération de " + bot + " à date = " + date;
        return s + "\n";
    }

    @Override
    public int compareTo(DateLiberation d){
        if (date > d.getDate()){
            return 1;
        }
        if (date < d.getDate()){
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object other){
        if (other instanceof DateLiberation){
            DateLiberation autreDate = (DateLiberation) other;
            return date == autreDate.getDate();
        }
        return false;
    }
}
