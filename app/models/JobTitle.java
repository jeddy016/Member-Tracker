package models;

import javax.persistence.*;

@Entity
@Table(name= "job_title")
public class JobTitle
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="job_title_id")
    private int id;

    @Column(name="name")
    private String name;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
