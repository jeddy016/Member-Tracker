package models;

import javax.persistence.*;

@Entity
public class ChapterDetail
{
    @Id
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private String meetingPlace;

    @Column
    private String streetAddress;

    @Column
    private String city;

    @Column
    private String leader;

    @Column
    private String leaderEmail;

    public int getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getMeetingPlace()
    {
        return meetingPlace;
    }

    public String getStreetAddress()
    {
        return streetAddress;
    }

    public String getCity()
    {
        return city;
    }

    public String getLeader()
    {
        return leader;
    }

    public String getLeaderEmail()
    {
        return leaderEmail;
    }
}
