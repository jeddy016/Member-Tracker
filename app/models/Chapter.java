package models;

import javax.persistence.*;

@Entity
@Table(name = "chapter")
public class Chapter
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="chapter_id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="leader_id")
    private Integer leaderID;

    public int getID()
    {
        return id;
    }

    public void setID(int chapterID)
    {
        this.id = chapterID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getLeaderID()
    {
        return leaderID;
    }

    public void setLeaderID(Integer leaderID)
    {
        this.leaderID = leaderID;
    }
}
