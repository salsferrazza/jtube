jtube
=====

Transparently stream POJOs to Google BigQuery.
```
class Person {
  
  public String firstName;
  public String lastName;
  
  public Person(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
  
}
```

```
Object o = new Person("firstName", "lastName");
jtube.JTube.stream(o);
```

JTube finds your BigQuery table named Person and using BQ's streaming interface will insert a record into the firstName and lastName columns with the value of the corresponding POJO member properties.

**Roadmap**

- Add support for schema mapping as an auxiliary parameter to stream()
