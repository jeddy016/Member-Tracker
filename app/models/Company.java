package models;

import javax.persistence.*;

@Entity
@Table(name= "company")
public class Company
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="company_id")
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
