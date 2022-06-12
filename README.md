# RepoBrowser
This is a basic demonstration of what I am familliar with and what I can create.

The app is a github repository browser.

Uses clean MVVM architecture.

Features:
- Login using OAuth Github + Firebase as backend
- Cache the token and user data to skip login next time
- Type a name filter request and get a page with 30 elements
- The requested page of 30 elements is loaded in 2 separate threads
- Page parts are published using Kotlin flow
- The filtered list supports further pagination on scroll
- Items are clickable and lead to the browser repository preview
- Clicked items are cached (alongside date of access)
- Cached clicked items are visible on the "History" page
- Logout by clicking on the options menu

TODO (known issues):
- Add user data encryption before saving to DB
