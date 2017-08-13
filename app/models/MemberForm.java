package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberForm
{
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String company;
    private String jobTitle;
    private String dateJoined;
    private List<String> errors = new ArrayList<>();

    private final int COMPANY_MAX_LENGTH = 80;
    private final int JOB_MAX_LENGTH = 80;

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public void setJobTitle(String jobTitle)
    {
        this.jobTitle = jobTitle;
    }

    public void setDateJoined(String dateJoined)
    {
        this.dateJoined = dateJoined;
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
        return phone;
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

    public boolean isValid()
    {
        return nameValid(firstName) && nameValid(lastName)
                && emailValid(email) && phoneValid(phone)
                && dateValid(dateJoined);
    }

    private boolean nameValid(String name)
    {
        boolean valid = true;
        final int NAME_MAX_LENGTH = 30;

        if (name.length() != 0 && email.length() > 0)
        {
            if (name.length() > NAME_MAX_LENGTH)
            {
                errors.add("Names cannot exceed 30 characters");
                valid = false;
            }
        }
        else
        {
            errors.add("Fields marked with * are required");
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
        final int PHONE_MAX_LENGTH = 12;
        final int PHONE_MIN_LENGTH = 10;

        if(phone.length() > 0)
        {
            if (phone.length() < PHONE_MIN_LENGTH || phone.length() > PHONE_MAX_LENGTH)
            {
                valid = false;
                errors.add("Phone number must include area code and be in format XXX-XXX-XXXX or XXXXXXXXXX");
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
        }catch(Exception e)
        {
            errors.add("Date must be formatted as YYYY-MM-DD");
            valid = false;
        }

        return valid;
    }

    public List<String> showErrors()
    {
        return errors;
    }

}
