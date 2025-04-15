public class PeopleCount {
    private int peopleID;
    private int memberCount;
    private String firstName;
    private String lastName;
    private int numOfChildren;
    private int numOfAdults;
    private int numOfSeniors;

    // constructor
    public PeopleCount(String firstName, String lastName, int numOfChildren, int numOfAdults, int numOfSeniors) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.numOfChildren = numOfChildren;
        this.numOfAdults = numOfAdults;
        this.numOfSeniors = numOfSeniors;
        this.memberCount = numOfChildren + numOfAdults + numOfSeniors;
    }

    // getters
    public int getMemberCount() { return memberCount; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getNumOfChildren() { return numOfChildren; }
    public int getNumOfAdults() { return numOfAdults; }
    public int getNumOfSeniors() { return numOfSeniors; }
}

