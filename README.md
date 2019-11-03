# Spring In Action

## Table of contents:
1. [Chapter 1: Getting started with Spring](#Chapter1)
2. [Chapter 2: Developing web applications](#Chapter2)
3. [Chapter 3: Working with data](#Chapter3)
4. [Chapter 4: Securing Spring](#Chapter4)
5. [Chapter 5: Working with configuration properties](#Chapter5)
6. [Chapter 6: Creating REST services](#Chapter6)
7. [Chapter 7: Consuming REST services](#Chapter7)
8. [Chapter 8: Sending messages asynchronously](#Chapter8)
9. [Chapter 9: Integrating Spring](#Chapter9)
10. [Chapter 10: Introducing Reactor](#Chapter10)
11. [Chapter 11: Developing reactive APIs](#Chapter11)
12. [Chapter 12: Persisting data reactively](#Chapter12)
13. [Chapter 13: Discovering services](#Chapter13)


## Chapter 1: Getting started with Spring<a name="Chapter1"></a>

### What is Spring?
At its core, Spring offers a container, often referred to as the Spring application con- text, that creates and manages 
application components. These components, or beans, are wired together inside the Spring application context to make a 
complete application, this is known as dependency injection (DI).
The following Java-based configuration class creates two beans and injects one into the other:

```java
@Configuration
public class ServiceConfiguration {
  @Bean
  public InventoryService inventoryService() {
    return new InventoryService();
  }
  @Bean
  public ProductService productService() {
    return new ProductService(inventoryService());
  }
}
```

The _@Configuration_ annotation indicates to Spring that this is a configuration class that will provide beans to the Spring
 application context. By default, the bean IDs will be the same as the names of the methods that define them. Spring Boot 
 can make reasonable guesses of what components need to be configured and wired together, based on entries in the 
 classpath, environment variables, and other factors.

### Initializing a Spring application
The Spring Initializr is both a browser-based web application and a REST API, which can produce a skeleton Spring project
 structure that you can flesh out with whatever functionality you want. 
The Spring Boot plugin performs a few important functions:

    * It provides a Maven goal that enables you to run the application using Maven
    * It ensures that all dependencies are included within the executable JAR file and available on the runtime classpath
    * It produces a manifest file in the JAR file that denotes the bootstrap class as the main class for the executable JAR

You need at least a minimal amount of Spring configuration to bootstrap the application, the standard way to do it is to 
mark the main class with @SpringBootApplication, which is a composite application that combines three other annotations:

    * @SpringBootConfiguration—Designates this class as a configuration class. This annotation is, in fact, a specialized 
    form of the @Configuration annotation
    * @EnableAutoConfiguration—Enables Spring Boot automatic configuration. This annotation tells Spring Boot to 
    automatically configure any components that it thinks you’ll need
    * @ComponentScan—Enables component scanning. This lets you declare other classes with annotations like @Component, 
    @Controller, @Service, and others, to have Spring automatically discover them and register them as components in the 
    Spring application context
 
Then this class will be used to load the configuration:
   
```java
@SpringBootApplication
public class YourMainClass {

    public static void main(String[] args) {
        SpringApplication.run(YourMainClass.class, args);
    }
}
```

The main() method calls a static run() method on the SpringApplication class, which performs the actual bootstrapping of 
the application, creating the Spring application context. Spring Initializr gives you also a test class to get started. 
The class annotated with @RunWith(SpringRunner.class). @RunWith is a JUnit annotation, providing a test runner that guides
JUnit in running a test. SpringRunner is an alias for SpringJUnit4ClassRunner, and was introduced in Spring 4.3 to remove
the association with a specific version of JUnit.

#### Handling web requests
Spring comes with a powerful web framework known as Spring MVC. At the center of Spring MVC is the concept of a controller, 
 a class that handles requests and responds with information of some sort.
A Class annotated with @Controller, makes possible for Spring’s component scanning to automatically discovers it and create
 an instance of the controller class as a bean in the Spring application context.
_@GetMapping_ in a method indicates that if an HTTP GET request is received for the configured path (the parameter in the 
annotation), then this method should handle requests to that path. It does so (in the example) by doing nothing more than 
returning a String value of home. This value is interpreted as the logical name of a view.
_@WebMvcTest_ is a special test annotation provided by Spring Boot that arranges for the test to run in the context of a 
Spring MVC application, it also sets up Spring support for testing Spring MVC.
Spring boot comes with an embedded Tomcat server in the Spring application context.

#### Getting to know Spring Boot DevTools
As its name suggests, DevTools provides Spring developers with some handy develop- ment-time tools. Among those are

    * Automatic application restart when code changes
    * Automatic browser refresh when browser-destined resources changes (templates,JavaScript, stylesheets, and so on)
    * Automatic disable of template caches
    * Built in H2 Console if the H2 database is in use
    

## Chapter 2: Developing web applications<a name="Chapter2"></a>

Spring allows you to define DTO with like the one below:

```java
package tacos;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Ingredient {
      
    private final String id;
    private final String name;
    private final Type type;
      
    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
```

Some useful class level annotations on Spring MVC:

    * @Controller: This annotation serves to identify this class as a controller
    * @Request: Specifies the kind of requests that this controller handles (paths beginning with the specified annotation
     attribute
    * @Slf4j: A Lombok-provided annotation that, at runtime, will automatically generate an SLF4J Logger in the class
    
Spring method annotations to handle HTTP requests:

    * @RequestMapping: General-purpose request handling
    * @GetMapping: Handles HTTP GET requests
    * @PostMapping: Handles HTTP POST requests
    * @PutMapping: Handles HTTP PUT requests
    * @DeleteMapping: Handles HTTP DELETE requests
    * @PatchMapping: Handles HTTP PATCH requests

The spring Model class is an object that ferries data between a controller and whatever view is charged with rendering that 
data. Data that’s placed in Model attributes is copied into the servlet response attributes, where the view can find them. 
Thymeleaf templates are just HTML with some additional element attributes that guide a template in rendering request data. 
The `${}` operator tells it to use the value of a request attribute, `th:each` iterates over a collection of elements, 
rendering the HTML once for each item in the collection.
In Spring MVC, we can indicate that a method should perform a redirect by prepending the name of the view to redirect with
 the 'redirect:' prefix.
 
### Validating form input 
Spring supports Java’s Bean Validation API (JSR-303). To apply validation in Spring MVC, you need to:

    * Declare validation rules on the class that is to be validated
    * Specify that validation should be performed in the controller methods that require validation
    * Modify the form views to display validation errors
    

The Validation API offers several annotations that can be placed on properties of domain objects to declare validation rules.
There is a number of annotations that deals with different validations such as @NotNull, @CreditCardNumber, @Pattern (for 
regex), @Digits...
To validate a submitted DTO, you need to add the Java Bean Validation API’s @Valid annotation to the argument of 
the controller's method: 

```java
public String doSomething(@Valid Model model, Errors errors){
    //Something 
}
```

If there are any validation errors, the details of those errors will be captured in an Errors object that’s passed into 
the method's error argument.On the view side, Thymeleaf offers convenient access to the Errors object via the fields 
property and with its th:errors attribute. It also has a conditional th:if tag to display errors if they exists:

```html
<label for="ccNumber">Credit Card #: </label>
        <input type="text" th:field="*{ccNumber}"/>
        <span class="validationError"
              th:if="${#fields.hasErrors('ccNumber')}"
              th:errors="*{ccNumber}">CC Num Error</span>
```

### Working with view controllers
When a controller is simple enough that it doesn’t populate a model or process input, there’s another way that you can 
define the controller. A view controller is a controller that does nothing but forward the request to a view, and it can 
be register as follows:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }
}
```

WebMvcConfigurer defines several methods for configuring Spring MVC, the `addViewControllers()` method is given a 
ViewControllerRegistry that you can use to register one or more view controllers.

### Choosing a view template library
Spring can work with a handfull of template libraries like Thymeleaf, Groovy templates, JSF, JSP, FreeMarker, Mustache...
Spring boot can automatically configure your templating choice (templates must be in the /templates directory (under the 
src/main/resources directory).
By default, templates are only parsed once, when they’re first used, and the results of that parse are cached for 
subsequent use. This behaviour can be changed by setting the appropriate template caching property (depends on the 
templating library) to false:


| Template         | Cache enable property        |
| ---------------- | ---------------------------- |
| FreeMarker       | spring.freemarker.cache      |
| Groovy Templates | spring.groovy.template.cache |
| Mustache         | spring.mustache.cache        |
| Thymeleaf        | spring.thymeleaf.cache       |

One option is to set the above property in a profile so it is not deployed in production.


## Chapter 3: Working with data<a name="Chapter3"></a>

Spring JDBC support is rooted in the JdbcTemplate class. JdbcTemplate provides a means by which developers can perform SQL
 operations against a relational database.
 
```java
class IngredientDAO{

    private JdbcTemplate jdbc;

    @Override
    public Ingredient findOne(String id) {
      return jdbc.queryForObject(
          "select id, name, type from Ingredient where id=?",
          this::mapRowToIngredient, id);
    }
    private Ingredient mapRowToIngredient(ResultSet rs, int rowNum)
        throws SQLException {
      return new Ingredient(
          rs.getString("id"),
          rs.getString("name"),
          Ingredient.Type.valueOf(rs.getString("type")));
    }
}
```

Support for JdbcTemplate is given in the package

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

The `query()` method accepts the SQL for the query as well as an implementation of Spring’s RowMapper for the purpose of 
mapping each row in the result set to an object. `queryForObject()` works much like `query()` except that it returns a single
 object instead of a List of objects. _JdbcTemplate’s_ `update()` method can be used for any query that writes or updates 
 data in the database.
One important thing to know, is that if there’s a file named _schema.sql_ in the root of the application’s classpath, then
 the SQL in that file will be executed against the database when the application starts. Spring Boot will also execute a 
 file named _data.sql_ from the root of the classpath when the application starts.
 
### Inserting data
Two ways to save data with JdbcTemplate include the following:

    * Directly, using the update() method
    * Using the SimpleJdbcInsert wrapper class
    
_@SessionAttributes_ (class annotation) and _@ModelAttribute_ (method annotation) ensures that an object of the type 
annotated by this will be created in the model. The _@SessionAttributes_ annotation specifies any model objects that 
should be kept in session and available across multiple requests. The parameter annotated with @ModelAttribute  is to 
indicate that its value should come from the model and that Spring MVC  shouldn't attempt to bind request parameters to it.
Once the object is saved, you don’t need it hanging around in a session anymore. In fact, if you don’t clean it out, the 
object remains in session and the next order will start with whatever state the session was left in. 

_SimpleJdbcInsert_ is an object that wraps JdbcTemplate to make it easier to insert data into a table.
SimpleJdbcInsert has a couple of useful methods for executing the insert: _execute()_ and _executeAndReturnKey()_. Both 
accept a Map<String, Object>, where the map keys correspond to the column names in the table the data is inserted into. 
The map values are inserted into those columns.

### Persisting data with Spring Data JPA
The Spring Data project is a rather large umbrella project comprised of several sub-projects: Spring Data JPA, Spring Data 
MongoDB, Spring Data Neo4j, Spring Data Redis and Spring Data Cassandra. Spring Data has the ability to automatically 
create repositories for all of these projects. 
To include Spring JPA use:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

In order to declare an Object as a JPA entity, it must be annotated with _@Entity_, and its id property must be annotated 
with @Id to designate it as the property that will uniquely identify the entity in the database. JPA requires that 
entities have a no-arguments constructor, so Lombok’s _@NoArgsConstructor_ does that for you (if you don't want to use it, 
set the access attribute to _AccessLevel.PRIVATE_). When a @NoArgsConstructor is used, the constructor with args gets 
removed. An explicit _@RequiredArgsConstructor_ ensures that you’llstill have a required arguments constructor in addition 
to the private no-arguments constructor.
The _@PrePersist_ annotation allows you to set a property value before the object is persisted (i.e. a timestamp).
_@Table_ specifies the name of the table this annotated entity should be persisted to.
With Spring Data, you can extend the _CrudRepository_ interface, when the application starts, Spring Data JPA  would make 
available a dozen methods for create, update, delete and select, and it will automatically generates an implementation on 
the fly for those (given the parameterized class it deals with, and the type of its id). It is also possible to customize 
the methods you might need inside the repositories, Spring Data examines any methods in the repository interface, parses 
the method name, and attempts to understand the method’s purpose in the context of the persisted object. 
Repository methods are composed of a verb, an optional subject, the word By, and a predicate:

    * Spring Data also understands find, read, and get as synonymous for fetching one or more entities
    * You can also use count as the verb if you only want the method to return an int with the count of matching entities
    * Use 'And' as the conjunction to joint to properties as in 'findByUserNameAndEmail(...)'
    * Use Between to define a value that must fall between the given range
    * The method parameter order must match that in the method name

In addition to an implicit Equals operation and the Between operation, Spring Data method signatures can also include any 
of these operators:

    * IsAfter, After, IsGreaterThan, GreaterThan  IsGreaterThanEqual, GreaterThanEqual
    * IsBefore, Before, IsLessThan, LessThan
    * IsLessThanEqual, LessThanEqual
    * IsBetween, Between 
    * IsNull, Null
    * IsNotNull, NotNull 
    * IsIn, In
    * IsNotIn, NotIn
    * IsStartingWith, StartingWith, StartsWith 
    * IsEndingWith, EndingWith, EndsWith 
    * IsContaining, Containing, Contains 
    * IsLike, Like
    * IsNotLike, NotLike
    * IsTrue, True
    * IsFalse, False
    * Is, Equals
    * IsNot, Not
    * IgnoringCase, IgnoresCase, AllIgnoreCase (to ignore case for all String comparisons)
    
Finally, you can also place OrderBy at the end of the method name to sort the results by a specified column. If the naming
 convention gets too complex, you can also use _@Query_ to explicitly specify the query to be performed when the method is
 called. 
    

## Chapter 4: Securing Spring<a name="Chapter4"></a>

To add spring security to your application, you need to put the following dependency into your pom.xml:

```xml
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

With this dependency, when the application starts, autoconfiguration will detect that Spring Security is in the classpath 
and will set up some basic security configuration. After adding this, you'll be prompted for user and pass if you try to 
access the main page (user is 'user' and the pass appears in the generated log files). This is because spring security 
adds the following features:

    * All HTTP request paths require authentication
    * No specific roles or authorities are required
    * There’s no login page
    * Authentication is prompted with HTTP basic authentication
    * There’s only one user; the username is user

### Configuring Spring Security
Spring Security offers several options for configuring a user store, including these:

    * An in-memory user store
    * A JDBC-based user store
    * An LDAP-backed user store 
    * A custom user details service
    
By overriding a `configure()` method defined in the _WebSecurityConfigurerAdapter_ configuration base class, you can 
select which one of the previous options you want to use:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

@Override protected void configure(AuthenticationManagerBuilder auth) throws Exception {...}

}
```

#### In memory User Store
For example, to use the in memory user store (for small number of users not likely to change), do:

```java
@Override 
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("buzz").password("infinity").authorities("ROLE_USER")
        .and()
        .withUser("woody").password("bullseye").authorities("ROLE_USER");
}
```

#### JDBC User Store
Similarly, to use a JDBC-based user store:

```java
@Autowired
DataSource dataSource;

@Override protected void configure(AuthenticationManagerBuilder auth)throws Exception {
    auth.jdbcAuthentication().dataSource(dataSource); 
}
```

This minimal configuration will make some assumptions about your database schema, the queries user to retrieve the 
security configuration are shown below:

```java
public static final String DEF_USERS_BY_USERNAME_QUERY =
        "select username,password,enabled from users where username = ?";
public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY =
        "select username,authority from authorities where username = ?";
public static final String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY =
        "select g.id, g.group_name, ga.authority from groups g, group_members gm, group_authorities ga " +
        "where gm.username = ? and g.id = ga.group_id and g.id = gm.group_id";
```

But it is also possible to configure your own queries:

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  auth.jdbcAuthentication().dataSource(dataSource)
    .usersByUsernameQuery("select username, password, enabled from Users where username=?")
    .authoritiesByUsernameQuery("select username, authority from UserAuthorities where username=?")
    .passwordEncoder(new StandardPasswordEncoder("53cr3t");;
}
```

To avoid passwords to be stored in the DB in plain text this, spring provides multiple password encoders:

    * BCryptPasswordEncoder—Applies bcrypt strong hashing encryption 
    * NoOpPasswordEncoder—Applies no encoding
    * Pbkdf2PasswordEncoder—Applies PBKDF2 encryption
    * SCryptPasswordEncoder—Applies scrypt hashing encryption
    * StandardPasswordEncoder—Applies SHA-256 hashing encryption

You can also provide your own implementation of password encoder, by implementing the following interface:

```java
public interface PasswordEncoder {
  String encode(CharSequence rawPassword);
  boolean matches(CharSequence rawPassword, String encodedPassword);
}
```

#### LDAP-backed User Store
Similarly to the ones above, to use LDAP, simply do:

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  auth.ldapAuthentication().userSearchFilter("(uid={0})").groupSearchFilter("member={0}");
}
```
By default, the base queries for both users and groups are empty, indicating that the search will be done from the root of
 the LDAP hierarchy. But you can change that by specifying a query base:
 
 ```java

@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  auth.ldapAuthentication().userSearchBase("ou=people").userSearchFilter("(uid={0})")
    .groupSearchBase("ou=groups").groupSearchFilter("member={0}");
}
```


The default strategy for authenticating against LDAP is to perform a bind operation, authenticating the user directly to 
the LDAP server. Another option is to perform a comparison operation. This involves sending the entered password to the 
LDAP directory and asking the server to compare the password against a user’s password attribute. You can do password 
comparison by adding `.passwordCompare();` to the above.
The password given in the login form will be compared with the value of the userPassword attribute in the user’s LDAP 
entry. If the password is kept in a different attribute, you can specify the attribute’s name with `passwordAttribute()`. 
As with JDBC, we can define a password encoder (encoder should match that in the LDAP server).
By default, Spring Security’s LDAP authentication assumes that the LDAP server is listening on port 33389 on localhost. To
configure a remote server, use `.contextSource().url("ldap://yourserver:389/dc=yourapp,dc=com");`. Spring allows you to 
define an embedded LDAP server, instead of setting the URL to a remote LDAP server, you can specify the root suffix for 
the embed- ded server via the _root()_ method: `.contextSource().root("dc=yourapp,dc=com");`. When this LDAP server starts, 
it will attempt to load data from any LDIF files that it can find in the classpath (or use the `ldif()` method to define 
the lookup path).

#### Customizing user authentication
It is also possible to define your own entity to store user credentials, for example to add other details like address or 
phone. To do that, implement the Spring's _UserDetails_ interface. With this class created, we can create the following:

```java
public interface UserDetailsService {
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
``` 

The loadByUsername() method has one simple rule: it must never return null. Once more, to use this security configuration,
 you'll need to implement the `configure()` method, and pass the _UserDetailService_ to the _AuthenticationManagerBuilder_.
 
```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception { auth.userDetailsService(userDetailsService); }
```

### Securing web requests
To configure security rules, we have other options in _WebSecurityConfigurer_ other configure() method:

```java
@Override
protected void configure(HttpSecurity http) throws Exception {...}
```

This method accepts an HttpSecurity object, which can be used to configure how security is handled at the web level. 
Among the many things you can configure with HttpSecurity are these:

    * Requiring that certain security conditions be met before allowing a request to be served
    * Configuring a custom login page
    * Enabling users to log out of the application
    * Configuring cross-site request forgery protection
    
#### Securing requests
The call to authorizeRequests() returns an object (ExpressionInterceptUrlRegistry) on which you can specify URL paths and 
patterns and the security requirements for those paths. The order of these rules is important. Security rules declared 
first take precedence over those declared lower down. The following is a list of all methods available:

| METHOD                     | WHAT IT DOES                                                      |
|----------------------------|-------------------------------------------------------------------|
| access(String)             | Allows access if the given SpEL expression evaluates to true      |
| anonymous()                | Allows access to anonymous users                                  |
| authenticated()            | Allows access to authenticated users                              |
| denyAll()                  | Denies access unconditionally                                     |
| fullyAuthenticated()       | Allows access if the user is fully authenticated (not remembered) |
| hasAnyAuthority(String...) | Allows access if the user has any of the given authorities        |
| hasAnyRole(String...)      | Allows access if the user has any of the given roles              |
| hasAuthority(String)       | Allows access if the user has the given authority                 |
| hasIpAddress(String)       | Allows access if the request comes from the given IP address      |
| hasRole(String)            | Allows access if the user has the given role                      |
| not()                      | Negates the effect of any of the other access methods             |
| permitAll()                | Allows access unconditionally                                     |
| rememberMe()               | Allows access for users who are authenticated via remember-me     |

Similar methods to the ones above, are defined to accept SpEL expression instead of strings. With this, an expression like
 the one below would be valid:

```java
http.authorizeRequests().antMatchers("/design", "/orders")
    .access("hasRole('ROLE_USER') && T(java.util.Calendar).getInstance().get(T(java.util.Calendar).DAY_OF_WEEK) == T(java.util.Calendar).TUESDAY")  
```

#### Creating a custom login page
To replace the built-in login page, you first need to tell Spring Security what path your custom login page will be at:

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
  http
    .authorizeRequests()
    .antMatchers("/design", "/orders")
    .access("hasRole('ROLE_USER')")
    .antMatchers(“/”, "/**").access("permitAll")
    .and()
    .formLogin().loginPage("/login");
}
```

The call to `loginPage()` designates the path where your custom login page will be provided. When Spring Security determines 
that the user is unauthenticated and needs to log in, it will redirect them to this path. With this configuration, Spring 
Security listens for login requests at _/login_ and expects that the username and password fields be named username and 
password (although this can be configured with the `.usernameParameter("user")` and `.passwordParameter("pwd")` methods).
If the user were to directly navigate to the login page, a successful login would take them to the root path (for example, 
the homepage). But you can change that by specifying a default success page using the `.defaultSuccessUrl("/page")` method. 

#### Logging out
To enable logout, you simply need to call logout on the HttpSecurity object: `.and().logout().logoutSuccessUrl("/")`, 
which is used to clear out the session. The method `.logoutSuccessUrl("/")` serves to define the redirection after logout.

#### Preventing cross-site request forgery
Cross-site request forgery (CSRF) is a common security attack. It involves subjecting a user to code on a maliciously 
designed web page that automatically (and usually secretly) submits a form to another application on behalf of a user who 
is often the victim of the attack. To protect against such attacks, applications can generate a CSRF token upon displaying a
form, place that token in a hidden field, and then stow it for later use on the server. Spring Security has built-in CSRF
protection enabled by default (can be disable with`.and().csrf().disable()`). You only need to make sure that any forms 
your application submits include a field named _\_csrf_ that contains the CSRF token. Spring Security even makes that 
easy by placing the CSRF token in a request attribute with the name _csrf: `<input type="hidden" name="_csrf" 
th:value="${_csrf.token}"/>`. Thymeleaf will include this for you as long as one of the attributes of the <form> element 
is prefixed as a Thymeleaf attribute.
 
### Knowing your user
There are several ways to determine who the user is. These are a few of the most common ways:

    * Inject a Principal object into the controller method
    * Inject an Authentication object into the controller method 
    * Use SecurityContextHolder to get at the security context
    * Use an @AuthenticationPrincipal annotated method

An example of this can be seen below:

```java
@PostMapping
public String processOrder(@Valid Order order, Errors errors,SessionStatus sessionStatus,Authentication authentication) {
    ...
      User user = (User) authentication.getPrincipal();
      order.setUser(user);
    ...
}
```

Perhaps the cleanest solution of all, however, is to simply accept a User object in processOrder(), but annotate it with 
_@AuthenticationPrincipal_ so that it will be the authentication’s principal:

```java
@PostMapping
public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus,
    @AuthenticationPrincipal User user) {...}
```


## Chapter 5: Working with configuration properties<a name="Chapter5"></a>

There are two different (but related) kinds of configurations in Spring:

    * Bean wiring—Configuration that declares application components to be created as beans in the Spring application 
      context and how they should be injected into each other
    * Property injection—Configuration that sets values on beans in the Spring application context
    
#### Understanding Spring’s environment abstraction
The Spring environment abstracts the origins of properties so that beans needing those properties can consume them from 
Spring itself. The properties can come from JVM system properties, operating system environment variables, command-line 
arguments or application property configuration files (when setting properties as environment variables, the naming style 
is slightly different to accommodate restrictions placed on environment variable names by the operating system).

#### Configuring a data source
Although you could explicitly configure your own DataSource bean, that’s usually unnecessary. Spring Boot can even figure 
it out the JDBC driver class from the structure of the database URL. The DataSource bean will be pooled using Tomcat’s 
JDBC connection pool if it’s avail- able on the classpath. If not, Spring Boot looks for and uses one of these other 
connection pool implementations on the classpath (HikariCP and Commons DBCP 2). With spring boot, you can specify the 
database initialization scripts to run when the application starts:

```yaml
spring:
  datasource:
    schema:
    - DBCstructure.sql
    data:
    - data.sql
```

You can also configure your data source in JNDI and have Spring look it up from there:

```yaml
spring:
  datasource:
    jndi-name: java:/comp/env/jdbc/tacoCloudDS
```

#### Configuring the embedded server
When setting the server port, if 0 is chosen, spring will randomly chosen available port (i.e. tests). If the server is 
set to use HTTPS, after setting up a keystore, you can configure it as:

```yaml
server:
  port: 8443
  ssl:
    key-store: file:///path/to/mykeys.jks
    key-store-password: pass
    key-password: pass
```

#### Configuring logging
By default, Spring Boot configures logging via Logback to write to the console at an INFO level. For full control over the
 logging configuration, you can create a logback.xml file at the root of the classpath. To set the logging levels, you 
 create properties that are prefixed with logging.level, followed by the name of the logger. The logging.path and logging
 .file properties can help to set to which file to write the logs (By default, the log files rotate once they reach 10 MB 
 in size).
 
#### Using special property values
When setting properties, you aren’t limited to declaring their values as hard-coded String and numeric values. Instead, 
you can derive their values from other configuration properties. To achieve this, you could use the ${} placeholder markers.

### Creating your own configuration properties
To support property injection of configuration properties, Spring Boot provides the @ConfigurationProperties annotation. 
When placed on any Spring bean, it specifies that the properties of that bean can be injected from properties in the Spring
 environment. _Pageable_ is Spring Data’s way of selecting some subset of the results by a page number and page size.
The _@ConfigurationProperties_ annotation has an attribute _prefix_ that can be used to define the prefix that would be 
added to the attribute of the class that is marked with it, in order to resolve the property name to take the value from. 
i.e. if there is a property in a class named size, and the _@ConfigurationProperties(prefix="com.test")_, the property 
name would be resolved as "com.test.size".

#### Defining configuration properties holders
_@ConfigurationProperties_ are in fact often placed on beans whose sole purpose in the application is to be holders of 
configuration data. In this way, if you need to add, remove, rename, or otherwise change the prop- erties therein, you 
only need to apply those changes in one class.

#### Declaring configuration property metadata
Configuration property metadata is completely optional and doesn’t prevent configuration properties from working. But the 
metadata can be useful for providing some minimal documentation around the configuration properties, especially in the IDE.
To create metadata for your custom configuration properties, you’ll need to create a file under the META-INF (for example,
 in the project under src/main/resources/ META-INF) named additional-spring-configuration-metadata.json. i.e.:
 
```json
{
  "properties": [
    {
      "name": "taco.orders.page-size",
      "type": "java.lang.String",
      "description": "Sets the maximum number of orders to display in a list"
  } ]
}
```

### Configuring with profiles
When applications are deployed to different run-time environments, usually some configuration details differ. One way to 
configure properties uniquely in one environment over another is to use environment variables to specify configuration 
properties instead of defining them in _application.properties_ and _application.yml_. Profiles are a type of conditional 
configuration where different beans, configuration classes, and configuration properties are applied or ignored based on 
what profiles are active at runtime.

#### Defining profile-specific properties
One way to define profile-specific properties is to create yet another YAML or properties file containing only the 
properties for production. The name of the file should follow this convention: _application-{profile name}.yml_ or 
_application-{profile name}.properties_.
Another way to specify profile-specific properties works only with YAML configuration. It involves placing profile-specific 
properties alongside non-profiled properties in _application.yml_, separated by three hyphens and the _spring.profiles_ 
property to name the profile:

```yaml
logging:
  level:
    app: DEBUG # Applies to all profiles (default profile)
--- 
spring:
  profiles: prod # Profile specific config
  datasource:
    url: jdbc:mysql://localhost/tacocloud
    username: tacouser
    password: tacopassword
...    
```

### Activating profiles
To make a profile active, include it in the list of profile names given to the _spring.profiles.active_ property. The profile
 active and the corresponding configuration properties would take precedence over the properties in the default profile.
 
#### Conditionally creating beans with profiles
Sometimes it’s useful to provide a unique set of beans for different profiles, the _@Profile_ annotation can designate 
beans as only being applicable to a given profile. It’s also possible to use _@Profile_ on an entire _@Configuration_ 
annotated class. i.e.

```java
@Configuration
@Profile({"dev", "qa", "!prod"}) // Multiple profiles can be passed, ! negates the profile
public class DevelopmentConfig {...}
```

## Chapter 6: Creating REST services<a name="Chapter6"></a>

### Retrieving data from the server
The _@RestController_ annotation serves two purposes. First, it’s a stereotype annotation like _@Controller_ and _@Service_ 
that marks a class for discovery by component scanning. Also, it tells Spring that all handler methods in the controller 
 should have their return value written directly to the body of the response, rather than being carried in the model to a 
 view for rendering.
You could also annotate the  _@Controller_, just like with any Spring MVC controller. But then you’d need to also annotate
 all of the handler methods with _@ResponseBody_ to achieve the same result (or return a _ResponseEntity_ object). 
If the handler methods in your controller has a producer attribute, it will only handle requests if the request’s Accept 
header includes the same accepts content type (i.e. "application/json"). This allows other controllers to handle requests 
with the same paths, so long as those requests don’t require the same content type output (you can also produce several 
type of contents in the same controller).
With Angular, the portion of the application will be running on a separate host and/or port from the API (at least for 
now), the web browser will prevent your Angular client from consuming the API. This restriction can be overcome by 
including CORS (Cross-Origin Resource Sharing) headers in the server responses (or the @CrossOrigin annotation).
You can leverage the REST syntaxis to pass variables as path parameters by annotating the methods like this:
```@GetMapping("/example/{id}") public Model getById(@PathVariable("id") Long id) {...}``` The controller's base path and 
the method paths are additive.

### Sending data to the server
An example of how to define a method in a controller to accept content instead of producing it, is below:

```java
@PostMapping(path ="/somePath", consumes="application/json")
@ResponseStatus(HttpStatus.CREATED) //Replies with 201 code on completion
public Model postModel(@RequestBody Model model) { //The json message is converted to the Model object
  return modelRepo.save(model);
}
```

Without the _@RequestBody_ annotation, Spring assume that you want request parameters (either query parameters or form 
parameters).

### Updating data on the server
GET is the verb used to fetch data from server, PUT is really intended to perform a wholesale replacement operation rather 
than an update operation. In contrast, the purpose of HTTP PATCH is to perform a patch or partial update of resource data 
(_@PatchMapping_ is available in Spring).

### Deleting data from the server
Spring MVC’s _@DeleteMapping_ comes in handy for declaring methods that handle DELETE requests.

## Enabling hypermedia
Hypermedia as the Engine of Application State, or HATEOAS, is a means of creating self-describing APIs wherein resources 
returned from an API contain links to related resources. An example of the response of such API is shown:


```json
    {
      "_embedded": {
        "tacoResourceList": [
          {
            "name": "Veg-Out",
            "createdAt": "2018-01-31T20:15:53.219+0000",
            "ingredients": [
              {
                "name": "Flour Tortilla", "type": "WRAP",
                "_links": {
                  "self": { "href": "http://localhost:8080/ingredients/FLTO" }
                }
              }]
          }],
      "_links": {
        "recents": {
          "href": "http://localhost:8080/design/recent"
      }
    }
  }   
}
```
Each element in this in this example includes a property named _links that contains hyperlinks for the client to navigate 
the API. The Spring HATEOAS project brings hyperlink support to Spring. It offers a set of classes and resource assemblers
 that can be used to add links to resources before returning them from a Spring MVC controller. We can add support for 
 HATEOAS with the following dependency:
 
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
 ```
 
### Adding hyperlinks
Spring HATEOAS provides two primary types that represent hyperlinked resources: Resource and Resources. The Resource type 
represents a single resource, whereas Resources is a collection of resources. The most useful of the Spring HATEOAS link 
builders is ControllerLinkBuilder. This link builder is smart enough to know what the hostname is without you having to 
hardcode it. And it provides a handy fluent API to help you build links relative to the base URL of any controller.

### Creating resource assemblers
Adding links to embedded lists of resources can be tedious, therefore Spring provides resource assemblers. Rather than let
 Resources.wrap() create a Resource object for each taco in the list, you’re going to define a utility class that converts
Model objects to a new ModelResource object. This new Resource should extends _ResourceSupport_ to inherit a list of Link 
object and methods to manage the list of links. In addition to this, you need to create a ResouceAssembler.
The ResourceAssembler has a default constructor that informs the superclass (ResourceAssemblerSupport) that it will be 
using the passed controller class to determine the base path for any URLs in links it creates when creating a new Resource.

### Naming embedded relationships
If you were to refactor the name of the Resource class to something else, the field name in the resulting JSON would 
change to match it. The _@Relation_ annotation can help break the coupling between the JSON field name and the resource type
 class names as defined in Java:
 `@Relation(value="taco", collectionRelation="tacos") public class TacoResource extends ResourceSupport {...}`
 
## Enabling data-backed services
Spring Data REST is another member of the Spring Data family that automatically creates REST APIs for repositories created
 by Spring Data. Add it with:
 
 ```xml
 <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-data-rest</artifactId>
 </dependency>
 ```
 By simply having the Spring Data REST starter in the build, the application gets auto-configuration that enables 
 automatic creation of a REST API for any repositories that were created by Spring Data (including Spring Data JPA, Spring
  Data Mongo and others).
One thing you might want to do is set a base path for the API so that its endpoints are distinct and don’t collide with 
 any controllers you write. For that, you need to define the _spring.data.rest.base-path_ property, which sets the base path
 for Spring Data REST endpoints. 
 
### Adjusting resource paths and relation names
When creating endpoints for Spring Data repositories, Spring Data REST tries to pluralize the associated entity class (for
 the _Model_ entity, the endpoint is _Models_). Spring Data REST also exposes a home resource (at the url defined by the 
 spring.data.rest.base-path property) that has links for all exposed endpoints so you can find the generated urls for your
 resources. By adding a simple annotation to your model class, you can tweak both the relation name and that path for the
 resource: 
 
```java
@Data
@Entity
@RestResource(rel="tacos", path="tacos")
public class Taco {...}
```

### Paging and sorting
By default, requests to a collection resource such as /api/tacos will return up to 20 items per page from the first page, 
although it can be adjusted by specifying the _page_ (0-based) and _size_ parameters in your request.
The _sort_ parameter lets you sort the resulting list by any property of the entity.

### Adding custom endpoints
When you write your own API controllers, their endpoints seem somewhat detached from the Spring Data REST endpoints in a 
couple of ways:

    * Your own controller endpoints aren’t mapped under Spring Data REST’s base path. You could force their mappings to be 
    prefixed with whatever base path you want, including the Spring Data REST base path, but if the base path were to 
    change, you’d need to edit the controller’s mappings to match.
    * Any endpoints you define in your own controllers won’t be automatically included as hyperlinks in the resources 
    returned by Spring Data REST end- points. This means that clients won’t be able to discover your custom endpoints with
    a relation name.
    
Spring Data REST includes _@RepositoryRestController_, a new annotation for annotating controller classes whose mappings 
should assume a base path that’s the same as the one configured for Spring Data REST endpoints. All mappings in a 
controller annotatde with this would be prefixed with the value of the _spring.data.rest.base-path_ property.
One important thing to notice is that although _@RepositoryRestController_ is named similarly to _@RestController_, it 
doesn’t carry the same semantics as _@RestController_. Specifically, it doesn’t ensure that values returned from handler 
methods are automatically written to the body of the response. Therefore you need to either annotate the method with 
 _@ResponseBody_ or return a ResponseEntity that wraps the response data. Here you chose to return a ResponseEntity.
 
### Adding custom hyperlinks to Spring Data endpoints
By declaring a resource processor bean, you can add links to the list of links that Spring Data REST automatically includes.
Spring offers _ResourceProcessor_, an interface for manipulating resources before they’re returned through the API. 


## Chapter 7: Consuming REST services<a name="Chapter7"></a>

A Spring application can consume a REST API with:

    * RestTemplate — A straightforward, synchronous REST client provided by the core Spring Framework
    * Traverson — A hyperlink-aware, synchronous REST client provided by Spring HATEOAS
    * WebClient — A reactive, asynchronous REST client

### Consuming REST endpoints with RestTemplate
RestTemplate provides 41 methods for interacting with REST resources. The most important of them are:

| Method        | Description                                                                                              |
|---------------|----------------------------------------------------------------------------------------------------------|
| delete(...)   |Performs an HTTP DELETE request on a resource at a specified URL                                          |
| exchange(...) | Executes a specified HTTP method against a URL, returning a ResponseEntity containing an object mapped from the response body |
| execute(...)  | Executes a specified HTTP method against a URL, returning an object mapped from the response body        |
| getForEntity(...) | Sends an HTTP GET request, returning a ResponseEntity containing an object mapped from the response body |
| getForObject(...) | Sends an HTTP GET request, returning an object mapped from a response body                           |
| headForHeaders(...) | Sends an HTTP HEAD request, returning the HTTP headers for the speci- fied resource URL |
| optionsForAllow(...) | Sends an HTTP OPTIONS request, returning the Allow header for the specified URL |
| patchForObject(...) | Sends an HTTP PATCH request, returning the resulting object mapped from the response body |
| postForEntity(...) | POSTs data to a URL, returning a ResponseEntity containing an object mapped from the response body |
| postForLocation(...) | POSTs data to a URL, returning the URL of the newly created resource |
| postForObject(...) | POSTs data to a URL, returning an object mapped from the response body |
| put(...) | PUTs resource data to the specified URL |

With the exception of TRACE, RestTemplate has at least one method for each of the standard HTTP methods. In addition, 
execute() and exchange() provide lower-level, general-purpose methods for sending requests with any HTTP method.
Most of the methods in table 7.1 are overloaded into three method forms:

    * One accepts a String URL specification with URL parameters specified in a variable argument list
    * One accepts a String URL specification with URL parameters specified in a Map<String,String>
    * One accepts a java.net.URI as the URL specification, with no support for parameterized URLs

To use RestTemplate, you’ll either need to create an instance at the point you need it

`RestTemplate rest = new RestTemplate();`

or you can declare it as a bean and inject it where you need it:

`@Bean public RestTemplate restTemplate() { return new RestTemplate();}`

#### GETting resources
The following code uses RestTemplate to fetch an Ingredient object by its ID (you can also use a Map to specify the URL 
variables):

```java
public Ingredient getIngredientById(String ingredientId) {
  return rest.getForObject("http://localhost:8080/ingredients/{id}", Ingredient.class, ingredientId);
}
```

It’s important to know that the variable param- eters are assigned to the placeholders in the order that they’re given.
_getForEntity()_ works in much the same way as _getForObject()_, but instead of returning a domain object that represents the
 response’s payload, it returns a Response- Entity object that wraps that domain object.

#### PUTting resources
All three overloaded variants of put() accept an Object that is to be serialized and sent to the given URL. As for the URL
 itself, it can be specified as a URI object or as a String:
 
```java
public void updateIngredient(Ingredient ingredient) {
          rest.put("http://localhost:8080/ingredients/{id}", ingredient, ingredient.getId());
}
```

#### DELETEing resources
As with the other RestTemplate methods, the URL to delete a resource could be specified as a URI object or the URL 
parameters given as a Map:

```java
public void deleteIngredient(Ingredient ingredient) {
  rest.delete("http://localhost:8080/ingredients/{id}", ingredient.getId());
}
```

#### POSTing resource data
RestTemplate has three ways of sending a POST request, each of which has the same overloaded variants for specifying the URL.

```java
public Ingredient createIngredient(Ingredient ingredient) {
  return rest.postForObject("http://localhost:8080/ingredients", ingredient, Ingredient.class);
}
```

If your client has more need for the location of the newly created resource, then you can call _postForLocation()_ instead:

```java
public URI createIngredient(Ingredient ingredient) {
  return rest.postForLocation("http://localhost:8080/ingredients", ingredient);
}
```

The URI returned is derived from the response’s Location header. In the off chance that you need both the location and 
response payload, you can call _postForEntity()_ with the same parameters than before. If the API you’re consuming 
includes hyperlinks in its response, RestTemplate isn’t very helpful.

### Navigating REST APIs with Traverson
Traverson comes with Spring Data HATEOAS as the out-of-the-box solution for consuming hypermedia APIs in Spring applications.
Working with Traverson starts with instantiating a Traverson object with an API’s base URI:
`Traverson traverson = new Traverson(URI.create("http://localhost:8080/api"), MediaTypes.HAL_JSON);`

You need to point Traverson to the base URL and from here on out, you’ll navigate the API by link relation names:

```java
ParameterizedTypeReference<Resources<Ingredient>> ingredientType =new ParameterizedTypeReference<Resources<Ingredient>>(){};
Resources<Ingredient> ingredientRes = traverson.follow("ingredients").toObject(ingredientType);
Collection<Ingredient> ingredients = ingredientRes.getContent();
```
By calling the follow() method on the Traverson object, you can navigate to the resource whose link’s relation name is 
ingredients. The toObject() method requires that you tell it what kind of object to read the data into. The 
_ParameterizedTypeReference_ helps to deal with java type erasure. Traverson makes it easy to navigate HATEOAS-style 
website, but doesn’t offer any methods for writing to or deleting from those APIs, but _RestTemplate_ does so you can 
combine them:

```java
private Ingredient addIngredient(Ingredient ingredient) {
  String ingredientsUrl = traverson.follow("ingredients").asLink().getHref();
  return rest.postForObject(ingredientsUrl,ingredient,Ingredient.class);
}
``` 


## Chapter 8: Sending messages asynchronously<a name="Chapter8"></a>

Asynchronous messaging is a way of indirectly sending messages from one application to another without waiting for a 
response. 

### Sending messages with JMS
JMS is a Java standard that defines a common API for working with message brokers. Spring supports JMS through a 
template-based abstraction known as JmsTemplate.

#### Setting up JMS
There are several spring starters for JMS depending on the provider (RabitMQ, ActiveMQ...):

```xml
<!-- Active MQ-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-activemq</artifactId>
</dependency>
<!-- Artemis MQ -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-artemis</artifactId>
</dependency>
```

Artemis (nex generation of ActiveMQ), uses by default localhost and port 61616. To set it for other environments, the 
following properties should be set:

    * spring.artemis.host: The broker’s host
    * spring.artemis.port: The broker’s port
    * spring.artemis.user: The user to use to access the broker (optional)
    * spring.artemis.password: The password to use to access the broker (optional)

The same properties for ActiveMQ:

    * spring.activemq.broker-url: The URL of the broker
    * spring.activemq.user: The user to use to access the broker (optional)
    * spring.activemq.password: The password to use to access the broker (optional)
    * spring.activemq.in-memory: Whether or not to start an in-memory broker (default: true)

If you’re using ActiveMQ, you will, however, need to set the `spring.activemq.in-memory` property to false to prevent 
Spring from starting an in-memory broker.

#### Sending messages with JmsTemplate
JmsTemplate has several methods that are useful for sending messages, and eliminates a lot of boilerplate code that would 
otherwise be required to work with JMS.
JmsTemplate really has only two methods, `send()` and `convertAndSend()`, each overridden to support different parameters. 
And if you look closer, you’ll notice that the various forms of `convertAndSend()` can be broken into two subcategories:

    * Three send() methods require a MessageCreator to manufacture a Message object
    * Three convertAndSend() methods accept an Object and automatically convert that Object into a Message behind the scenes
    * Three convertAndSend() methods automatically convert an Object to a Message, but also accept a MessagePostProcessor
      to allow for customization of the Message before it’s sent

Each of these three method categories is composed of three overriding methods that are distinguished by how the JMS 
destination (queue or topic) is specified:

    * One method accepts no destination parameter and sends the message to a default destination
    * One method accepts a Destination object that specifies the destination for the message
    * One method accepts a String that specifies the destination for the message by name

An example of how to send a message is given below:

```java
@Override
public void sendOrder(Order order) {
  jms.send(session -> session.createObjectMessage(order)); //Functional interface implementing MessageCreator
}
```

In order for this to work, you must specify a default destination name with the `spring.jms.template.default-destination` 
property. Using a default destination is the easiest choice, but if you ever need to send a message to a destination other 
than the default destination, you’ll need to specify that destination as a parameter to `send()`. Example of how to create
 a destination:
 
```java
@Bean
public Destination orderQueue() {
  return new ActiveMQQueue("tacocloud.order.queue");
}
```

And use it as a parameter to the send method:

```java
@Override
public void sendOrder(Order order) {
  jms.send(orderQueue, session -> session.createObjectMessage(order));
}
```

If you don't need to configure more parameters in the destination, you can use a simple string with the name of the 
destination instead of the Destination object.

##### Converting Messages Before Sending
JmsTemplates’s convertAndSend() method simplifies message publication by eliminating the need to provide a `MessageCreator`.
Instead, you pass the object that’s to be sent directly to `convertAndSend()`, and the object will be converted into a 
`Message` before being sent. Just like the `send()` method, `convertAndSend()` will accept either a `Destination` or string 
value to specify the destination.

##### Configuring a Message Converter
MessageConverter is a Spring-defined interface that has only two methods to be implemented:

```java
public interface MessageConverter {
  Message toMessage(Object object, Session session) throws JMSException, MessageConversionException;
  Object fromMessage(Message message);
}
```

Spring already offers a handful of implementations for it:

    * MappingJackson2MessageConverter: Uses the Jackson 2 JSON library to convert messages to and from JSON
    * MarshallingMessageConverter: Uses JAXB to convert messages to and from XML
    * MessagingMessageConverter: Converts a Message from the messaging abstraction to and from a Message using an underlying 
      Message-Converter for the payload and a JmsHeaderMapper to map the JMS headers to and from standard message headers
    * SimpleMessageConverter: Converts Strings to and from TextMessage, byte arrays to and from BytesMessage, Maps to and 
      from MapMessage, and Serializable objects to and from ObjectMessage

SimpleMessageConverter is the default, but it requires that the object being sent implement Serializable. To apply a 
different message converter, you must declare an instance of the chosen converter as a bean in the context.

##### Post-processing Messages
Custom headers can be added to the messages to carry extra information. If you were using the `send()` method, this could 
easily be accomplished by calling `setStringProperty()` on the Message object:

```java
jms.send("tacocloud.order.queue", session -> {
        Message message = session.createObjectMessage(order);
        message.setStringProperty("X_ORDER_SOURCE", "WEB");
});
```

This can also be achieved by passing in a `MessagePostProcessor` as the final parameter to `convertAndSend()`, you can do 
whatever you want with the Message after it has been created:

```java
jms.convertAndSend("tacocloud.order.queue", order, new MessagePostProcessor() { 
    @Override
    public Message postProcessMessage(Message message) throws JMSException {
        message.setStringProperty("X_ORDER_SOURCE", "WEB");
        return message; 
    }
});
```

Or even clearer with a functional interface:

```java
jms.convertAndSend("tacocloud.order.queue", order, message -> {
      message.setStringProperty("X_ORDER_SOURCE", "WEB");
      return message;
});
```

#### Receiving JMS messages
When it comes to consuming messages, you have the choice of a pull model, where your code requests a message and waits 
until one arrives, or a push model, in which messages are handed to your code as they become available. JmsTemplate offers 
several methods for receiving messages, but all of them use a pull model. You call one of those methods to request a message,
and the thread blocks until a message is available. You also have the option of using a push model, wherein you define a 
message listener. 

##### Receiving with JMSTemplate
`JmsTemplate` offers several methods for pulling methods which mirrors the `send()` and `convertAndSend()` methods seen 
before. The `receive()` methods receive a raw Message, whereas the `receiveAndConvert()` methods use a configured message 
converter to convert messages into domain types. And for each of these, you can specify either a Destination or a String 
containing the destination name, or you can pull a message from the default destination. For example:

```java
private JmsTemplate jms;
private MessageConverter converter;

public Order receiveOrder() {
    Message message = jms.receive("tacocloud.order.queue");
    return (Order) converter.fromMessage(message);
}
```

The `receiveAndConvert()` converts the payload of a `Message` to a domain type, making the above operation simpler:

```java
public Order receiveOrder() {
    return (Order) jms.receiveAndConvert("tacocloud.order.queue"); //there is no need for a converter anymore
}
```

##### Declaring Message Listeners
A message listener is a passive component that’s idle until a message arrives. To create a message listener that reacts to 
JMS messages, you simply must annotate a method in a component with `@JmsListener`:

```java
@JmsListener(destination = "tacocloud.order.queue")
public void receiveOrder(Order order) {
    // Do something with the order
}
```

The `@JmsListener` annotation is like one of Spring MVC’s request mapping annotations, like `@GetMapping` or `@PostMapping`.
Message listeners are often touted as the best choice because they don’t block and are able to handle multiple messages 
quickly, but when the message handlers need to be able to ask for more messages on their own timing, the pull model 
offered by `JmsTemplate` seems more fitting.

### Working with RabbitMQ and AMQP
Whereas JMS messages are addressed with the name of a destination from which the receiver will retrieve them, AMQP mes- 
sages are addressed with the name of an exchange and a routing key, which are decoupled from the queue that the receiver 
is listening to. When a message arrives at the RabbitMQ broker, it goes to the exchange for which it was addressed. The 
exchange is responsible for routing it to one or more queues. Several type of exchanges exists:

    * Default: A special exchange that’s automatically created by the broker. It routes messages to queues whose name is the 
      same as the message’s routing key. All queues will automatically be bound to the default exchange
    * Direct: Routes messages to a queue whose binding key is the same as the message’s routing key
    * Topic: Routes a message to one or more queues where the binding key (which may contain wildcards) matches the 
      message’s routing key
    * Fanout: Routes messages to all bound queues without regard for binding keys or routing keys
    * Headers: Similar to a topic exchange, except that routing is based on message header values rather than routing keys
    * Dead letter: A catch-all for any messages that are undeliverable (meaning they don’t match any defined 
      exchange-to-queue binding)
      
#### Adding RabbitMQ to Spring
Spring has a starter dependency for ActiveMQ:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

Adding this to your project will create a AMQP connection factory and RabbitTemplate beans, as well as other supporting 
components. Some important configurations:

    * spring.rabbitmq.addresses: A comma-separated list of RabbitMQ broker addresses
    * spring.rabbitmq.host: The broker’s host (defaults to localhost)
    * spring.rabbitmq.port: The broker’s port (defaults to 5672)
    * spring.rabbitmq.username: The username to use to access the broker (optional)
    * spring.rabbitmq.password: The password to use to access the broker (optional)
 
#### Sending messages with RabbitTemplate
At the core of Spring’s support for RabbitMQ messaging is `RabbitTemplate`. With regard to sending messages with 
RabbitTemplate, the `send()` and `convertAndSend()` methods parallel the same-named methods from `JmsTemplate`, although 
`RabbitTemplate` methods send messages in terms of exchanges and routing keys. Below is an example of how to send a message:


```java
public void sendOrder(Order order) {
    MessageConverter converter = rabbit.getMessageConverter();
    MessageProperties props = new MessageProperties();
    Message message = converter.toMessage(order, props);
    rabbit.send("routing.key.value", message); //supplied the routing key, the default exchange will be used
}
public void sendOrderWithConvertAndSend(Order order) {
    rabbit.convertAndSend("tacocloud.order", order);
}
```

The default exchange name and default routing key is an empty String, which corresponds to the default exchange that’s 
automatically created by the RabbitMQ broker (but they both can be set with `spring.rabbitmq.template.exchange` and 
`spring.rabbitmq.template.routing-key` properties).

##### Configuring a Message Converter
By default, message conversion is performed with `SimpleMessageConverter`, which is able to convert simple types (like 
String) and Serializable objects to Message objects. Other convertersare: 

    * Jackson2JsonMessageConverter: Converts objects to and from JSON using the Jackson 2 JSON processor
    * MarshallingMessageConverter: Converts using a Spring Marshaller and Unmarshaller
    * SerializerMessageConverter: Converts String and native objects of any kind using Spring’s serde abstractions
    * SimpleMessageConverter: Converts String, byte arrays, and Serializable types
    * ContentTypeDelegatingMessageConverter: Delegates to another Message-Converter based on the contentType header
    * MessagingMessageConverter: Delegates to an underlying MessageConverter for the message conversion and to an  
      AmqpHeaderConverter for the headers

##### Setting Message Properties
When creating your own Message objects, you can set the header through the MessageProperties instance you give to the 
message converter:

```java
public void sendOrder(Order order) {
  MessageConverter converter = rabbit.getMessageConverter();
  MessageProperties props = new MessageProperties();
  props.setHeader("X_ORDER_SOURCE", "WEB");
  Message message = converter.toMessage(order, props);
  rabbit.send("tacocloud.order", message);
}
```

With convertAndSend(), you don’t have quick access to the Message-Properties object. A MessagePostProcessor can be used:

```java
@Override
public void sendOrder(Order order) {
  rabbit.convertAndSend("tacocloud.order.queue", order, new MessagePostProcessor() {
        @Override
        public Message postProcessMessage(Message message) throws AmqpException {
            MessageProperties props = message.getMessageProperties();
            props.setHeader("X_ORDER_SOURCE", "WEB");
            return message;
        }
    });
}
```

#### Receiving message from RabbitMQ
RabbitMQ queue isn’t very different than from JMS. As with JMS, you have two choices: 

    * Pulling messages from a queue with RabbitTemplate
    * Having messages pushed to a @RabbitListener-annotated method

##### Receiving Messages with Rabbittemplate
RabbitTemplate comes with several methods for pulling messages from a queue which are the mirror images of the `send()` and 
`convertAndSend()` methods described earlier. 

```java
public Order receiveOrder() {
    //No routing or exchange key. 30000 is an optional timeout in milliseconds
    Message message = rabbit.receive("tacocloud.orders", 30000); 
    return message != null ? (Order) converter.fromMessage(message): null;
}
public Order receiveOrder() {
  return (Order) rabbit.receiveAndConvert("tacocloud.order.queue");
}
```

The above timeout can be set via configuration with the `spring.rabbitmq.template.receive-timeout` property. In the above 
receiveAndConvert example, the casting to Order can be replaced with a `ParameterizedTypeReference`: 

```java
public Order receiveOrder() {
  return rabbit.receiveAndConvert("tacocloud.order.queue",new ParameterizedTypeReference<Order>() {});
}
```

##### Handling Rabbitmq messages With Listeners
Spring offers RabbitListener, the RabbitMQ counterpart to JmsListener. To specify that a method should be invoked when a 
message arrives in a RabbitMQ queue, annotate a bean’s method with `@RabbitTemplate`:

```java

@Component
public class OrderListener {
  @RabbitListener(queues = "tacocloud.order.queue")
  public void receiveOrder(Order order) {
    ui.displayOrder(order);
  }
}
```

### Messaging with Kafka
Kafka topics are replicated across all brokers in the cluster. Each node in the cluster acts as a leader for one or more
 topics, being responsible for that topic’s data and replicating it to the other nodes in the cluster.
Each topic can be split into multiple partitions. In that case, each node in the cluster is the leader for one or more 
partitions of a topic, but not for the entire topic.

#### Setting up Spring for Kafka messaging
There is a Spring starter for Kafka:

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

Its presence will trigger Spring Boot autoconfiguration for Kafka that will arrange a `KafkaTemplate` in the Spring 
application context. Take in consideration the following property to set the bootstrap server for kafka(as kafka defaults
 to localhost:9092): `spring.kafka.bootstrap-servers=kafka.tacocloud.com:9092`.
 
#### Sending messages with KafkaTemplate
In many ways, KafkaTemplate is similar to its JMS and RabbitMQ counterparts, although KafkaTemplate is typed with generics
 and is able to deal with domain types directly when sending messages. When sending messages in Kafka, you can specify the 
following parameters to guide how the message is sent:

    * The topic to send the message to (required for send())
    * A partition to write the topic to (optional)
    * A key to send on the record (optional)
    * A timestamp (optional; defaults to System.currentTimeMillis()) 
    * The payload (required) 

For the `send()` method, you can also choose to send a `ProducerRecord`, which is little more than a type that captures all 
of the preceding parameters in a single object. You can also send a `Message` object, but doing so would require you to 
convert your domain objects into a `Message`:

```java
public void sendOrder(Order order) {    
    kafkaTemplate.send("some.topic", order);
}
```

If you set a default topic, you can simplify the sendOrder() method. Set the `spring.kafka.template.default-topic` property 
and use the `sendDefault()` method instead.

#### Writing Kafka listeners
`KafkaTemplate` differs from `JmsTemplate` and `RabbitTemplate` in that it doesn’t offer any methods for receiving messages, 
therefore the only way to read messages is to write a `MessageListener`.

```java
@KafkaListener(topics="orders.topic")
public void handle(Order order) {
    orderRepository.save(order);
}
```

If you need additional metadata from the message, it can also accept a ConsumerRecord or Message object:

```java
@KafkaListener(topics="orders.topic")
public void handle(ConsumerRecord<Order> record) {
  log.info("Received from partition {} with timestamp {}", record.partition(), record.timestamp());
  orderRepository.save(record.value());
}

@KafkaListener(topics="priority.orders.topic")
public void handleMessage(Message<Order> message) {
MessageHeaders headers = message.getHeaders();
  log.info("Received a priority order from partition {} with timestamp {}",
    headers.get(KafkaHeaders.RECEIVED_PARTITION_ID)
    headers.get(KafkaHeaders.RECEIVED_TIMESTAMP));
  orderRepository.save(message.payload());
}
```


## Chapter 9: Integrating Spring<a name="Chapter9"></a>

### Declaring a simple integration flow
Spring Integration enables the creation of integration flows through which an application can receive or send data to some
 resource external to the application itself (like the filesystem). The starter dependencies are:
 
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-integration</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.integration</groupId>
    <artifactId>spring-integration-file</artifactId>
</dependency>
```

The first dependency is the Spring Boot starter for Spring Integration. This dependency is essential to developing a Spring 
Integration flow, regardless of what the flow may integrate with. The second dependency is for Spring Integration’s file 
endpoint module. 
Next you need to create a way for the application to send data into an integration flow so that it can be written to a file,
so you’ll create a gateway like this:

```java
@MessagingGateway(defaultRequestChannel="textInChannel") // This declares a message gateway
public interface FileWriterGateway {
  void writeToFile(@Header(FileHeaders.FILENAME) String filename, String data); // Writes to a file
}
```

`@MessagingGateway` tells Spring Integration to generate an implementation of this interface at runtime. The 
`defaultRequestChannel` attribute of `@MessagingGateway` indicates that any messages resulting from a call to the interface 
methods should be sent to the given message channel. In the above example, the `@Header` annotation indicates that the value 
passed to filename should be placed in a message header.

#### Defining integration flows with XML
Example of xml configuration:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:int="http://www.springframework.org/schema/integration" 
  xmlns:int-file="http://www.springframework.org/schema/integration/file"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/integration
    http://www.springframework.org/schema/integration/spring-integration.xsd
    http://www.springframework.org/schema/integration/file
    http://www.springframework.org/schema/integration/file/spring-integration-file.xsd">

<int:channel id="textInChannel" />
    <int:transformer id="upperCase" input-channel="textInChannel" output-channel="fileWriterChannel" expression="payload.toUpperCase()" />
<int:channel id="fileWriterChannel"/>
<int-file:outbound-channel-adapter id="writer" channel="fileWriterChannel" directory="/tmp/files" mode="APPEND" append-new-line="true"/>
</beans>
```

In this file:

    * You configured a channel named textInChannel. You’ll recognize this as the same channel that’s set as the request 
      channel for FileWriterGateway. When the writeToFile() method is called on FileWriterGateway
    * You configured a transformer that receives messages from textInChannel. It uses a Spring Expression Language (SpEL) 
      expression to call toUpperCase() on the message payload
    * You configured the channel named fileWriterChannel. This channel serves as the conduit that connects the transformer 
      with the outbound channel adapter
    * You configured an outbound channel adapter using the int-file namespace. This XML namespace is provided by 
      Spring Integration’s file module to write files, it receives messages from fileWriterChannel and writes the message 
      payload to a file whose name is specified in the message’s file_name header in the directory specified in the 
      directory attribute. If the file already exists, the file will be appended with a newline rather than overwritten

Bare in mind that for this configuration to take effect, you'll need to import the xml file into your application. For 
example, by usingSpring’s `@ImportResource` annotation.
      
#### Configuring integration flows in Java  
Below is shown the same file-writing integration flow as before, but written in Java:

```java
@Configuration
public class FileWriterIntegrationConfig { 
    @Bean
    @Transformer(inputChannel="textInChannel", outputChannel="fileWriterChannel") //Declares a Transformer
    public GenericTransformer<String, String> upperCaseTransformer() {
      return text -> text.toUpperCase(); //Functional interface implementing GenericTransformer
    }
    @Bean
    @ServiceActivator(inputChannel="fileWriterChannel") //Declares a FileWriter
    public FileWritingMessageHandler fileWriter() {
       FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("/tmp/sia5/files"));
        handler.setExpectReply(false); // indicates that the service activator shouldn’t expect a reply channel 
        handler.setFileExistsMode(FileExistsMode.APPEND);
        handler.setAppendNewLine(true);
        return handler;
    }
}
```

The transformer is a GenericTransformer. Because GenericTransformer is a functional interface, you’re able to provide its 
implementation as a lambda that calls `toUpperCase()` on the message text. `@ServiceActivator` indicates that it’ll accept 
messages from `fileWriterChannel` and hand the messages to the service defined by an instance of `FileWritingMessageHandler`.
`FileWritingMessageHandler` is a message handler that writes a message payload to a file in a specified directory using a 
filename specified in the message’s `file_name` header.

#### Using Spring Integration’s DSL configuration
Instead declaring an individual bean for each component, you’ll declare a single bean that defines the entire flow:

```java

@Bean
public IntegrationFlow fileWriterFlow() {
    return IntegrationFlows
            .from(MessageChannels.direct("textInChannel")) //Inbound channel
            .<String, String>transform(t -> t.toUpperCase()) //Transformer
            .handle(Files.outboundAdapter(new File("/tmp/sia5/files")) //Handles writing to a file
            .fileExistsMode(FileExistsMode.APPEND)
            .appendNewLine(true))
            .get();
    }
}
```

The above is simply a builder that connects the Transformer, channel and handler and the returns a bean of type 
`IntegrationFlow`.

### Surveying the Spring Integration landscape
An integration flow is composed of one or more of the following components:


    * Channels: Pass messages from one element to another
    * Filters: Conditionally allow messages to pass through the flow based on some criteria
    * Transformers: Change message values and/or convert message payloads from one type to another
    * Routers: Direct messages to one of several channels, typically based on message headers
    * Splitters: Split incoming messages into two or more messages, each sent to different channels
    * Aggregators: The opposite of splitters, combining multiple messages from separate channels into a single message
    * Service activators: Hand a message off to some Java method for processing, and then publish the return value on an 
    output channel
    * Channel adapters: Connect a channel to some external system or transport. They can accept input or write to a external 
    system
    * Gateways: Pass data into an integration flow via an interface
    
#### Message channels
Spring Integration provides several channel implementations, including:

    * PublishSubscribeChannel: Messages published into a PublishSubscribeChannel are passed on to one or more consumers. 
    If there are multiple consumers, all of them receive the message
    * QueueChannel: Messages published into a QueueChannel are stored in a queue until pulled by a consumer in a first in, 
    first out (FIFO) fashion. If there are multiple consumers, only one of them receives the message
    * PriorityChannel: Like QueueChannel but, rather than FIFO behavior, mes- sages are pulled by consumers based on the 
    message priority header.
    * RendezvousChannel: Like QueueChannel except that the sender blocks the channel until a consumer receives the message, 
    effectively synchronizing the sender with the consumer
    * DirectChannel: Like PublishSubscribeChannel but sends a message to a sin- gle consumer by invoking the consumer in the 
    same thread as the sender. This allows for transactions to span across the channel
    * ExecutorChannel: Similar to DirectChannel but the message dispatch occurs via a TaskExecutor, taking place in a 
    separate thread from the sender. This channel type doesn’t support transactions that span the channel
    * FluxMessageChannel—A Reactive Streams Publisher message channel based on Project Reactor’s Flux

Input channels are automatically created, with DirectChannel as the default. To declare a different channel, you need to 
declare it as a bean in the context, and reference this channel by name in the integration flow definition. 

```java
@Bean
public MessageChannel orderChannel() {
  return new PublishSubscribeChannel(); // This channel can be referenced as: @ServiceActivator(inputChannel="orderChannel")
}
```

The DSL equivalent is:

```java
@Bean
public IntegrationFlow orderFlow() {
    return IntegrationFlows
        ...
        .channel("orderChannel")
        ...
        .get();
}
```

If you’re using QueueChannel, the consumers must be configured with a poller like in: 
`@ServiceActivator(inputChannel="orderChannel", poller=@Poller(fixedRate="1000"))`

#### Filters
Example of a filter filtering non even numbers from a channel receiving integer numbers:

```java
@Filter(inputChannel="numberChannel", outputChannel="evenNumberChannel")
public boolean evenNumberFilter(Integer number) {
    return number % 2 == 0;
}
``` 

The DSL equivalent is:

```java
@Bean
public IntegrationFlow evenNumberFlow(AtomicInteger integerSource) {
    return IntegrationFlows
        ...
        .<Integer>filter((p) -> p % 2 == 0)
        ...
        .get();
}    
```

#### Transformers
Transformers perform some operation on messages, typically resulting in a different message or even a different payload type.
The following example is a Transformer applied to the previous filter that transform the numbers to roman numbers:

```java
@Bean
@Transformer(inputChannel="evenNumberChannel", outputChannel="romanNumberChannel")
public GenericTransformer<Integer, String> romanNumTransformer() {
    return RomanNumbers::toRoman;
}
``` 

The DSL equivalent is:

```java
@Bean
public IntegrationFlow transformerFlow() {
    return IntegrationFlows
        ...
        .transform(RomanNumbers::toRoman)
        ...
        .get();
}    
```

#### Routers
Based on some routing criteria, allow for branching in an integration flow, directing messages to different channels:

```java
@Bean
@Router(inputChannel="numberChannel")
public AbstractMessageRouter evenOddRouter() {
    return new AbstractMessageRouter() {
        @Override
        protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
          Integer number = (Integer) message.getPayload();
          if (number % 2 == 0) {
            return Collections.singleton(evenChannel());
          }
          return Collections.singleton(oddChannel());
        }
    };
}
@Bean
public MessageChannel evenChannel() {
    return new DirectChannel();
}
@Bean
public MessageChannel oddChannel() {
    return new DirectChannel();
}
```

The same in its DSL equivalent:

```java

@Bean
public IntegrationFlow numberRoutingFlow(AtomicInteger source) {
  return IntegrationFlows
    ...
    .<Integer, String>route(n -> n%2==0 ? "EVEN":"ODD", mapping -> mapping
      .subFlowMapping("EVEN", sf -> sf.<Integer, Integer>transform(n -> n * 10).handle((i,h) -> { ... }))
      .subFlowMapping("ODD", sf -> sf.transform(RomanNumbers::toRoman).handle((i,h) -> { ... }))
    ) .get();
}
```

#### Splitters
Splitters can be useful in the following situations:

    * A message payload contains a collection of items of the same type but you’d like to process as individual message payloads
    * A message payload carries information that can be split into two or more messages of different types
    
Suppose that you want to split a message carrying a purchase order into the billing information and the list of line items: 

```java
public class OrderSplitter {
    public Collection<Object> splitOrderIntoParts(PurchaseOrder po) {
        ArrayList<Object> parts = new ArrayList<>();
        parts.add(po.getBillingInfo());
        parts.add(po.getLineItems());
        return parts;
    }
}

// And then define the splitter in the context
@Bean
@Splitter(inputChannel="poChannel", outputChannel="splitOrderChannel")
public OrderSplitter orderSplitter() { 
    return new OrderSplitter();
}
// Finally, use a router to route each of the objects to the appropriate channel
@Bean
@Router(inputChannel="splitOrderChannel")
public MessageRouter splitOrderRouter() {
  PayloadTypeRouter router = new PayloadTypeRouter();
  router.setChannelMapping(BillingInfo.class.getName(), "billingInfoChannel");
  router.setChannelMapping(List.class.getName(), "lineItemsChannel");
  return router;
}
```

The equivalent DSL is:

```java
return IntegrationFlows
  ...
    .split(orderSplitter())
    .<Object, String> route(p -> {
          if (p.getClass().isAssignableFrom(BillingInfo.class)) {
            return "BILLING_INFO";
          } else {
            return "LINE_ITEMS";
          }
        }, mapping -> mapping
          .subFlowMapping("BILLING_INFO", sf -> sf.<BillingInfo> handle((billingInfo, h) -> {...}))
          .subFlowMapping("LINE_ITEMS", sf -> sf.split().<LineItem> handle((lineItem, h) -> {... }))
).get();

```

#### Service activators
Service activators receive messages from an input channel and send those messages to an implementation of `MessageHandler`.

```java
@Bean
@ServiceActivator(inputChannel="someChannel")
public MessageHandler sysoutHandler() {
  return message -> System.out.println("Message payload:  " + message.getPayload());
}
```

You could also declare a service activator that processes the data in the incoming message before returning a new payload.
In that case, the bean should be a `GenericHandler` rather than a `MessageHandler`:

```java
// When the order arrives, it’s saved via a repository
// the resulting saved Order is returned to be sent to the output channel whose name is completeChannel
@Bean
@ServiceActivator(inputChannel="orderChannel", outputChannel="completeOrder")
public GenericHandler<Order> orderHandler(OrderRepository orderRepo) {
  return (payload, headers) -> return orderRepo.save(payload); 
}
```

The DSL example:

```java
public IntegrationFlow someFlow() {
  return IntegrationFlows
    ...
      .handle(msg -> System.out.println("Message payload:  " + msg.getPayload())).get();
}
```

#### Gateways
Channel adapters represent the entry and exit points of an integration flow. Inbound channel adapters can take many forms,
 depending on the source of the data they introduce into the flow.
 
```java
// The example introduces incrementing numbers from an AtomicInteger into the flow
@Bean
@InboundChannelAdapter(poller=@Poller(fixedRate="1000"), channel="numberChannel")
public MessageSource<Integer> numberSource(AtomicInteger source) {
    return () -> {
        return new GenericMessage<>(source.getAndIncrement());
    };
}
```

The DSL example:

```java
@Bean
public IntegrationFlow someFlow(AtomicInteger integerSource) {
    return IntegrationFlows.from(integerSource, "getAndIncrement", c -> c.poller(Pollers.fixedRate(1000)))....get();
}
```

Another example of input channel but of type `FileReadingMessageSource` which monitors a specified directory and submits any 
files that are written to that directory as messages to a channel:

```java
@Bean 
@InboundChannelAdapter(channel="file-channel",poller=@Poller(fixedDelay="1000")) 
public MessageSource<File> fileReadingMessageSource() {
    FileReadingMessageSource sourceReader = new FileReadingMessageSource();
    sourceReader.setDirectory(new File(INPUT_DIR));
    sourceReader.setFilter(new SimplePatternFileListFilter(FILE_PATTERN));
    return sourceReader;
}
``` 

or with DSL:
```java
@Bean
public IntegrationFlow fileReaderFlow() {
  return IntegrationFlows.from(Files.inboundAdapter(new File(INPUT_DIR)).patternFilter(FILE_PATTERN)).get();
}
```

As with outbound channels, Service activators, implemented as message handlers often acts as one.

#### Endpoint modules
Spring Integration provides over two dozen endpoint modules containing channel adapters, each of the endpoint modules offers 
channel adapters that can be either declared as beans when using Java configuration or referenced via static methods when 
using Java DSL configuration. 


## Chapter 10: Introducing Reactor<a name="Chapter10"></a>

### Understanding reactive programming
In imperative programming, you write code as a list of instructions to be followed, one at a time, in the order that 
they’re encountered. A task is performed and the program waits for it to complete before moving on to the next task. While a 
task is being performed, the thread that invoked that task is blocked, unable to do anything else until the task completes.
Reactive programming is functional and declarative in nature. Rather than describe a set of steps that are to be performed
 sequentially, reactive programming involves describing a pipeline or stream through which data flows. 
 
#### Defining Reactive Streams
Reactive Streams aims to provide a standard for asynchronous stream processing with non-blocking backpressure. The Reactive 
Streams specification can be summed up by four interface definitions.
`Publisher`, produces data:

```java
public interface Publisher<T> {
  void subscribe(Subscriber<? super T> subscriber);
}
```

`Subscriber`, which consumes data that receives from the producer:

```java
public interface Subscriber<T> {
  void onSubscribe(Subscription sub);
  void onNext(T item);
  void onError(Throwable ex);
  void onComplete();
}
```

`Subscription`, it is a channel that connects producers and subscribers. When the Publisher calls `onSubscribe()`, it passes 
a `Subscription` object to the `Subscriber`:

```java
public interface Subscription {
  void request(long n); //n is the number of data object that the subscriber is willing to accept (backpressure)
  void cancel();
}
```

After the `Publisher` has sent as many items as were requested, the `Subscriber` can call `request()` again to request more.
For every item that’s published by the Publisher, the onNext() method will be called to deliver the data to the Subscriber. 
If the Publisher has no more data to send and isn’t going to produce anymore data, it will call onComplete() to tell the 
Subscriber that it’s out of business.
And `Processor`, which receives data and process it in some way, it is like a combination of `Subscriber` and `Publisher`:

```java
 public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {}
```

As a `Subscriber`, a `Processor` will receive data and process it in some way. Then it will switch hats and act as a 
`Publisher` to publish the results to its `Subscribers`.

### Getting started with Reactor
#### Diagramming reactive flows
Reactive flows are often illustrated with marble diagrams, which depicts a timeline of data as it flows through a Flux (a 
Publisher representing a pipeline of zero, one, or many -potentially infinite- data items), or Mono (a Publisher 
representing a pipeline of no more than one data items) at the top, an operation in the middle, and the timeline of the 
resulting Flux or Mono at the bottom.

#### Adding Reactor dependencies
To get started with Reactor, add the following dependency to the project build:

```xml
<dependency>
  <groupId>io.projectreactor</groupId>
  <artifactId>reactor-core</artifactId>
</dependency>
```

To add Reactor's test dependency to your build:

```xml
<dependency>
  <groupId>io.projectreactor</groupId>
  <artifactId>reactor-test</artifactId>
   <scope>test</scope>
  </dependency>
```

If you want to use Reactor in a non-Spring Boot project, you’ll need to set up Reactor’s BOM (bill of materials) in the 
build:

```xml
<dependencyManagement>
  <dependencies>
      <dependency>
          <groupId>io.projectreactor</groupId>
          <artifactId>reactor-bom</artifactId>
          <version>Bismuth-RELEASE</version>
          <type>pom</type>
          <scope>import</scope>
      </dependency>
  </dependencies>
</dependencyManagement>
```

### Applying common reactive operations
Between Flux and Mono, there are over 500 operations, each of which can be loosely categorized as creation, combination, 
transformation and logic operations.

#### Creating reactive types
Reactor provides several operations for creating Fluxes and Monos:

##### Creating from objects
If you have one or more objects that you’d like to create a Flux or Mono from, you can use the static just() method on 
Flux or Mono to create a reactive type whose data is driven by those objects: `Flux.just("Apple", "Orange", "Grape");`
At this point, the Flux has been created, but it has no subscribers. To add a subscriber, you can call the subscribe() 
method on the Flux: `fruitFlux.subscribe(f -> System.out.println("Here's some fruit: " + f));` Upon calling subscribe(), 
the data starts flowing. 
Given a Flux or Mono, `StepVerifier` subscribes to the reactive type and then applies assertions against the data as it flows
 through the stream, finally verifying that the stream completes as expected: 
`StepVerifier.create(fruitFlux).expectNext("Apple").expectNext("Orange").expectNext("Grape").verifyComplete();`

##### Creating from collections
A Flux can also be created from an array, Iterable, or Java Stream: `Flux.fromArray(theArray);`. If you need to create a Flux
 from a `java.util.List`, `java.util.Set`, or any other implementation of `java.lang.Iterable`, you can pass it into the 
static `fromIterable()` method. For a `java.util.Stream` use the `fromStream()` method.

##### Generating flux data
Sometimes you don’t have any data to work with and just need Flux to act as a counter, emitting a number that increments with
each new value. To create a counter Flux, you can use the static `range(int init, int end)` method. To create a flux that
emits data in regular intervals of time use the static `interval(Duration theDuration)` method, the value emitted by an 
interval Flux starts with 0 and increments on each successive item. Because `interval()` isn’t given a maximum value, it 
will potentially run forever, use the `take()` operation to limit the results.

#### Combining reactive types
##### Merging reactive types
If you have two Flux streams and need to create a single resulting Flux that will produce data as it becomes available from 
either of the upstream Flux streams, use the `flux1.mergeWith(flux2)`. A Flux will publish data as quickly as it possibly 
can, but you use a `delayElements(theDuration)` to slow down the publication of data. You can apply a `delaySubscription(d)` 
operation to a Flux so that it won’t emit any data until the duration d has passed following a subscription. `mergeWith()`
 can’t guarantee a perfect back and forth between its sources, use the static method `zip()` operation instead for this, 
 which produces  a tuple of items, where the tuple contains one item from each source Flux.

##### Selecting the first reactive type to publish
Suppose you have two Flux objects, and rather than merge them together, you merely want to create a new Flux that emits 
the values from the first Flux that produces a value. `Flux.first(flux1, flux2)` operation picks the first of two Flux 
objects and echoes the values it publishes.

#### Transforming and filtering reactive streams
##### Filtering data from reactive types
To disregard the first n entries, use the `skip(n)` operation which produces a new flux that skips over a specified number of
 items before emitting the remaining items from the source Flux. To skip based on duration instead of number of elements, 
 use the skip method and pass it a `Duration` object. `take` which is the inverse of skip, is also overloaded.
Given a Predicate that decides whether an item will pass through the Flux or not, the `filter()` operation lets you 
 selectively publish based on whatever criteria you want.
`distinct()` method results in a Flux that only publishes items from the source Flux that haven’t already been published.  

##### Mapping reactive data
Reactor’s types offer `map()` and `flatMap()` operations to transform published items to some other form or type. The mapping 
is performed synchronously, as each item is published by the source Flux. If you want to perform the mapping asynchronously, 
you should consider the `flatMap()` operation. The `flatMap()` maps each object to a new Mono or Flux. The results of the 
Mono or Flux are flattened into a new resulting Flux:

```java
 Flux<Player> playerFlux = Flux
    .just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
    .flatMap(n -> Mono.just(n)
        .map(p -> {
            String[] split = p.split("\\s");
            return new Player(split[0], split[1]);
          })
        .subscribeOn(Schedulers.parallel()) // indicates that each subscription should take place in a parallel thread
);
```

`Schedulers` supports several concurrency models:

    * immediate(): Executes the subscription in the current thread
    * single(): Executes the subscription in a single, reusable thread and reuses the same thread for all callers
    * newSingle(): Executes the subscription in a per-call dedicated thread
    * elastic(): Executes the subscription in a worker pulled from an unbounded, elastic pool. New worker threads are 
      created as needed, and idle workers are disposed of (by default, after 60 seconds)
    * parallel(): Executes the subscription in a worker pulled from a fixed-size pool, sized to the number of CPU cores
      
##### Buffering data on a reactive stream
The `buffer()` operation breaks the stream of data from the flux into bite-size chunks (returns a flux which elements are 
listx of size equal to the number passed to the buffer operation). When you combine `buffer()` with `flatMap()`, it enables 
each of the List collections to be processed in parallel. To collect everything that a Flux emits into a List, you can 
call `buffer()` with no arguments. The `log()` operation logs all Reactive Streams events (or use the `collectList()` 
operation which produces a Mono that publishes a List).
`collectMap()` operation results in a Mono that publishes a Map that’s populated with entries whose key is calculated by a 
given Function (if the function produces duplicates, the last entry flowing through the stream overrides earlier entries).

#### Performing logic operations on reactive types
The `all()` and `any()` operations are used to know if the entries published by a Mono or Flux match some criteria. These 
two operations results in a Mono of type Boolean.


## Chapter 11: Developing reactive APIs<a name="Chapter11"></a>
### Working with Spring WebFlux
Typical Servlet-based web frameworks, such as Spring MVC, are blocking and multi- threaded in nature, using a single 
thread per connection. Asynchronous web frameworks, in contrast, achieve higher scalability with fewer threads—generally 
one per CPU core. By applying a technique known as event looping, these frameworks are able to handle many requests per 
thread. In an event loop, everything is handled as an event, including requests and callbacks from intensive operations 
like database and network operations. When a costly operation is needed, the event loop registers a callback for that 
operation to be performed in parallel, while it moves on to handle other events.

#### Introducing Spring WebFlux
Opposite to the Spring MVC, Spring WebFlux doesn’t have ties to the Servlet API, so it builds on top of a Reactive HTTP 
API, which is a reactive approximation of the same functionality provided by the Servlet API. Spring WebFlux doesn't 
require a servlet container to run on, instead, it can run on any non-blocking web container (Netty, Undertow, Tomcat, Jetty,
 or any Servlet 3.1 or higher container). To add Spring WebFlux to yout project, you’ll need to add the Spring Boot WebFlux 
 starter dependency instead of the standard web starter (for example, spring-boot-starter-web):
 
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

Spring WebFlux controller methods usually accept and return reactive types, like Mono and Flux, instead of domain types and
 collections (although Spring MVC controller methods can also return a Mono or Flux).
 
#### Writing reactive controllers
A way to create a reactive controller in Spring, is to return a flux type like in:

```java
return Flux.fromIterable(tacoRepo.findAll()).take(12);
```  

You can also rewrite your controller to look something like this:

```java
@GetMapping("/recent")
public Flux<Taco> recentTacos() {
    return tacoRepo.findAll().take(12); //This needs to have a reactive repository in place that returns a Flux<Taco>
}
```

The above repository can be written by extending `ReactiveCrudRepository<T, K>`. Both MVC and WebFlux controllers have 
_@RestController_, _@RequestMapping_ and _@GetMapping_ annotations, therefore is a matter of changing the return type. The 
framework will call _subscribe()_ for you, which means that when a request for _\/design\/_ recent is handled, the 
_recentTacos()_ method will be called and will return before the data is even fetched from the database. Remember that a 
call to return a single element can be wrapped to return a _Mono<T>_ to make it reactive.

##### Working With RxJava Types
With Spring WebFlux, you can also choose to work with RxJava types like _Observable_ and _Single_. Spring WebFlux 
controller methods can also return RxJava’s _Completable_, which is equivalent to a _Mono<Void>_ in Reactor. WebFlux can also
return a _Flowable_ as an alternative to _Observable_ or Reactor’s Flux.
 
##### Handling Input Reactively
With Spring WebFlux, you can also accept a Mono or a Flux as input to a handler method, making it a fully non-blocking, 
request-handling method:

```java
@PostMapping(consumes="application/json")
@ResponseStatus(HttpStatus.CREATED)
public Mono<Taco> postTaco(@RequestBody Mono<Taco> tacoMono) {
    return tacoRepo.saveAll(tacoMono).next();
}
```

By accepting a _Mono<Taco>_ as input, the method is invoked immediately without waiting for the Taco to be resolved from the
 request body. Because the repository is also reactive, it’ll accept a _Mono_ and immediately return a _Flux<Taco>_, from 
 which you call _next()_ and return the resulting _Mono<Taco>_ all before the request is even processed.
 
### Defining functional request handlers
As an alternative to WebFlux, Spring 5 has introduced a new functional programming model for defining reactive APIs. 
Writing an API using Spring’s functional programming model involves four primary types:

    * RequestPredicate: Declares the kind(s) of requests that will be handled
    * RouterFunction: Declares how a matching request should be routed to handler code
    * ServerRequest: Represents an HTTP request, including access to header and body information
    * ServerResponse: Represents an HTTP response, including header and body information

An example of how to use the above types is shown below:

```java

import static org.springframework.web. reactive.function.server.RequestPredicates.GET;
import static org.springframework.web. reactive.function.server.RouterFunctions.route;
import static org.springframework.web. reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

@Configuration
public class RouterFunctionConfig {
    @Bean
    public RouterFunction<?> helloRouterFunction() {
        return route(GET("/hello"), request -> ok().body(just("Hello World!"), String.class));
    }
}
```

The _route()_ method from RouterFunctions accepts two parameters: a Request- Predicate and a function to handle matching 
requests. The _GET()_ method from _RequestPredicates_ declares a _RequestPredicate_ that matches HTTP GET requests for the 
/hello path. If you need to handle a different kind of request, you only need to call _andRoute()_ to declare another 
_RequestPredicate-to-function_ mapping. For example, here’s how you might add another handler for GET requests for /bye:

```java
return route(GET("/hello"), request -> ok().body(just("Hello World!"), String.class))
            .andRoute(GET("/bye"),request -> ok().body(just("See ya!"), String.class));
```

The following configuration class is a functional analog to a Spring MVC configuration class:

```java
@Configuration
public class RouterFunctionConfig {
    @Autowired
    private TacoRepository tacoRepo;
    @Bean
    public RouterFunction<?> routerFunction() {
        return route(GET("/design/taco"), this::recents).andRoute(POST("/design"), this::postTaco);
    }
    public Mono<ServerResponse> recents(ServerRequest request) { 
        return ServerResponse.ok().body(tacoRepo.findAll().take(12), Taco.class);
    }
    public Mono<ServerResponse> postTaco(ServerRequest request) {
        Mono<Taco> taco = request.bodyToMono(Taco.class);
        Mono<Taco> savedTaco = tacoRepo.save(taco);
        return ServerResponse.created(URI.create( "http://localhost:8080/design/taco/" + savedTaco.getId()))
            .body(savedTaco, Taco.class);
    }
}
```

_RouterFunction_ is created to handle GET requests for /design/taco and POST requests for /design.

### Testing reactive controllers
Spring 5 has introduced WebTestClient, a new test utility that makes it easy to write tests for reactive controllers 
written with Spring WebFlux.

#### Testing GET Requests
To test a controller call `WebTestClient.bindToController(new ControllerToBeTested)` to create an instance of 
_WebTestClient_. Calling `get().uri("/aUrl")` over your _WebTestClient_, describes the request you want to issue, calling 
to `exchange()` submits the request, and finally, by calling `expectStatus()`you can affirm that the response is as 
expected. For those cases where it may be clumsy to use `jsonPath()`, _WebTestClient_ offers `json()`, which accepts a 
String parameter containing the JSON to compare the response against. The `expectBodyList()` method accepts either a 
Class or a _ParameterizedTypeReference_ indicating the type of elements in the list and returns a ListBodySpec object to 
make assertions against.

#### Testing POST requests
With WebTestClient you can test any kind of HTTP method, including GET, POST, PUT, PATCH, DELETE, and HEAD requests.

#### Testing with a live server
Sometimes you may need to test a WebFlux controller in the context of a server like Netty or Tomcat and maybe with a 
repository or other dependencies. To write a WebTestClient integration test, you start by annotating the test class with 
_@RunWith_ and _@SpringBootTest_ like any other Spring Boot integration test: 

```java
@RunWith(SpringRunner.class) 
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) 

public class DesignTacoControllerWebTest {
    @Autowired
    private WebTestClient testClient;
}
```

### Consuming REST APIs reactively
_RestTemplate_ is an old-timer, having been introduced in Spring version 3.0, but all of the methods provided by 
_RestTemplate_ deal in non-reactive domain types and collections. Spring 5 offers _WebClient_ as a reactive alternative to 
_RestTemplate_.  The general usage pattern for working with WebClient is:

    * Create an instance of WebClient (or inject a WebClient bean) 
    * Specify the HTTP method of the request to send
    * Specify the URI and any headers that should be in the request 
    * Submit the request
    * Consume the response
    
####  GETting resources
Suppose that you need to fetch an Ingredient object by its ID from the application, with WebClient, you build the 
request, retrieve a response, and then extract a Mono that publishes the Ingredient object:

```java
Mono<Ingredient> ingredient = WebClient
    .create()
    .get()
    .uri("http://localhost:8080/ingredients/{id}", ingredientId)
    .retrieve()
    .bodyToMono(Ingredient.class); // use .bodyToFlux(TheClass.class) to retrieve a Flux<TheClass>

ingredient.subscribe(i -> { ... })
```
Here you create a new _WebClient_ instance with `create()` and use `get()` and `uri()` to define a GET request. The 
`retrieve()` method executes the request. A call to `bodyToMono()` extracts the response’s body payload into a 
_Mono<Ingredient>_ on which you can continue applying addition Mono operations.

##### Making requests with a base URI
You may find yourself using a common base URI for many different requests. In that case, it can be useful to create a 
WebClient bean with a base URI and inject it anywhere it’s needed like in `WebClient.create("http://localhost:8080")`, 
subsequent calls can be made using relative uris.

##### Timing out on long-running requests
To avoid having your client requests held up by a sluggish network or service, you can use the `timeout()` method from Flux 
or Mono to put a limit on how long you’ll wait for data to be published. Call This method before the `subscribe()` call.

#### Sending resources
To send a Flux or Mono, pass them on the `.body(<Flux or Mono>, TypeClass.class)` method of the _WebClient_. Alternatively
 if instead you have the raw domain object on hand, you can use `.syncBody(instanceOfTheClass)`. PUT requests typically have 
 empty response payloads, so you must ask bodyToMono() to return a Mono of type Void. On subscribing to that Mono, the 
 request will be sent.
 
#### Deleting resources
WebClient also allows the removal of resources by way of its `delete()` method:
`Mono<Void> result = webClient.delete().uri("/some/{id}", theId) .retrieve().bodyToMono(Void.class).subscribe();`

#### Handling errors
If you need to handle errors, then a call to `onStatus()` can be used to specify how various HTTP status codes should be 
dealt with. It accepts two functions: a predicate function, which is used to match the HTTP status, and a function that given
a _ClientResponse_ object, returns a _Mono<Throwable>_. When subscribing to a Mono or Flux that might end in error, it’s 
important to register an error consumer as well as a data consumer in the call to `subscribe()`:
`ingredientMono.subscribe(ingredient -> {/*handle the ingredient data ...}*/, error-> {/* deal with the error*/});`
The second lambda (the error consumer) is given a _WebClientResponseException_, which is not very specific, by adding a 
custom error handler, you can provide code that translates a status code to a Throwable of your own choosing:

```java
Mono<Ingredient> ingredientMono = webClient .get()
    .uri("http://localhost:8080/ingredients/{id}", ingredientId) .retrieve()
    .onStatus(HttpStatus::is4xxClientError,response -> Mono.just(new UnknownIngredientException()))
    .bodyToMono(Ingredient.class);
``` 

#### Exchanging requests
You’ve used the `retrieve()` method to signify sending a request when working with _WebClient_. This method returned an 
object of type _ResponseSpec_, which is fine for simple cases, but it’s limited in a few ways (you can't access header or 
cookie values). The `exchange()` method instead, returns a Mono of type _ClientResponse_ to which you can apply reactive 
operations.

### Securing reactive web APIs
Spring Security web security model has been built around servlet filters, but when writing a web application with Spring 
WebFlux, there’s no guarantee that servlets are even involved. Starting with version 5.0.0, Spring Security uses Spring’s 
WebFilter, a Spring-specific analog to servlet filters that doesn’t demand dependence on the servlet API. Spring Security 
comes as the same Spring Boot security starter:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

#### Configuring reactive web security
Configuring Spring Security to secure a Spring MVC web application typically involves creating a new configuration class 
that extends _WebSecurityConfigurerAdapter_ and is annotated with _@EnableWebSecurity_. The same configuration for a reactive 
Spring WebFlux application would like like this:

```java
@Configuration
@EnableWebFluxSecurity //Instead of @EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) { //Replaces the configure() method
        return http
            .authorizeExchange() //Similar to authorizeRequests()
            .pathMatchers("/design", "/orders") //Similar to antMatchers()
            .hasAuthority("USER")
            .anyExchange()
            .permitAll()
            .and()
            .build();
    }
}
``` 

#### Configuring a reactive user details service
In a reactive security configuration, you declare a _ReactiveUserDetailsService_ bean, which is the equivalent to the 
_UserDetailsService_. _ReactiveUserDetailsService_ requires implementation of `findByUsername()` method, which returns a 
_Mono<userDetails>_ instead of a raw _UserDetails_ object:

```java
@Service
public ReactiveUserDetailsService userDetailsService(UserRepository userRepo) {
    return new ReactiveUserDetailsService() {
        @Override
        public Mono<UserDetails> findByUsername(String username) {
            return userRepo.findByUsername(username).map(user -> {return user.toUserDetails();});
        }
    };
}
```


## Chapter 12: Persisting data reactively<a name="Chapter12"></a>
### Understanding Spring Data’s reactive story
For now, there’s no support for working with relational databases reactively because that would require that the databases 
and JDBC drivers involved also support non-blocking reactive models.

#### Spring Data reactive distilled
Spring Data’s reactive can be summed up by saying that reactive repositories have methods that accept and return Mono and 
Flux instead of domain entities and collections, like `Flux<Ingredient> findByType(Ingredient.Type type);`. Analogous, a 
save method would accept a Publisher like in: `<Taco> Flux<Taco> saveAll(Publisher<Taco> tacoPublisher);`.

#### Converting between reactive and non-reactive types
Although the full benefit of reactive programming comes when you have a reactive model from end to end, including at the 
database level, there’s still some benefit to be had by using reactive flows on top of a non-reactive database. For 
example, you can convert the non-reactive collection into a Flux as soon as you receive it, so that you can deal with the 
results reactively from there on `Flux.fromIterable(repo.findByUser(someUser));` (or `Mono.just(repo.findById(id));`).
Likewise, from reactive to non reactive, you can call _toIterable()_ on Flux, and _block()_ on Mono.

#### Developing reactive repositories
Built on top of their non-reactive repository support, Spring Data Cassandra and Spring Data MongoDB both support a 
reactive model.

### Working with reactive Cassandra repositories
Cassandra is a distributed, high-performance, always available, eventually consistent, partitioned-row-store, NoSQL database.

#### Enabling Spring Data Cassandra
If you aren’t planning to write reactive repositories for Cassandra, you can use the following dependency in your build:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-cassandra</artifactId>
</dependency>
```

The starter dependency that enables reactive Cassandra repositories is:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-cassandra-reactive </artifactId>
</dependency>
```

At this point, you’ll probably want to remove the Spring Data JPA starter dependency or any relational database dependencies.
When Cassandra libraries are in the runtime classpath, autoconfiguration for creating reactive Cassandra libraries is 
triggered. But you’ll need to provide a small amount of configuration like the name of a key space (a grouping of tables 
in a Cassandra node) within which your repositories will operate. Using the Cassandra CQL (Cassandra Query Language) 
shell, you can create a key space like this: 
`create keyspace tacocloud with replication={'class':'SimpleStrategy', 'replication_factor':1} and durable_writes=true;`
Use SimpleStrategy for single node clusters, and NetworkTopologyStrategy for multi-node clusters. With a created key space, 
you need to configure the `spring.data.cassandra.keyspace-name` property to tell Spring Data Cassandra to use that key space:

```yaml
spring:
  data:
    cassandrakeyspace-name: tacocloud 
    schema-action: recreate-drop-unused #tables and user-defined types will be dropped and recreated on application start
```

By default, cassandra assumes the server runs in localhost:9092, use `spring.data.cassandra.contact-points` (contact point
 is the host where a Cassandra node is running) and `spring.data.cassandra.port` to define this settings. To define 
 username and password use `spring.data.cassandra.username` and `spring.data.cassandra.password` properties.
 
#### Understanding Cassandra data modeling
Cassandra data modeling is different from how you might model your data for persistence in a relational database:

    * Cassandra tables may have any number of columns, but not all rows will necessarily use all of those columns
    * Cassandra databases are split across multiple partitions. Any row in a given table may be managed by one or more 
    partitions, but it’s unlikely that all partitions will have all rows
    * A Cassandra table has two kinds of keys: partition keys and clustering keys. Hash operations are performed on each 
    row’s partition key to determine which partition(s) that row will be managed by. Clustering keys determine the order 
    in which the rows are maintained within a partition
    * Cassandra is highly optimized for read operations. As such, it’s common and desirable for tables to be highly 
    denormalized and for data to be duplicated across multiple tables

#### Mapping domain types for Cassandra persistence
Spring Data Cassandra provides its own set of mapping annotations for a similar purpose. Although it’s not required, 
properties that hold a generated ID value are commonly of type UUID. _@PrimaryKeyColumn_ with a type of 
_PrimaryKeyType.PARTITIONED_ specifies that the id property serves as the partition key, if we use instead the 
_PrimaryKeyType.CLUSTERED_ is used to determine the ordering of rows within a partition.
Cassandra tables are highly denormalized and may contain data that’s duplicated from other tables. Columns that contain 
collections of data, must be collections of native types (integers, strings, and so on) or must be collections of 
user-defined types. User-defined types enable you to declare table columns that are richer than simple native types. Often
they’re used as a denormalized analog for relational foreign keys, they are marked with the _@UserDefinedType_ annotation.
You can’t use the a class as a user-defined type if the _@Table_ annotation has already mapped it as an entity for 
persistence in Cassandra. You must create a new class to define how the dependant entity will be stored in the column 
mapping the collection in the entity that owns the relation. 

#### Writing reactive Cassandra repositories
When it comes to writing reactive Cassandra repositories, you have the choice of two base interfaces: 
_ReactiveCassandraRepository_ and _ReactiveCrudRepository_.  _ReactiveCassandraRepository_ extends _ReactiveCrudRepository_ 
to offer a few variations of an insert() method, if you’ll be inserting a lot of data, you might choose 
_ReactiveCassandraRepository_ otherwise, it’s better to stick with _ReactiveCrudRepository_. If you don't want to have 
reactive repositories, simply extend _CrudRepository_ or _CassandraRepository_ interfaces.
In Cassandra, filtering results with a where clause could potentially slow down an otherwise fast query, the 
_@AllowFiltering_ annotation makes it possible to filter the results, acting as an opt-in for those cases where it’s needed. 
This alerts Cassandra that you’re aware of the potential impacts to the query’s performance and that you need it anyway. 

### Writing reactive MongoDB repositories
MongoDB stores documents in BSON (Binary JSON) format, which can be queried for and retrieved in a way that’s similar to 
how you might query for data in any other database.

#### Enabling Spring Data MongoDB
The Spring starter dependency for non-reactive MongoDB is the following:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

The dependency for reactive Spring Data MongoDB starter dependency is:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
</dependency>
```

Spring Data MongoDB assumes that you have a MongoDB server run- ning locally and listening on port 27017. To work with an 
embedded MongoDB you need to add the following dependency:

```xml
<dependency>
    <groupId>de.flapdoodle.embed</groupId>
    <artifactId>de.flapdoodle.embed.mongo</artifactId>
</dependency>
```

With this configuration, all data will be wiped clean when you restart the application. Some interesting properties to 
configure MongoDB in Spring:

    * spring.data.mongodb.host: The hostname where Mongo is running (default: localhost)
    * spring.data.mongodb.port: The port that the Mongo server is listening on (default: 27017)
    * spring.data.mongodb.username: The username to use to access a secured Mongo database
    * spring.data.mongodb.password: The password to use to access a secured Mongo database
    * spring.data.mongodb.database: The database name (default: test)
    
#### Mapping domain types to documents
Spring Data MongoDB offers a handful of annotations, the most important for common use cases are:

    * @Id (required): Designates a property as the document ID (from Spring Data Commons)
    * @Document (required): Declares a domain type as a document to be persisted to MongoDB
    * @Field: Specifies the field name (and optionally the order) for storing a property in the persisted document. 
    Properties that aren’t annotated with @Field will assume a field name equal to the property name

By default, the collection name (the Mongo analog to a relational database table) is based on the class name, with the 
first letter lowercased, but you can change that by setting the collection attribute of _@Document_. If you choose to use 
a String property as the ID, you get the benefit of Mongo automatically assigning a value to it when it’s saved.

#### Writing reactive MongoDB repository interfaces
When it comes to writing reactive repositories for MongoDB, you have a choice between _ReactiveCrudRepository_ and 
_ReactiveMongoRepository_,  use _CrudRepository_ or _MongoRepository_ for non reactive repositories. The key difference is 
that _ReactiveMongoRepository_ provides a handful of special `insert()` methods optimized for persisting new documents.
One of the benefits of extending _ReactiveCrudRepository_ it’s more portable across various database types and works equally
 well for MongoDB as for Cassandra. _ReactiveMongoRepository_ it’s specific to MongoDB and not portable to other databases.
 

## Chapter 13: Discovering services<a name="Chapter13"></a>