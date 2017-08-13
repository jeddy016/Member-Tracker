package models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name= "member")
public class Member
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="member_id")
    private int id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="date_joined")
    private LocalDate dateJoined;

    @Column(name="volunteer")
    private int volunteer;

    @Column(name="chapter_id")
    private int chapterID;

    @Column(name="job_title_id")
    private int jobTitleID;

    @Column(name="company_id")
    private int companyID;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public LocalDate getDateJoined()
    {
        return dateJoined;
    }

    public void setDateJoined(LocalDate dateJoined)
    {
        this.dateJoined = dateJoined;
    }

    public int isVolunteer()
    {
        return volunteer;
    }

    public void setVolunteer(int volunteer)
    {
        this.volunteer = volunteer;
    }

    public int getChapterID()
    {
        return chapterID;
    }

    public void setChapterID(int chapterID)
    {
        this.chapterID = chapterID;
    }

    public int getJobTitleID()
    {
        return jobTitleID;
    }

    public void setJobTitleID(int jobTitleID)
    {
        this.jobTitleID = jobTitleID;
    }

    public int getCompanyID()
    {
        return companyID;
    }

    public void setCompanyID(int companyID)
    {
        this.companyID = companyID;
    }
}
