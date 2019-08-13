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