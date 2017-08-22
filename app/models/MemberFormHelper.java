package models;

import play.data.DynamicForm;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberFormHelper
{
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String company;
    private String jobTitle;
    private String dateJoined;
    private int chapter;
    private int jobTitleID;
    private int companyID;
    private int volunteer;
    private List<String> errors = new ArrayList<>();

    public void setJobTitleID(int jobTitleID)
    {
        this.jobTitleID = jobTitleID;
    }

    public void setCompanyID(int companyID)
    {
        this.companyID = companyID;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhone()
    {
        return phone.replaceAll( "[^\\d]", "" );
    }

    public String getCompany()
    {
        return company;
    }

    public String getJobTitle()
    {
        return jobTitle;
    }

    public LocalDate getDateJoined()
    {
        return LocalDate.parse(dateJoined);
    }

    public int getChapter()
    {
        return chapter;
    }

    public int getJobTitleID()
    {
        return jobTitleID;
    }

    public int getCompanyID()
    {
        return companyID;
    }

    public int getVolunteer()
    {
        return volunteer;
    }

    public boolean isValid()
    {
        boolean valid = true;

        if(!formComplete()) valid = false;
        if(!nameValid(firstName)) valid = false;
        if(!nameValid(lastName)) valid = false;
        if(!emailValid(email)) valid = false;
        if(!phoneValid(phone)) valid = false;
        if(!dateValid(dateJoined)) valid = false;
        if(!companyValid(company)) valid = false;
        if(!jobTitleValid(jobTitle)) valid = false;

        return valid;
    }

    private boolean formComplete()
    {
        boolean valid = true;

        if(firstName.length() == 0 || lastName.length() == 0 || email.length() == 0 || dateJoined.length() == 0)
        {
            errors.add("Fields marked with * are required");
            valid = false;
        }
        return valid;
    }

    private boolean nameValid(String name)
    {
        boolean valid = true;
        final int NAME_MAX_LENGTH = 30;

        if (name.length() > NAME_MAX_LENGTH)
        {
            this.errors.add("Names cannot exceed 30 characters");
            valid = false;
        }

        return valid;
    }

    private boolean emailValid(String email)
    {
        boolean valid = true;
        final int EMAIL_MAX_LENGTH = 80;
        final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = EMAIL_REGEX .matcher(email);

        if(matcher.find())
        {
            if(email.length() > EMAIL_MAX_LENGTH)
            {
                errors.add("Email address cannot exceed 80 characters");
                valid = false;
            }
        }
        else
        {
            errors.add("Email format incorrect");
            valid = false;
        }
        return valid;
    }

    private boolean phoneValid(String phone)
    {
        boolean valid = true;
        final int PHONE_MAX_LENGTH = 10;

        phone = phone.replaceAll( "[^\\d]", "" );

        if(phone.length() > 0)
        {
            if (phone.length() != 10)
            {
                valid = false;
                errors.add("Phone number must contain 10 numbers, to include area code");
            }
        }
        return valid;
    }

    private boolean dateValid(String date)
    {
        boolean valid = true;

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            format.parse(date);
        } catch (Exception e)
        {
            errors.add("Date must be formatted as YYYY-MM-DD");
            valid = false;
        }

        return valid;
    }

    private boolean companyValid(String company)
    {
        final int COMPANY_MAX_LENGTH = 80;
        boolean valid = true;

        if(company.length() > COMPANY_MAX_LENGTH)
        {
            errors.add("Company and Job Title cannot exceed 80 characters");
            valid = false;
        }

        return valid;
    }

    private boolean jobTitleValid(String job)
    {
        final int JOB_MAX_LENGTH = 80;
        boolean valid = true;

        if(job.length() > JOB_MAX_LENGTH)
        {
            errors.add("Company and Job Title cannot exceed 80 characters");
            valid = false;
        }

        return valid;
    }

    public List<String> showErrors()
    {
        return errors;
    }

    public void fillForm(DynamicForm form)
    {
        this.chapter = Integer.parseInt(form.get("chapter"));
        this.volunteer = Integer.parseInt(form.get("volunteer"));
        this.firstName = form.get("firstName");
        this.lastName = form.get("lastName");
        this.email = form.get("email");
        this.phone = form.get("phone");
        this.company = form.get("company");
        this.jobTitle = form.get("job");
        this.dateJoined = form.get("joinDate");
    }
}
