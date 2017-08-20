package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MemberDetail
{
    @Id
    @Column
    private int id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String dateJoined;

    @Column
    private String volunteer;

    @Column
    private String chapter;

    @Column
    private String jobTitle;

    @Column
    private String company;

    @Column
    private int active;

    public int getID()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getFullName()
    {
        return firstName + " " + lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhone()
    {
        StringBuilder formattedPhone = new StringBuilder();

        if(phone.length() > 0)
        {
            String[] phoneArr = phone.split("");

            for (int i = 0; i < phoneArr.length; i++)
            {
                if (i == 0) formattedPhone.append("(");

                if (i == 3) formattedPhone.append(") ");

                if (i == 6) formattedPhone.append("-");

                formattedPhone.append(phoneArr[i]);
            }
        }
        else
        {
            formattedPhone.append("N/A");
        }
        return formattedPhone.toString();
    }

    public String getDateJoined()
    {
        return dateJoined;
    }

    public String getVolunteer()
    {
        return volunteer.equals("0") ? "No" : "Yes";
    }

    public String getChapter()
    {
        return chapter;
    }

    public String getJobTitle()
    {
        return jobTitle;
    }

    public String getCompany()
    {
        return company;
    }

}
