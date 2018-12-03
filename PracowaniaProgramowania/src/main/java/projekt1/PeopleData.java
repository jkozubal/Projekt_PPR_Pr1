package projekt1;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PeopleData
{
    // Used for sorting in ascending order of
    // roll name
    private String fullname;
    private String city;
    private String pesel;
    public PeopleData(String pesel, String fullname, String city) {
        this.city = city;
        this.fullname = fullname;
        this.pesel = pesel;
    }
    public String getPESEL() {return this.pesel; }
    public String getFullname() {
        return this.fullname;
    }
    public String getCity() {
        return this.city;
    }
}

class Sortbycity implements Comparator<PeopleData>
{
    public int compare(PeopleData a, PeopleData b)
    {
        return a.getCity().compareTo(b.getCity());
    }
}
