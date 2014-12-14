package ncucsie.cas;

import org.json.JSONArray;

public class myAppointmentClass {

    private String date;
    private String classroom;
    private JSONArray array;
    private int num;
    private String time;
    private String name;
    private String teacher;
    private String hiddenNum;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getHiddenNum() {
        return hiddenNum;
    }

    public void setHiddenNum(String hiddenNum) {
        this.hiddenNum = hiddenNum;
    }

    public JSONArray getData() {
        return array;
    }

    public void setData(JSONArray array) {
        this.array = array;
    }


    @Override
    public String toString() {
        return array.toString();
    }
}