# PopularMovies

### Getting Started

Add your API key for both TheMovieDb and YouTube to gradle.properties in Gradle Scripts

TheMovieDbAPIKey="yourTheMovieDBAPIKey", and YouTubeAPIKey="yourYouTubeAPIKey"

### Project Overview

Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, you’ll build an app to allow users to discover the most popular movies playing. We will split the development of this app in two stages. First, let's talk about stage 1.

In this stage you’ll build the core experience of your movies app.

Your app will:

Present the user with a grid arrangement of movie posters upon launch.
Allow your user to change sort order via a setting:

The sort order can be by most popular or by highest-rated

Allow the user to tap on a movie poster and transition to a details screen with additional information such as:

original title
movie poster image thumbnail
A plot synopsis (called overview in the api)
user rating (called vote_average in the api)
release date

### Why this Project?
To become an Android developer, you must know how to bring particular mobile experiences to life. Specifically, you need to know how to build clean and compelling user interfaces (UIs), fetch data from network services, and optimize the experience for various mobile devices. You will hone these fundamental skills in this project.

By building this app, you will demonstrate your understanding of the foundational elements of programming for Android. Your app will communicate with the Internet and provide a responsive and delightful user experience.

In Stage 2:

You’ll add more information to your movie details view:

You’ll allow users to view and play trailers (either in the youtube app or a web browser).
You’ll allow users to read reviews of a selected movie.
You’ll also allow users to mark a movie as a favorite in the details view by tapping a button (star).
You'll make use of Android Architecture Components (Room, LiveData, ViewModel and Lifecycle) to create a robust an efficient application.
You'll create a database using Room to store the names and ids of the user's favorite movies (and optionally, the rest of the information needed to display their favorites collection while offline).
You’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.


![Alt Text](https://github.com/jfussinger/PopularMovies/blob/Revised/PopularMovies.png)

![Alt Text](https://github.com/jfussinger/PopularMovies/blob/Revised/Detail%20Screen.png)

![Alt Text](https://github.com/jfussinger/PopularMovies/blob/Revised/Add%20to%20Favorites.png)

![Alt Text](https://github.com/jfussinger/PopularMovies/blob/Revised/Videos%20and%20Reviews.png)

![Alt Text](https://github.com/jfussinger/PopularMovies/blob/Revised/YouTube%20Activity%20Portrait%20Still%20Image.png)

![Alt Text](https://github.com/jfussinger/PopularMovies/blob/Revised/Favorite%20Movies.png)





