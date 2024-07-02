# Nutrition_tracker

# Introduction
This is a University project which was meant to be an android app that helps you decide your meal plan.<br>
Me and my colleague Milan Marinković worked on this project together. You will find his github here: https://github.com/marinZv<br>
The original documentation is placed in the "Nutrition tracked.docx" file.<br>
The app uses two API's, one for the meals: https://themealdb.com/<br>
and the other one for fetching the calories for the meals based on their ingredients: https://api-ninjas.com/api/nutrition<br>
The language used was Kotlin.
<hr>

# What you can do with the app
You can search meals by categories, name, ingredients, area, tag.<br>
You can save meals to certain days and see if it exceeds your daily intake.<br>
You can also calculate your suggested intake based on your height, weight, age, sex and activity level.<br>
For each meal you can see the detailed screen with ingredients, explanation on how to prepare the meal,<br>
the amount of calories it has, a link to the youtube video tutorial.<br>
You can create a weekly plan which can be sent to you via email.<br>
There is a screen with statistics on how many meals you added for a certain day in the past 7 days,<br>
and also a page with calories added in the past 7 days.(Parts of graph where the calories of meals added<br>
exceed the daily intake are colored red)<br>
There are other functionalities but these are the most important.<br>
<hr>

# Libraries used
Retrofit, Koin, Gson
<hr>

# The app itself

Splash screen:

![Splash](pictures/2.png)

Login screen:

![Login](pictures/1.png)

Meal categories screen:

![Categories](pictures/1.png)

Categories description:

![Categories_desc](pictures/14.png)

Meals in a category:

![Meals](pictures/13.png)

Saved meals:

![Saved_Meals](pictures/12.png)

Detailed meal screen:

![Detailed_meal](pictures/11.png)

Saving a certain meal:

![Saving_meal](pictures/10.png)

Fetching and sorting meals by calories:

![Calories](pictures/6.png)

After sorting by calories:

![Calories_sort](pictures/5.png)

Statistics on added meals:

![Statistics_meals](pictures/9.png)

Statistics on added calories:

![Statistics_calories](pictures/7.png)

Calculate daily intake for yourself:

![Intake](pictures/8.png)

Filter meals by category, area, ingredient, tag, name:

![Filter](pictures/4.png)

Create a weekly plan and send it to your email:

![Plan](pictures/3.png)
