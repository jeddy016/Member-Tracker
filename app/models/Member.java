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

    @Column(name="active")
    private int active;

    public Member(){}

    public Member(MemberFormHelper helper)
    {
        this.volunteer = helper.getVolunteer();
        this.companyID = helper.getCompanyID();
        this.jobTitleID = helper.getJobTitleID();
        this.firstName = helper.getFirstName();
        this.lastName = helper.getLastName();
        this.email = helper.getEmail();
        this.phone = helper.getPhone();
        this.dateJoined = helper.getDateJoined();
        this.chapterID = helper.getChapter();
        this.active = 1;
    }
}
