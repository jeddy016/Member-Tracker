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

}
