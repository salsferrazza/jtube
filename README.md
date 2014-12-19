jtube
=====

Stream Java POJOs into Google BigQuery.

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
try {
  JTube jtube = new JTube("googleserviceaccount", "/tmp/mycred.p12");
  Object o = new Person("firstName", "lastName");
  jtube.stream(o);
} catch (Exception ex) {
  throw(ex);
}
```

JTube finds your BigQuery table named PERSON and using BQ's streaming interface will insert a record into the firstName and lastName columns with the value of the corresponding POJO member properties.

**Roadmap**

- Add support for schema mapping as an auxiliary parameter to stream()
