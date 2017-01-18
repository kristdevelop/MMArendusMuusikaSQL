import javafx.beans.property.SimpleStringProperty;


public class Persoon {
    final SimpleStringProperty nimi;                                //stringi konstruktor. Is the base class for all writable string properties.  Is the abstract base class for observable string properties, SimpleStringProperty is a concrete implementation.
    final SimpleStringProperty skoor;                               // When properties are used, if the value of a property attribute in the datamodel changes, the view of the item in the TableView is automatically updated to match the updated datamodel value.
                                                                    // For example, if the value of a person's email property is set to a new value, that update will be reflected in the TableView because it listens for the property change.
                                                                    // If instead, a plain String had been used to represent the email, the TableView would not refresh as it would be unaware of email value changes.
    public Persoon(String score, String name){
        this.skoor= new SimpleStringProperty(score);
        this.nimi=new SimpleStringProperty(name);
    }
    public String getNimi() {
        return nimi.get();
    }
    public String getSkoor() {
        return skoor.get();
    }
    public void setNimi(String name) {
        nimi.set(name);
    }
    public void setScore(String score) {
        skoor.set(score);
    }
}
