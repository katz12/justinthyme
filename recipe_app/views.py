from django.shortcuts import render_to_response
from recipe_app.models import Recipe, User
from django.http import HttpResponse

from django.db import transaction, connection
import json
import hashlib, uuid

salt = '4b3807c84925478fabb40091f8646d11'

def test(request):
    return render_to_response('test.html')

def name_search(request):
    search = request.GET.get('search')
    results = Recipe.objects.raw('select * from recipe where name like %s',['%' + search + '%'])
    return render_to_response('test/name_search.html', {'results' : results})

def recipe_insert(request):
    name = request.GET.get('name')
    url = request.GET.get('url')
    cook_time = request.GET.get('cook_time')
    img_url = request.GET.get('img_url')

    cursor = connection.cursor()
    values = [name,url,cook_time,img_url]
    try:
        cursor.execute('insert into recipe (name,url,cook_time,img_url) values(%s,%s,%s,%s)', values)
    except Exception, e:
        print e
        return HttpResponse(status=500)
    transaction.commit_unless_managed()
    return HttpResponse()

# Derek add views here

# Katie add views here
def index(request):
    return render_to_response('index.html')

def contact(request):
    return render_to_response('site/ContactUs.html')

def about(request):
    return render_to_response('site/AboutUs.html')

def print_page(request):
    ingredient_result = Recipe.objects.raw('select * from recipe, recipe_ingredient where recipe.id == 422 AND recipe.id == recipe_ingredient.recipe_id')
    result = Recipe.objects.raw('select * from recipe where recipe.id == 422')
    return render_to_response('site/result.html', {'result': result, 'ingredient_result': ingredient_result})

# Michal add views here
def search(request):
    return render_to_response('search/search.html')
def recipe_search(request):
    search = request.GET.get('search')
    results = Recipe.objects.raw('select * from recipe where name like %s',['%' + search + '%'])
    return render_to_response('search/name_search.html', {'results' : results})

# Andy add views here
def api_test(request):
    return HttpResponse(json.dumps({'test' : 'passed'}), mimetype="application/json")

def api_login(request):
    user_name = request.GET.get('user_name')
    password = request.GET.get('password')
    hashed_password = hashlib.sha512(password + salt).hexdigest()
    result = User.objects.raw('select * from user where name = %s AND pass_hash = %s', [user_name, hashed_password])

    if len(list(result)) == 1:
        return HttpResponse(json.dumps({'success' : True, 'user_name' : user_name}), mimetype="application/json")
    else:
        return HttpResponse(json.dumps({'success' : False}), mimetype="application/json")
    

def api_search(request):
    search_text = request.GET.get('search_text')
    page = request.GET.get('page')
    if page is not None:
        offset = int(page)*10
    else:
        offset = 0
        
    results = Recipe.objects.raw('select * from recipe_ingredient join (select * from recipe where name like %s limit %s,10) as R on R.id=recipe_ingredient.recipe_id', ['%' + search_text + '%', offset])

    recipes = []
    recipe_id = -1
    for row in results:
        if (recipe_id != row.recipe_id):
            if (recipe_id != -1):
                recipe['ingredients'] = ingredients
                recipes.append(recipe)
            recipe_id = row.recipe_id
            recipe = {}
            recipe['id'] = row.recipe_id
            recipe['url'] = row.url
            recipe['img_url'] = row.img_url
            recipe['name'] = row.name
            recipe['description'] = row.description
            recipe['servings'] = row.servings
            recipe['course'] = row.course
            recipe['method'] = row.method
            recipe['difficulty'] = row.difficulty
            recipe['wait_time'] = row.wait_time
            recipe['prep_time'] = row.prep_time
            recipe['cook_time'] = row.cook_time

            ingredients = []
            ingredients.append({'quantity' : row.quantity,
                                'name' : row.ingredient_name})
        else:
            ingredients.append({'quantity' : row.quantity,
                                'name' : row.ingredient_name})

    # One last add to get last result
    recipe['ingredients'] = ingredients
    recipes.append(recipe)

    return HttpResponse(json.dumps({'recipes' : recipes}), mimetype="application/json")
