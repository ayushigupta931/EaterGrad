# EaterGrad

<b> Problem Statement: </b>

Many concerns have arisen as a result of our students dining in college mess over a long period of time. One of the obstacles they confront on a daily basis is communicating their presence for the meal during meal hours. On the other side, the mess administration is uncertain of the number of students who will be having a certain meal, resulting in food waste.
Technology plays an important role in everyday activities, which has resulted in significant changes in various work domains, one of which is the Mobile Application. Mobile applications, which are extensively utilised and known for their ease of use, can be employed efficiently for this job.

<b> Proposed Solution : </b>

This project proposes a Meal Tracking System to keep track of an individual's choice of having a certain meal in the college mess. It also provides the mess administration the exact number of students having that meal. Students and mess administration members may login using the provided IDs, allowing students to make their choices while also assisting the administration in keeping track. The application displays the menu for each meal of the week and saves the student's selection in the database. Its capabilities include estimating the expenditure to date (based on meal selections) and displaying previous meal records.

<img width = "559" alt = "sampleImage" src = "https://user-images.githubusercontent.com/85157266/148817719-f0a7a4e1-a46e-4271-a439-c85ddabcb403.png"/>


<b> Functionality & Concepts used : </b>

- The App has a very simple and interactive interface which helps the students make their selection about having a meal and keeps the administration informed about the same. Following are few android concepts used to achieve the functionalities in app : 
- Simple & Easy Views Design : The app provide an intuitive and easy to use UI consisting of swipeable and expandable cards with interactive buttons making it easier for students to track their mess bills.
- Kotlin coroutines : We are leveraging kotlin coroutines for performing asynchronous work throughout the application. 
- Navigation Library : We are using single activity architecture in the application. Different screens are represented as Fragments and Androidx Navigation Library is used to navigate between them.
- LiveData & Room Database : We are also using LiveData to update & observe any changes in the student's meal preference which is stored in local SQL Database created using Room Persistence Library.
- Firebase Firestore : Food menus and student's meal preference are backed up online using Firebase Cloud Firestore. 
- Google Authentication : Student and mess administration are being authenticated using Google Sign-In provider.
- RecyclerView : To present the list of menus of different meals and display the previous meal records we used an efficient RecyclerView.
- TabLayout : The main fragment uses a TabLayout to smoothly switch between different days.
- ViewPager2 : The ViewPager instantiates Fragments according to the days and shows the respective menu.

<b> Application Link & Future Scope : </b>

You can access the app : [APK LINK HERE](https://github.com/ayushigupta931/EaterGrad/blob/master/EaterGrad.apk).

**Note: Right now, the admin portal is for the mess staff only.**
