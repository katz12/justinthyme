from django.shortcuts import render_to_response
from recipe_app.models import Recipe
from django.http import HttpResponse

from django.db import transaction, connection
import json

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

# Andy add views here
def api_test(request):
    return HttpResponse(json.dumps({'test' : 'passed'}), mimetype="application/json")

def api_search(request):
    search_text = request.GET.get('search_text')
    results = Recipe.objects.raw('select * from recipe join recipe_ingredient on recipe.id=recipe_ingredient.recipe_id where name like %s', ['%' + search_text + '%'])

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

    return HttpResponse(json.dumps(recipes), mimetype="application/json")
