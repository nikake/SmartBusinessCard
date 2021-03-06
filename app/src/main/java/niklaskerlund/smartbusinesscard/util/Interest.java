package niklaskerlund.smartbusinesscard.util;

/**
 * Created by Niklas on 2015-12-21.
 */
public enum Interest {

    ANDROID("AND", "Android"),
    CPLUSPLUS("C++", "C++"),
    CSHARP("C#", "C#"),
    DEVELOPMENT("DEV", "Development"),
    EDUCATION("EDU", "Education"),
    INTERNET_OF_THINGS("IOT", "Internet of Things"),
    IT("IT", "IT"),
    JAVA("JAVA", "Java"),
    PROGRAMMING("PROG", "Programming"),
    RECRIUTMENT("RECR", "Recruitment"),
    SOCIAL_NETWORKING("SN", "Social networking");

    private final String tagName;
    private final String index;

    Interest(String index, String tagName) {
        this.index = index;
        this.tagName = tagName;
    }

    public String getTagName(){
        return tagName;
    }

    public String toString(){
        return index;
    }


}
