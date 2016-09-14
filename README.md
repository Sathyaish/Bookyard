# Bookyard

A client/server application written in the Kotlin programmming language. This application was written to demonstrate how to write an application using the Kotlin programming language and to secure it using Json Web Tokens (JWT's).

The client is a desktop application made using Swing/AWT (java.swing._*_, java.awt._*_).

The server is an HTTP Servlet application. The endpoints on the server return Json responses.

## Workflow
The client application provides a login dialog box. The user logs in with the user name **Sathyaish** and password **FooBar**.

> Since this application was written to illustrate the use of the Kotlin programming language only, its features have been kept to a bare minimum. There is no way to create a new user.

The client application makes a secured Web request, i.e. an HTTPS POST request to the server at the end-point **/login**. The client packages the user name and password in a Json Web Token (JWT) that is signed with the client's *application secret*.

In response to a login request, the server send back a Json string with the following object structure (pseudo-code):

```
OperationResult<User> where:

class User
  public int id;
  public string userName;
  
class OperationResult<T>
  public bool successful;
  public String? errorMessage;
  public T? data;
```

To be contd...
