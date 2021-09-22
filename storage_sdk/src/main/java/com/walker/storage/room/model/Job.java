package com.walker.storage.room.model;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class Job {

    public String company;
    public double time;

    public Address address;

    public Job(String company, double time) {
        this.company = company;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Job{" +
                "company='" + company + '\'' +
                ", time=" + time +
                ", address=" + address +
                '}';
    }
}
