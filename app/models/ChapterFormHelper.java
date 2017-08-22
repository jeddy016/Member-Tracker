package models;

import play.data.DynamicForm;

import java.util.ArrayList;
import java.util.List;

public class ChapterFormHelper
{
    private String name;
    private String meetingPlace;
    private String streetAddress;
    private String city;
    private String leaderID;
    private List<String> errors = new ArrayList<>();

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

    public String getLeaderID()
    {
        return leaderID;
    }

    public void fillForm(DynamicForm form)
    {
        this.name = form.get("name");
        this.meetingPlace = form.get("meetingPlace");
        this.streetAddress = form.get("streetAddress");
        this.city = form.get("city");
        this.leaderID = form.get("leaderID");
    }

    public boolean isValid()
    {
        boolean valid = true;
        final int MAXLENGTH_MEETING_PLACE = 50;
        final int MAXLENGTH_ADDRESS = 80;
        final int MAXLENGTH_CITY = 50;

        if(!nameValid()) valid = false;

        if(!lengthValid(meetingPlace, MAXLENGTH_MEETING_PLACE))
        {
            errors.add("Meeting place must be fewer than 50 characters");
            valid = false;
        }
        if(!lengthValid(streetAddress, MAXLENGTH_ADDRESS))
        {
            errors.add("Address must be fewer than 80 characters");
            valid = false;
        }
        if(!lengthValid(city, MAXLENGTH_CITY))
        {
            errors.add("City must be fewer than 50 characters");
            valid = false;
        }

        return valid;
    }

    private boolean nameValid()
    {
        boolean valid = true;
        final int MAXLENGTH_NAME = 30;

        if(name.length() > 0)
        {
            if(name.length() > MAXLENGTH_NAME)
            {
                errors.add("Chapter name must be fewer than 30 characters");
                valid = false;
            }
        }
        else
        {
            errors.add("Chapter name is required");
            valid = false;
        }
        return valid;
    }

    private boolean lengthValid(String input, int maxLength)
    {
        return input.length() <= maxLength;
    }

    public List<String> showErrors()
    {
        return errors;
    }
}
