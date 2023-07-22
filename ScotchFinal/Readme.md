# ScotchApp

Dating app. It has an interface that is taken from various libraries. The application is written in Java. An API is written for the application, which allows it to interact with the server. The api was written by the user kek-flip in Go. The direct development of the application was carried out with the user DragIv.

The application has an ergonomic interface and connection with the server for storing, modifying and retrieving data in the PostgreSQL DBMS. Development was carried out in Android Studio

<H3> Key technologies used: </H3>
Android SDK  <br/>
Gradle  <br/>
Using OOP  <br/>
Using Third Party Design Libraries  <br/>
HttpUrlConnection  <br/>
XML  <br/>
Git  <br/>
Cookie  <br/>
Exception Handling  <br/>
Multithreading is implemented in the Registration window as a demonstration of skills, but not implemented in other windows due to API features  <br/>


<H3> The structure of the application is as follows: </H3>
There are two main windows: for login and for registration. After a correct login, 3 fragments appear: profile, search and favorites. <br/>
From the profile fragment, you can go to the settings or go to the login window. <br/>
In the search snippet, you can rate other users, view their information, and search by filters. <br/>
In the favorites fragment, there is an opportunity to see those whom I liked and those who liked me. <br/>

<H3>Sign In</H3>
Picture: https://github.com/Glevelll/Android-development/assets/113721736/f8568771-60f5-43bf-8c64-694c5e97be80 <br/>

The login window contains a transition to the registration window and fields for entering a login and password. The entered login and password are sent to the server using a POST request. If successful, the server returns a cookie for the user, which will be necessary for further interaction of the authorized user with the application. If the data entry is incorrect, the user is informed about it.

<H3>Sign Up</H3>
Pictures: https://github.com/Glevelll/Android-development/assets/113721736/131f5363-540b-4e85-b80f-b2391fcb90cb <br/>
https://github.com/Glevelll/Android-development/assets/113721736/146fa7b6-d5c1-417c-81dc-a86fc7e94415 <br/>

In the registration window, the user can enter information about himself: login, password, name, city, gender, age, phone number, information about himself and insert a photo (jpeg). If all the data is filled in correctly, then they are sent to the server for storage. Later, using this login and password, the user can log into the application. In case of entering incorrect data, the user is informed about it.

<H3>Profile</H3>
Picture: https://github.com/Glevelll/Android-development/assets/113721736/223e9018-c759-4630-8df7-84b428d39dac <br/>

After a successful login, the user is presented with a window with three fragments. First fragment: Profile. It contains all the information about the current user, which can be changed by clicking on the Settings button, after which the settings window will open. From here, you can exit to the login window using the Exit button. Also, here are the number of received and sent likes. The data here is obtained from the server using the get method using two requests: one for getting information, the other for getting a photo. This approach is implemented throughout the application.

<H3>Settings</H3> <br/>
Pictures: https://github.com/Glevelll/Android-development/assets/113721736/9f4cb582-d8ff-485c-a9a7-3e74924be041 <br/>
https://github.com/Glevelll/Android-development/assets/113721736/9fd8489e-76f6-4d0c-80b6-3676dfaf21f3 <br/>

In the settings window, you can change information about the current user: name, gender, city, age, about yourself and photo (jpeg). If the user has changed the data and wants to go back, the user is informed that the data has already been changed and needs to be saved. If the user changed the data and clicked OK, then a Patch Path request is sent to the server, which changes the data on the server. After that, the user will have new information in the profile window.

<H3>Search</H3>
Picture: https://github.com/Glevelll/Android-development/assets/113721736/785652b1-f192-4392-b084-2fde0b5461d2 <br/>

In the Search fragment, you can see cards with photos and information about other users registered in the application. Users can be rated using the like and dislike buttons, swipe them left to dislike and right to like. There is also a button to show full information about the user. At the top there is a filter button that allows you to search for users by the entered parameters.

<H3>Filter</H3>
Pictire: https://github.com/Glevelll/Android-development/assets/113721736/16642fd8-ed19-4d2c-86ff-3f7abcd5aae3 <br/>

The filter pops up from the bottom like a bottom navigation. It closes with a swipe down. It has options to search by gender, city, or age. By entering any of the parameters and clicking on the Apply button, after swiping down, you can see users corresponding to the specified parameters. Also, the parameters can be cleared, and if they are set incorrectly, the application notifies you of this.

<H3>Full info</H3>
Picture: https://github.com/Glevelll/Android-development/assets/113721736/da1872cb-f08d-465d-89c7-2fbfa909bd61 <br/>

In the window showing the full information, there is just a photo of the user, full information and a button to return to the previous fragment.

<H3>Favourite</H3>
Pictures: https://github.com/Glevelll/Android-development/assets/113721736/55655abf-bc28-4e41-a4f2-36652d42fbcf <br/>
https://github.com/Glevelll/Android-development/assets/113721736/5ec4fe0d-9f78-44b8-b789-2de8b4529758 <br/>
https://github.com/Glevelll/Android-development/assets/113721736/7ab390f8-b452-4276-be29-633c99f2fa3b <br/>

The Favorites fragment is built as two tabs that display those who liked the current user and those who liked the current user. Among those who liked the user, you can find those whom he liked in the search fragment. These people can be removed by clicking on the cross and see full information about them by clicking on the photo. In the tab with those who liked the current user, you can see those who liked him in their session. You can also like them and see information about them. If there is a mutual like, then this person is displayed in the feed at the top, which can be scrolled. In addition, with a mutual like, in the full information window you can see telegrams and whatsapp, which correspond to the phone number of the user of interest. By clicking on them, there is a transition to telegram or whatsapp

