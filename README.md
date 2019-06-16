# Spring In Action

## Table of contents:
1. [Chapter 1: Getting started with Spring](#Chapter1)
2. [Chapter 2: Developing web applications](#Chapter2)
3. [Chapter 3: Working with data](#Chapter3)
4. [Chapter 4: Securing Spring](#Chapter4)
5. [Chapter 5: Working with configuration properties](#Chapter5)
6. [Chapter 6: Creating REST services](#Chapter6)


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
