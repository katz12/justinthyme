justinthyme
===========

CS411 Databases project - Recipe website

/justinthyme - Contains django settings and urls files.

/recipe_app - Contains models and views for front end web app.

/sql - Contains raw sqlite3 file.

GIT Workflow
=============

1. Open up this page. (Man you're good at this!)
2. Open a git shell and change to top /justinthyme
3. run `git pull` to get any new changes.
4. Do some work. Do a lot actually.
5. It's been a while since step 4, run `git status` and look at all the stuff you've changed!
6. Make sure all of the file you have changed are under **staged for commit** and not **untracked**. For any files under **untracked**, run `git add filename` to add those new files.
7. Now you're ready to commit. Run `git commit -m "talk about what changes you did here"`
8. Last but not least, run `git push` to push your changes to the remote repository.

**IMPORTANT:** Don't forget to push your work after you're done or else you will have to do merges (ewww).

Running Django
=================

To run the shell: run `/path/to/python manage.py shell` 
To start the webapp: run `/path/to/python manage.py runserver`. You can view it at 127.0.0.1:8000/admin
To view sql schema: run `/path/to/python manage.py sql recipe_app` 
