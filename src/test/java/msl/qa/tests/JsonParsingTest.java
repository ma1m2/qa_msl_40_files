package msl.qa.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import msl.qa.model.Name;
import msl.qa.model.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class JsonParsingTest {
  private final ClassLoader cl = getClass().getClassLoader();
  private final ObjectMapper om = new ObjectMapper();

  @Test
  void jsonParsingTest() throws IOException {
    try(InputStream is = cl.getResourceAsStream("json/users.json")) {
      Users users = om.readValue(is, Users.class);

      int id = users.users().getFirst().id();
      System.out.println("id first user: " + id);
      Assertions.assertEquals(1, id);

      String username = users.users().get(1).username();
      System.out.println("username second user: " + username);
      Assertions.assertEquals("janedoe", username);

      String email = users.users().get(2).email();
      System.out.println("email third user: " + email);
      Assertions.assertEquals("alex.smith@example.com", email);

      Name name = users.users().get(3).name();
      System.out.println("name 4th user: " + name.first() +" " + name.last());
      Assertions.assertEquals("Emily", name.first());
      Assertions.assertEquals("Johnson", name.last());

      int age = users.users().get(4).age();
      System.out.println("age 5th user: " + age);
      Assertions.assertEquals(50, age);

      String role = users.users().get(4).role();
      System.out.println("role 5th user: " + role);
      Assertions.assertEquals("admin", role);

      boolean active = users.users().get(4).active();
      System.out.println("active 5th user: " + active);
      Assertions.assertTrue(active);

      String created = users.users().get(4).created();
      System.out.println("created date of 5th user: " + created);
      Assertions.assertEquals("2021-08-12", created);

    }
  }
}
